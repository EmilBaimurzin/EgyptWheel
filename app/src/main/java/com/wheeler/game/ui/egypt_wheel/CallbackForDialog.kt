package com.wheeler.game.ui.egypt_wheel

import androidx.lifecycle.ViewModel

class CallbackForDialog: ViewModel() {
    var callback: ((clickedYes: Boolean, isWin: Boolean) -> Unit)? = null
}