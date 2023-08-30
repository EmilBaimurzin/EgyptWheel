package com.wheeler.game.ui.egypt_wheel

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wheeler.game.R
import com.wheeler.game.core.library.random
import com.wheeler.game.core.library.shortToast
import com.wheeler.game.databinding.FragmentEgyptWheelBinding
import com.wheeler.game.ui.other.ViewBindingFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentEgyptWheel :
    ViewBindingFragment<FragmentEgyptWheelBinding>(FragmentEgyptWheelBinding::inflate) {
    private val viewModel: EgyptWheelViewModel by viewModels()
    private val callbackViewModel: CallbackForDialog by activityViewModels()
    private val sp by lazy {
        requireActivity().getSharedPreferences("SP", Context.MODE_PRIVATE)
    }
    private var lastTime: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lastTime = sp.getLong("LAST", 0)
        setText()

        callbackViewModel.callback = { clickedYes, isWin ->
            if (clickedYes) {
                if (isWin) {
                    setBalance(1000)
                    shortToast(requireContext(), "You won 1000 crystals!")
                    setText()
                } else {
                    shortToast(requireContext(), "Better luck next time")
                }
            } else {
                setBalance(100)
                setText()
                shortToast(requireContext(), "Money was returned")
            }
        }

        binding.plus.setOnClickListener {
            if (checkLastUpdateTime()) {
                setBalance(100 random 500)
                sp.edit().putLong("LAST", System.currentTimeMillis()).apply()
                lastTime = System.currentTimeMillis()
                setText()
            } else {
                shortToast(requireContext(), "get your reward later")
            }
        }

        binding.menu.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.spin.setOnClickListener {
            if (getBalance() >= viewModel.price.value!!) {
                viewModel.spin()
                setBalance(-viewModel.price.value!!)
                if (viewModel.price.value!! == 0) {
                    viewModel.setPrice(100)
                }
                setText()
            }
        }

        viewModel.price.observe(viewLifecycleOwner) {
            binding.price.text = it.toString()
        }

        viewModel.spinCallback = {
            if (it != 777) {
                lifecycleScope.launch {
                    setBalance(it)
                    setText()
                    binding.plusText.text = "+ $it"
                    binding.plusText.isVisible = true
                    delay(500)
                    binding.plusText.isVisible = false
                }
            } else {
                findNavController().navigate(R.id.action_fragmentEgyptWheel_to_dialogChance)
            }
        }
        viewModel.isSpinning.observe(viewLifecycleOwner) {
            binding.spin.isEnabled = !it
        }

        viewModel.rotation.observe(viewLifecycleOwner) {
            binding.wheel.rotation = -it + 100
        }

        viewModel.timer.observe(viewLifecycleOwner) {
            var totalSecs = (7_200_000 - (System.currentTimeMillis() - lastTime))
            if (totalSecs < 0) {
                binding.timer.text = "Take your reward"
            } else {
                totalSecs /= 1000
                val hours = totalSecs / 3600;
                val minutes = (totalSecs % 3600) / 60;
                val seconds = totalSecs % 60;
                binding.timer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            }
        }
    }

    private fun getBalance(): Int = sp.getInt("BALANCE", 1500)
    private fun setBalance(value: Int) = sp.edit().putInt("BALANCE", getBalance() + value).apply()

    private fun setText() {
        binding.balance.text = getBalance().toString()
    }

    private fun checkLastUpdateTime(): Boolean {
        return System.currentTimeMillis() - sp.getLong("LAST", 0) >= 7_200_000
    }
}