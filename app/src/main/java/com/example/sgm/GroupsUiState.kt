package com.example.sgm

sealed interface GroupsUiState {
    object Loading : GroupsUiState
    data class Success(val groups: List<Group>) : GroupsUiState
    data class Error(val message: String) : GroupsUiState
}