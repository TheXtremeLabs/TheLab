package com.riders.thelab.feature.googledrive.utils

import com.google.api.services.drive.DriveScopes

object Constants {
    const val TOKENS_DIRECTORY_PATH: String = "tokens"

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    val SCOPES: List<String> = listOf(
        DriveScopes.DRIVE,
        DriveScopes.DRIVE_FILE,
        DriveScopes.DRIVE_PHOTOS_READONLY,
        DriveScopes.DRIVE_METADATA_READONLY
    )
    const val CREDENTIALS_FILE_PATH: String = "/credentials.json"

    const val REQUEST_SIGN_IN = 1238
    const val CONST_SIGN_IN = 1236
}