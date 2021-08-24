package com.riders.thelab.ui.googledrive

import android.content.IntentSender
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.FileList
import com.riders.thelab.databinding.ActivityGoogleDriveBinding
import timber.log.Timber
import java.io.*
import java.util.*


class GoogleDriveActivity : AppCompatActivity(), OnConnectionFailedListener {
    companion object {

        private const val APPLICATION_NAME: String = "Google Drive API Java Quickstart"
        private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()
        private const val TOKENS_DIRECTORY_PATH: String = "tokens"

        /**
         * Global instance of the scopes required by this quickstart.
         * If modifying these scopes, delete your previously saved tokens/ folder.
         */
        private val SCOPES: List<String> =
            Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
        private const val CREDENTIALS_FILE_PATH: String = "/credentials.json";
    }

    lateinit var viewBinding: ActivityGoogleDriveBinding

    private lateinit var mGoogleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityGoogleDriveBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        // Build a new authorized API client service.
        val HTTP_TRANSPORT: NetHttpTransport = com.google.api.client.http.javanet.NetHttpTransport()
        val service: Drive = Drive.Builder(
            HTTP_TRANSPORT,
            JSON_FACTORY,
            getCredentials(HTTP_TRANSPORT)
        )
            .setApplicationName(APPLICATION_NAME)
            .build();

        // Print the names and IDs for up to 10 files.
        val result: FileList = service.files().list()
            .setPageSize(10)
            .setFields("nextPageToken, files(id, name)")
            .execute();
        val files: MutableList<com.google.api.services.drive.model.File>? = result.files;
        if (files == null || files.isEmpty()) {
            println("No files found.");
        } else {
            println("Files:");
            for (file in files) {
                System.out.printf("%s (%s)\n", file.name, file.id);
            }
        }
    }

    override fun onResume() {
        super.onResume()

        //mGoogleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()

        // disconnect Google Android Drive API connection.
        //mGoogleApiClient.disconnect();

    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    @Throws(IOException::class)
    fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): HttpRequestInitializer {
        // Load client secrets.
        val mInputStream: InputStream =
            this@GoogleDriveActivity.javaClass.getResourceAsStream(CREDENTIALS_FILE_PATH)
                ?: throw  FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH)
        val clientSecrets: GoogleClientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(mInputStream));

        // Build flow and trigger user authorization request.
        val flow: GoogleAuthorizationCodeFlow = GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
        )
            .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();
        val receiver: LocalServerReceiver = LocalServerReceiver.Builder().setPort(8888).build();
        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently

        // Called whenever the API client fails to connect.
        Timber.e("GoogleApiClient connection failed: %s", result.toString());

        if (!result.hasResolution()) {

            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.errorCode, 0)?.show();
            return;
        }

        /**
         *  The failure has a resolution. Resolve it.
         *  Called typically when the app is not yet authorized, and an  authorization
         *  dialog is displayed to the user.
         */

        try {

            //result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);

        } catch (e: IntentSender.SendIntentException) {

            e.printStackTrace()
            Timber.e("Exception while starting resolution activity");
        }
    }

}