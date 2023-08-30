package com.wheeler.game.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wheeler.game.R
import com.wheeler.game.core.library.ViewBindingDialog
import com.wheeler.game.databinding.DialogChanceBinding
import com.wheeler.game.ui.egypt_wheel.CallbackForDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DialogChance: ViewBindingDialog<DialogChanceBinding>(DialogChanceBinding::inflate) {
    private val viewModel: ChanceViewModel by viewModels()
    private val callbackViewModel: CallbackForDialog by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Dialog_No_Border)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setCancelable(false)
        dialog!!.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                findNavController().popBackStack()
                true
            } else {
                false
            }
        }

        binding.yes.setOnClickListener {
            viewModel.changePage()
        }

        binding.no.setOnClickListener {
            callbackViewModel.callback?.invoke(false, false)
            findNavController().popBackStack()
        }

        viewModel.endCallback = {
            lifecycleScope.launch (Dispatchers.Main) {
                delay(1000)
                callbackViewModel.callback?.invoke(true, it)
                findNavController().popBackStack()
            }
        }

        binding.green.setOnClickListener {
            if (viewModel.color.value == null) {
                viewModel.clickColor(true)
            }
        }

        binding.red.setOnClickListener {
            if (viewModel.color.value == null) {
                viewModel.clickColor(false)
            }
        }

        viewModel.isFirstPage.observe(viewLifecycleOwner) {
            binding.apply {
                page1.isVisible = it
                page2.isVisible = !it
            }
        }

        viewModel.color.observe(viewLifecycleOwner) {
            when (it) {
                true -> binding.colorImage.setImageResource(R.drawable.color03)
                false -> binding.colorImage.setImageResource(R.drawable.color02)
                null -> binding.colorImage.setImageResource(R.drawable.color_unknown)
            }
        }

        viewModel.isWin.observe(viewLifecycleOwner) {
            when (it) {
                true -> binding.textView2.text = "Nice!"
                false -> binding.textView2.text = "Next time"
                null -> binding.textView2.text = "guess the color!"
            }
        }
    }
}