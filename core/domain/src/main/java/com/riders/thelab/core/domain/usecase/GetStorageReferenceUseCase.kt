package com.riders.thelab.core.domain.usecase

import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.common.utils.Resource
import com.riders.thelab.core.common.utils.runCatching
import com.riders.thelab.core.domain.repository.IFirebaseRepository
import com.riders.thelab.core.domain.utils.UseCase
import timber.log.Timber
import javax.inject.Inject

class GetStorageReferenceUseCase @Inject constructor(
    private val firebaseRepository: IFirebaseRepository
) : UseCase<String, StorageReference?> {

    override suspend fun invoke(
        params: String
    ): Resource<StorageReference?> = runCatching(
        block = {
            when (val result = firebaseRepository.getStorageReferenceAsResource()) {
                is Resource.Success -> {
                    // Create a child reference
                    // imagesRef now points to "images"
                    val reference: StorageReference? = result
                        .data
                        ?.also { Timber.d("GetStorageReferenceUseCase.invoke() | storage reference : ${it.name}") }
                        ?.child(params)

                    reference?.let { Resource.Success(data = it) }
                        ?: Resource.Error(message = "Error")
                }

                is Resource.Error -> {
                    return@runCatching Resource.Error(
                        message = result.message,
                        throwable = result.throwable
                    )
                }
            }
        },
        exceptionBlock = { throwable ->
            Timber.e("GetStorageReferenceUseCase.invoke() | throwable : ${throwable.message}")
        }
    )
        .getOrElse { throwable ->
            Resource.Error(
                message = throwable.message ?: "Error occurred while getting value",
                throwable = throwable
            )
        }
}