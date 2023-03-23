package com.riders.thelab.data.local.model.compose

import com.riders.thelab.data.remote.dto.ApiResponse

sealed class LoginUiState {
    data class Success(val response: ApiResponse) : LoginUiState()
    data class Error(val errorResponse: ApiResponse? = null) : LoginUiState()
    object Loading : LoginUiState()
    object None : LoginUiState()
}
