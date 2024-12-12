package com.example.onlinemusicstreamapp.ui.fragments.bottomsheet.library

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.other.Constants.BOTTOM_SHEET_DIALOG
import com.example.onlinemusicstreamapp.databinding.FragmentLibraryBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LibraryBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentLibraryBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBottomSheetBinding.inflate(inflater, container, false)

        binding.createPlaylist.setOnClickListener {
            dismiss()

            val bottomSheetFragment = BottomSheetNamePlaylist()
            bottomSheetFragment.show(parentFragmentManager, BOTTOM_SHEET_DIALOG)
        }
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }


}