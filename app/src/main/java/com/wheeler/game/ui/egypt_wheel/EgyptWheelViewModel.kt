package com.wheeler.game.ui.egypt_wheel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wheeler.game.core.library.random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class EgyptWheelViewModel : ViewModel() {
    private val _isSpinning = MutableLiveData(false)
    val isSpinning: LiveData<Boolean> = _isSpinning

    private val _rotation = MutableLiveData(0f)
    val rotation: LiveData<Float> = _rotation

    private val _timer = MutableLiveData(true)
    val timer: LiveData<Boolean> = _timer

    private val _price = MutableLiveData(100)
    val price: LiveData<Int> = _price

    var spinCallback: ((Int) -> Unit)? = null

    init {
        viewModelScope.launch (Dispatchers.IO) {
            while (true) {
                delay(1000)
                _timer.postValue(_timer.value)
            }
        }
    }

    fun setPrice(price: Int) {
        _price.postValue(price)
    }

    fun spin() {
        viewModelScope.launch {
            _isSpinning.postValue(true)
            var value = (8..25).random().toFloat()
            val randomDelay = 10 random 13
            val randomDecrease = Random.nextFloat() * (0.044f - 0.067f) + 0.084f
            while (value > 0.10) {
                delay(randomDelay.toLong())
                if (value > 0.10f) {
                    value -= randomDecrease
                }
                if (value - randomDecrease < 0.10f) {
                    val values = listOf(
                        500, 100, 10, 20, 777, 0, 50, 10, 200, 100, 10, 20, 0, 50, 20, 10
                    )
                    val totalSections = values.size
                    val degreesPerSection = 360f / totalSections
                    val normalizedDegrees = (_rotation.value!!) % 360f

                    val sectionIndex = normalizedDegrees / degreesPerSection
                    if (_isSpinning.value!!) {
                        _isSpinning.postValue(false)
                        spinCallback?.invoke(values[sectionIndex.toInt()])
                    }
                }
                _rotation.postValue(_rotation.value!! + value)
            }
        }
    }
}