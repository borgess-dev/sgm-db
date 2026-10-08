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
fun GroupList(
    groupsViewModel: GroupsViewModel,
    onNavigateToForm: (Int) -> Unit
) {
    val uiState by groupsViewModel.uiState.collectAsState()

    val isActionLoading by groupsViewModel.isActionLoading.collectAsState()

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
                text = "Grupos de Veículos"
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(87, 83, 78),
                text = "Nessa tela, você verá todos os grupos de veículos que você cadastrou e poderá gerenciá-los"
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (val state = uiState) {
                is GroupsUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(23, 23, 23))
                    }
                }

                is GroupsUiState.Error -> {
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

                is GroupsUiState.Success -> {
                    if (state.groups.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nenhum grupo de veículos cadastrado.",
                                color = Color(87, 83, 78),
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(state.groups) { group ->
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
                                        .clickable { onNavigateToForm(group.id) }
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Text(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(23, 23, 23),
                                            text = group.name
                                        )
                                        Text(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color(87, 83, 78),
                                            text = group.description
                                        )
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