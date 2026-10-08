package com.example.sgm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VehiclesList(
    vehiclesViewModel: VehiclesViewModel,
    onNavigateToForm: (Int) -> Unit
) {
    val uiState by vehiclesViewModel.uiState.collectAsState()

    val isActionLoading by vehiclesViewModel.isActionLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(23, 23, 23),
                text = "Veículos"
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(87, 83, 78),
                text = "Nessa tela, você verá todos os veículos que você cadastrou e poderá gerenciá-los"
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (val state = uiState) {
                is VehiclesUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(23, 23, 23))
                    }
                }

                is VehiclesUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = Color(220, 38, 38),
                            fontSize = 14.sp
                        )
                    }
                }

                is VehiclesUiState.Success -> {
                    if (state.vehicles.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nenhum veículo cadastrado.",
                                color = Color(87, 83, 78),
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(state.vehicles) { vehicleWithGroup ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(245, 245, 245))
                                        .border(
                                            1.dp,
                                            Color(212, 212, 212),
                                            RoundedCornerShape(16.dp)
                                        )
                                        .clickable { onNavigateToForm(vehicleWithGroup.vehicle.id) }
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Text(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(23, 23, 23),
                                            text = vehicleWithGroup.vehicle.name
                                        )
                                        vehicleWithGroup.group?.name?.let { groupName ->
                                            Text(
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = Color(87, 83, 78),
                                                text = groupName
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }

        if (isActionLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}