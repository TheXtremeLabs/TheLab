package com.riders.thelab.core.data.repository

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.common.utils.runCatching
import com.riders.thelab.core.data.BuildConfig
import com.riders.thelab.core.data.remote.IApi
import com.riders.thelab.core.domain.repository.IFirebaseRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val api: IApi
) : IFirebaseRepository {

    private val executor: Executor = Executors.newSingleThreadExecutor()

    override suspend fun getStorageReference(): StorageReference? = runCatching(
        block = {
            Timber.e("getStorageReference()")

            val storage = arrayOfNulls<FirebaseStorage>(1)

            // Initialize Firebase Auth
            val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance()
                .signInAnonymously()
                .await()
                .user
                ?.also { user ->
                    if (BuildConfig.DEBUG) {
                        Timber.d("getStorageReference() | signInAnonymously | result user: ${user.displayName} (is anonymous: ${user.isAnonymous})")
                    }
                }

            return@runCatching if (null == firebaseUser) {
                Timber.e("getStorageReference() | failed")
                null
            } else {
                // Sign in success, update UI with the signed-in user's information
                Timber.i("getStorageReference() | success, get firebase instance and fetch bucket")

                val bucketName = "gs://the-lab-3920e.appspot.com"
                val bucketInstance = FirebaseStorage.getInstance(bucketName)

                storage[0] = bucketInstance.also { result ->
                    if (BuildConfig.DEBUG) {
                        Timber.d("getStorageReference() | FirebaseStorage.getInstance(bucketName) | result : ${result.reference.name}")
                    }
                }

                // Create a storage reference from our app
                val storageRef: StorageReference? = storage[0]?.reference?.also { ref ->
                    if (BuildConfig.DEBUG) {
                        Timber.d("getStorageReference() | reference : ${ref.name}")
                    }
                }
                storageRef
            }
        },
        exceptionBlock = { exception ->
            // If sign in fails, display a message to the user.
            Timber.w("getStorageReference() | signInAnonymously:failure %s", exception.toString())
            /*withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }*/
        }
    )
        .getOrNull()

    override suspend fun getStorageReferenceAsResource(): Resource<StorageReference?> =
        getStorageReference()?.let { Resource.Success(data = it) }
            ?: Resource.Error(message = "Error")
}