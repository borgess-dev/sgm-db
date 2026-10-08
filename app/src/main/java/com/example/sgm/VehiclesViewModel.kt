package com.example.sgm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VehiclesViewModel(
    private val vehicleDao: VehicleDao
) : ViewModel() {

    val uiState: StateFlow<VehiclesUiState> = vehicleDao.getVehiclesWithGroup()
        .map<List<VehicleWithGroup>, VehiclesUiState> { vehiclesList ->
            VehiclesUiState.Success(vehicles = vehiclesList)
        }
        .catch { throwable ->
            emit(VehiclesUiState.Error(message = throwable.message ?: "Erro ao carregar veículos"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = VehiclesUiState.Loading
        )
    private val _isActionLoading = MutableStateFlow(false)
    val isActionLoading: StateFlow<Boolean> = _isActionLoading.asStateFlow()

    suspend fun findById(id: Int?): Vehicle? {
        if (id == null || id == 0) return null
        return vehicleDao.findById(id)
    }

    fun save(
        id: Int?,
        name: String,
        model: String,
        year: String,
        groupId: Int?,
        isActive: Boolean,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                val vehicle = Vehicle(
                    id = id ?: 0,
                    name = name,
                    model = model,
                    year = year,
                    groupId = groupId,
                    isActive = isActive
                )

                if (id != null && id != 0) {
                    vehicleDao.update(vehicle)
                } else {
                    vehicleDao.insert(vehicle)
                }
                onSuccess?.invoke()
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Erro ao salvar veículo")
            } finally {
                _isActionLoading.value = false
            }
        }
    }

    fun remove(
        vehicle: Vehicle,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                vehicleDao.delete(vehicle)
                onSuccess?.invoke()
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Erro ao remover veículo")
            } finally {
                _isActionLoading.value = false
            }
        }
    }
}