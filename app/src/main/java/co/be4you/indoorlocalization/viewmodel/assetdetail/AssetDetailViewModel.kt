package co.be4you.indoorlocalization.viewmodel.assetdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.be4you.core.data.repositories.AssetRepository
import co.be4you.indoorlocalization.R
import co.be4you.indoorlocalization.viewmodel.main.MainAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AssetDetailViewModel(
    private val repository: AssetRepository
): ViewModel() {

    private val _state = MutableStateFlow(AssetDetailState()) //Ask why you need this
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<MainAction>()
    val event = _event.asSharedFlow()

    fun execute(action: AssetDetailAction){
        when(action){
            is AssetDetailAction.Load -> loadAsset(action.id)
        }
    }

    private fun loadAsset(id: Long){
        _state.update { it.copy(isLoading = true, errorResource = null) }

        viewModelScope.launch(Dispatchers.IO){
            repository.getAsset(id).fold(
                onSuccess = { asset ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            asset = asset
                        )
                    }
                },
                onFailure = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorResource = R.string.generic_error_message
                        )
                    }
                }
            )
        }
    }
}