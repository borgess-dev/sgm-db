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

class GroupsViewModel(
    private val groupDao: GroupDao
) : ViewModel() {

    val uiState: StateFlow<GroupsUiState> = groupDao.getAllGroups()
        .map<List<Group>, GroupsUiState> { groupsList ->
            GroupsUiState.Success(groups = groupsList)
        }
        .catch { throwable ->
            emit(GroupsUiState.Error(message = throwable.message ?: "Erro ao carregar grupos"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GroupsUiState.Loading
        )

    private val _isActionLoading = MutableStateFlow(false)
    val isActionLoading: StateFlow<Boolean> = _isActionLoading.asStateFlow()

    suspend fun findById(id: Int?): Group? {
        if (id == null || id == 0) return null
        return groupDao.findById(id)
    }

    fun save(
        id: Int?,
        name: String,
        description: String,
        isActive: Boolean,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                val group = Group(
                    id = id ?: 0,
                    name = name,
                    description = description,
                    isActive = isActive
                )

                if (id != null && id != 0) {
                    groupDao.update(group)
                } else {
                    groupDao.insert(group)
                }
                onSuccess?.invoke()
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Erro ao salvar grupo")
            } finally {
                _isActionLoading.value = false
            }
        }
    }

    fun remove(
        group: Group,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                groupDao.delete(group)
                onSuccess?.invoke()
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Erro ao remover grupo")
            } finally {
                _isActionLoading.value = false
            }
        }
    }
}