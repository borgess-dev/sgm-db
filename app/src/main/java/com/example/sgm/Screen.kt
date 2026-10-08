package com.example.sgm

import kotlinx.serialization.Serializable

@Serializable
data class Screen(val route: String, val title: String, val icon: Int) {
}