package com.riders.thelab.core.domain.repository

import com.google.firebase.storage.StorageReference
import com.riders.thelab.core.common.utils.Resource

interface IFirebaseRepository {
    suspend fun getStorageReference(): StorageReference?
    suspend fun getStorageReferenceAsResource(): Resource<StorageReference?>
}