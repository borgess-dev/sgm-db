package com.example.sgm

sealed interface VehiclesUiState {
    object Loading : VehiclesUiState
    data class Success(val vehicles: List<VehicleWithGroup>) : VehiclesUiState
    data class Error(val message: String) : VehiclesUiState
}