package com.wheeler.game.ui.dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Random

class ChanceViewModel: ViewModel() {
    private val _isFirstPage = MutableLiveData(true)
    val isFirstPage: LiveData<Boolean> = _isFirstPage

    private val _color = MutableLiveData<Boolean?>(null)
    val color: LiveData<Boolean?> = _color

    private val _isWin = MutableLiveData<Boolean?>(null)
    val isWin: LiveData<Boolean?> = _isWin

    var endCallback: ((Boolean) -> Unit)? = null

    fun changePage() {
        _isFirstPage.value = false
    }

    fun clickColor(value: Boolean) {
        viewModelScope.launch {
            _color.postValue(Random().nextBoolean())
            delay(10)
            if (_color.value!! == value) {
                _isWin.postValue(true)
                endCallback?.invoke(true)
            } else {
                _isWin.postValue(false)
                endCallback?.invoke(false)
            }
        }
    }
}