package com.example.sgm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleForm(
    id: Int?,
    vehiclesViewModel: VehiclesViewModel,
    groupsViewModel: GroupsViewModel,
    onBack: () -> Unit
) {
    var isLoadingData by remember { mutableStateOf(id != null && id != 0) }
    var vehicleToEdit by remember { mutableStateOf<Vehicle?>(null) }

    var nameInput by rememberSaveable { mutableStateOf("") }
    var modelInput by rememberSaveable { mutableStateOf("") }
    var yearInput by rememberSaveable { mutableStateOf("") }
    var isActiveInput by rememberSaveable { mutableStateOf(true) }

    var selectedGroup by remember { mutableStateOf<Group?>(null) }

    var showSheet by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }

    val groupsUiState by groupsViewModel.uiState.collectAsState()
    val isActionLoading by vehiclesViewModel.isActionLoading.collectAsState()

    LaunchedEffect(id) {
        if (id != null && id != 0) {
            val vehicle = vehiclesViewModel.findById(id)
            vehicleToEdit = vehicle
            if (vehicle != null) {
                nameInput = vehicle.name
                modelInput = vehicle.model
                yearInput = vehicle.year
                isActiveInput = vehicle.isActive

                if (vehicle.groupId != null) {
                    selectedGroup = groupsViewModel.findById(vehicle.groupId.hashCode())
                }
            }
            isLoadingData = false
        }
    }

    if (isLoadingData) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(23, 23, 23))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Text(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(23, 23, 23),
                text = if (id == null || id == 0) "Novo Veículo" else "Editar Veículo"
            )

            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(87, 83, 78),
                text = "Cadastre um veículo preenchendo o formulário abaixo!"
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Apelido") },
                placeholder = { Text("Dê um apelido ao veículo") },
                value = nameInput,
                enabled = !isActionLoading,
                onValueChange = { nameInput = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Modelo") },
                placeholder = { Text("Determine o modelo") },
                value = modelInput,
                enabled = !isActionLoading,
                onValueChange = { modelInput = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Ano") },
                placeholder = { Text("Determine o ano") },
                value = yearInput,
                enabled = !isActionLoading,
                onValueChange = { yearInput = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isActionLoading) { showSheet = true }
            ) {
                TextField(
                    value = selectedGroup?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Grupo do Veículo") },
                    placeholder = { Text("Selecione um grupo") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            label = { Text("Buscar grupo...") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        when (val state = groupsUiState) {
                            is GroupsUiState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Color(23, 23, 23))
                                }
                            }

                            is GroupsUiState.Error -> {
                                Text(
                                    text = state.message,
                                    color = Color(220, 38, 38),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }

                            is GroupsUiState.Success -> {
                                val filteredGroups = state.groups.filter {
                                    it.name.contains(query, ignoreCase = true)
                                }

                                if (filteredGroups.isEmpty()) {
                                    Text(
                                        text = "Nenhum grupo encontrado.",
                                        color = Color(87, 83, 78),
                                        modifier = Modifier.padding(16.dp)
                                    )
                                } else {
                                    LazyColumn {
                                        items(filteredGroups) { groupItem ->
                                            Text(
                                                text = groupItem.name,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        selectedGroup = groupItem
                                                        showSheet = false
                                                    }
                                                    .padding(vertical = 12.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isActiveInput,
                    enabled = !isActionLoading,
                    onCheckedChange = { isActiveInput = it }
                )
                Text("Ativo")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (vehicleToEdit != null) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(220, 38, 38),
                            contentColor = Color.White
                        ),
                        enabled = !isActionLoading,
                        onClick = {
                            vehicleToEdit?.let { vehicle ->
                                vehiclesViewModel.remove(
                                    vehicle = vehicle,
                                    onSuccess = { onBack() }
                                )
                            }
                        }
                    ) {
                        Text("Excluir")
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(229, 229, 229),
                        contentColor = Color(23, 23, 23)
                    ),
                    enabled = !isActionLoading,
                    onClick = { onBack() }
                ) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    enabled = !isActionLoading && nameInput.isNotBlank(),
                    onClick = {
                        vehiclesViewModel.save(
                            id = id,
                            name = nameInput,
                            model = modelInput,
                            year = yearInput,
                            groupId = selectedGroup?.id,
                            isActive = isActiveInput,
                            onSuccess = { onBack() }
                        )
                    }
                ) {
                    if (isActionLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}