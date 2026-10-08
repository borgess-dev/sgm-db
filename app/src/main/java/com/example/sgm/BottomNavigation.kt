package com.example.sgm

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController

@Composable
fun BottomNavigation(
    navigationController: NavHostController
) {

    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screens = listOf(
        Screen("vehicles", "Veículos", R.drawable.baseline_directions_car_24),
        Screen("groups", "Grupos", R.drawable.outline_ad_group_24),
    )

    val routes = screens.map { it.route }

    if(currentRoute in routes) {
        NavigationBar() {
            screens.forEach { route ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(route.icon),
                            contentDescription = route.title
                        )
                    },
                    label = { Text(route.title) },
                    selected = currentRoute == route.route,
                    onClick = {
                        navigationController.navigate(route.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}