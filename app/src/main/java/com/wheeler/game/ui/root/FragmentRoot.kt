package com.wheeler.game.ui.root

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wheeler.game.databinding.FragmentRootBinding
import com.wheeler.game.ui.other.ViewBindingFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentRoot : ViewBindingFragment<FragmentRootBinding>(FragmentRootBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            play.setOnClickListener {
                findNavController().navigate(FragmentRootDirections.actionFragmentMainToFragmentEgyptWheel())
            }
            exit.setOnClickListener {
                requireActivity().finish()
            }
        }

        binding.privacyText.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    ACTION_VIEW,
                    Uri.parse("https://www.google.com")
                )
            )
        }
    }
}