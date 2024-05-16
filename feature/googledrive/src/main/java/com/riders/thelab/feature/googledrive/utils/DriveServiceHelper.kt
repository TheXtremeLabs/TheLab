package com.riders.thelab.feature.googledrive.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import timber.log.Timber
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
class DriveServiceHelper<T : Activity>(activity: T) {

    private var mWeakRefActivity: WeakReference<T>? = null
    private var mDriveService: Drive? = null
    private val mExecutor: Executor = Executors.newSingleThreadExecutor()
    private val mJsonFactory: JsonFactory = GsonFactory.getDefaultInstance()

    init {
        Timber.i("init method")

        mWeakRefActivity = WeakReference<T>(activity)

        mDriveService = runCatching {
            // Build a new authorized API client service.
            val httpTransport = NetHttpTransport()
            val drive: Drive = Drive.Builder(
                httpTransport,
                mJsonFactory,
                getCredentials(httpTransport)
            )
                .setApplicationName("TheLab")
                .build()

            drive
        }
            .onFailure {
                it.printStackTrace()
                Timber.e("init | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
            }
            .onSuccess {
                Timber.d("init | onSuccess | is success: $it")
            }
            .getOrNull()
    }

    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */
    fun createFile(): Task<String> {
        Timber.d("createFile()")

        return Tasks.call(mExecutor) {
            val metadata = File()
                .setParents(listOf("root"))
                .setMimeType("text/plain")
                .setName("Untitled file")
            val googleFile = mDriveService?.files()?.create(metadata)?.execute()
                ?: throw IOException("Null result when requesting file creation.")
            googleFile.id
        }
    }

    /**
     * Opens the file identified by `fileId` and returns a [Pair] of its name and
     * contents.
     */
    fun readFile(fileId: String?): Task<Pair<String, String>> {
        Timber.d("readFile()")

        return Tasks.call(mExecutor) {

            // Retrieve the metadata as a File object.
            val metadata = mDriveService?.files()?.get(fileId)?.execute()
            val name = metadata?.name

            mDriveService?.files()
                ?.get(fileId)
                ?.executeMediaAsInputStream()
                .use { stream ->
                    BufferedReader(InputStreamReader(stream)).use { reader ->
                        val stringBuilder = StringBuilder()
                        var line: String?

                        while ((reader.readLine().also { line = it }) != null) {
                            stringBuilder.append(line)
                        }
                        val contents = stringBuilder.toString()
                        return@call name?.let { Pair(it, contents) } ?: Pair("", "")
                    }
                }
        }
    }

    /**
     * Updates the file identified by `fileId` with the given `name` and `content`.
     */
    fun saveFile(fileId: String?, name: String?, content: String?): Task<Any?> {
        Timber.d("saveFile() | fileId: ${fileId}, name: ${name}, content: $content")

        return Tasks.call<Any?>(
            mExecutor
        ) {
            // Create a File containing any metadata changes.
            val metadata = File().setName(name)

            // Convert content to an AbstractInputStreamContent instance.
            val contentStream =
                ByteArrayContent.fromString("text/plain", content)

            // Update the metadata and contents.
            mDriveService?.files()?.update(fileId, metadata, contentStream)?.execute()
            null
        }
    }

    /**
     * Returns a [FileList] containing all the visible files in the user's My Drive.
     *
     *
     * The returned list will only contain files visible to this app, i.e. those which were
     * created by this app. To perform operations on files not created by the app, the project must
     * request Drive Full Scope in the [Google
     * Developer's Console](https://play.google.com/apps/publish) and be submitted to Google for verification.
     */
    fun queryFiles(): Task<FileList> {
        Timber.d("queryFiles()")

        /*mDriveService?.let { service ->
            Tasks.forResult(
                service.files()
                    ?.list()
                    ?.setSpaces("drive")
                    ?.execute()
            )
        } ?: run {
            Timber.e("queryFiles() | mDriveService is null")
            null
        }*/

        return Tasks.call(mExecutor) {
            mDriveService?.files()
                ?.list()
                ?.setSpaces("drive")
                ?.execute()
                ?: run {
                    Timber.e("queryFiles() | mDriveService is null")
                    null
                }
        }
    }

    /**
     * Returns an [Intent] for opening the Storage Access Framework file picker.
     */
    fun createFilePickerIntent(): Intent {
        Timber.d("createFilePickerIntent()")

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("text/plain")

        return intent
    }

    /**
     * Opens the file at the `uri` returned by a Storage Access Framework [Intent]
     * created by [.createFilePickerIntent] using the given `contentResolver`.
     */
    fun openFileUsingStorageAccessFramework(
        contentResolver: ContentResolver,
        uri: Uri?
    ): Task<Pair<String, String>> {
        Timber.d("openFileUsingStorageAccessFramework()")

        return Tasks.call(mExecutor) {
            // Retrieve the document's display name from its metadata.
            var name: String
            contentResolver.query(uri!!, null, null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val nameIndex =
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    name = cursor.getString(nameIndex)
                } else {
                    throw IOException("Empty cursor returned for file.")
                }
            }
            // Read the document's contents as a String.
            var content: String
            contentResolver.openInputStream(uri).use { `is` ->
                BufferedReader(InputStreamReader(`is`)).use { reader ->
                    val stringBuilder = StringBuilder()
                    var line: String?
                    while ((reader.readLine().also { line = it }) != null) {
                        stringBuilder.append(line)
                    }
                    content = stringBuilder.toString()
                }
            }
            Pair(name, content)
        }
    }


    /**
     * Creates an authorized Credential object.
     * @param netHttpTransport The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    @Throws(IOException::class)
    fun getCredentials(netHttpTransport: NetHttpTransport): HttpRequestInitializer? {
        Timber.d("getCredentials()")

        return mWeakRefActivity?.get()?.let { activity ->

            // Load client secrets.
            val mInputStream: InputStream =
                activity.javaClass.getResourceAsStream(Constants.CREDENTIALS_FILE_PATH)
                    ?: throw FileNotFoundException("Resource not found: ${Constants.CREDENTIALS_FILE_PATH}")
            val clientSecrets: GoogleClientSecrets =
                GoogleClientSecrets.load(mJsonFactory, InputStreamReader(mInputStream))

            // Build flow and trigger user authorization request.
            val flow: GoogleAuthorizationCodeFlow = GoogleAuthorizationCodeFlow.Builder(
                netHttpTransport, mJsonFactory, clientSecrets, Constants.SCOPES
            )
                .setDataStoreFactory(FileDataStoreFactory(java.io.File(Constants.TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build()
            val receiver: LocalServerReceiver = LocalServerReceiver.Builder().setPort(8888).build()
            return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
        } ?: run {
            return null
        }
    }

}