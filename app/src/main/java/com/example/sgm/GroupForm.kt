package com.example.sgm

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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

@Composable
fun GroupForm(
    id: Int?,
    groupsViewModel: GroupsViewModel,
    onBack: () -> Unit
) {
    var isLoadingData by remember { mutableStateOf(id != null && id != 0) }
    var groupToEdit by remember { mutableStateOf<Group?>(null) }

    var nameInput by rememberSaveable { mutableStateOf("") }
    var descriptionInput by rememberSaveable { mutableStateOf("") }
    var isActiveInput by rememberSaveable { mutableStateOf(true) }

    val isActionLoading by groupsViewModel.isActionLoading.collectAsState()

    LaunchedEffect(id) {
        if (id != null && id != 0) {
            val group = groupsViewModel.findById(id)
            groupToEdit = group
            if (group != null) {
                nameInput = group.name
                descriptionInput = group.description
                isActiveInput = group.isActive
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
                text = if (id == null || id == 0) "Novo Grupo" else "Editar Grupo"
            )

            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(87, 83, 78),
                text = "Cadastre um grupo de veículos preenchendo o formulário abaixo!"
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nome do Grupo") },
                placeholder = { Text("Digite o nome do grupo") },
                value = nameInput,
                enabled = !isActionLoading,
                onValueChange = { nameInput = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Descrição") },
                placeholder = { Text("Determine uma descrição") },
                value = descriptionInput,
                enabled = !isActionLoading,
                onValueChange = { descriptionInput = it }
            )

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
                if (groupToEdit != null) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(220, 38, 38),
                            contentColor = Color.White
                        ),
                        enabled = !isActionLoading,
                        onClick = {
                            groupToEdit?.let { group ->
                                groupsViewModel.remove(
                                    group = group,
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
                        groupsViewModel.save(
                            id = id,
                            name = nameInput,
                            description = descriptionInput,
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