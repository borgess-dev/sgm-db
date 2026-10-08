package com.example.sgm

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun Navigation(
    navigationController: NavHostController,
    vehicleDao: VehicleDao,
    groupDao: GroupDao,
    modifier: Modifier = Modifier
) {
    val vehiclesViewModel: VehiclesViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return VehiclesViewModel(vehicleDao) as T
            }
        }
    )

    val groupsViewModel: GroupsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return GroupsViewModel(groupDao) as T
            }
        }
    )

    NavHost(
        navController = navigationController,
        startDestination = "vehicles",
        modifier = modifier
    ) {

        composable("vehicles") {
            VehiclesList(
                vehiclesViewModel = vehiclesViewModel,
                onNavigateToForm = { id: Int? ->
                    if (id != null && id != 0) {
                        navigationController.navigate("vehicleForm?vehicleId=$id")
                    } else {
                        navigationController.navigate("vehicleForm")
                    }
                }
            )
        }

        composable(
            route = "vehicleForm?vehicleId={vehicleId}",
            arguments = listOf(
                navArgument("vehicleId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val vehicleIdArg = backStackEntry.arguments?.getInt("vehicleId")
            val vehicleId = if (vehicleIdArg == -1 || vehicleIdArg == 0) null else vehicleIdArg

            VehicleForm(
                id = vehicleId,
                vehiclesViewModel = vehiclesViewModel,
                groupsViewModel = groupsViewModel,
                onBack = { navigationController.popBackStack() }
            )
        }

        composable("groups") {
            GroupList(
                groupsViewModel = groupsViewModel,
                onNavigateToForm = { id: Int? ->
                    if (id != null && id != 0) {
                        navigationController.navigate("groupForm?groupId=$id")
                    } else {
                        navigationController.navigate("groupForm")
                    }
                }
            )
        }

        composable(
            route = "groupForm?groupId={groupId}",
            arguments = listOf(
                navArgument("groupId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val groupIdArg = backStackEntry.arguments?.getInt("groupId")
            val groupId = if (groupIdArg == -1 || groupIdArg == 0) null else groupIdArg

            GroupForm(
                id = groupId,
                groupsViewModel = groupsViewModel,
                onBack = { navigationController.popBackStack() }
            )
        }
    }
}