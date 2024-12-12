package com.example.onlinemusicstreamapp.ui.fragments.bottomsheet

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.data.entities.UserPlaylist
import com.example.onlinemusicstreamapp.databinding.FragmentEditPlaylistBottomSheetDialogBinding
import com.example.onlinemusicstreamapp.ui.viewmodel.UserPlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPlaylistBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEditPlaylistBottomSheetDialogBinding

    private val mUserPlaylistViewModel: UserPlaylistViewModel by viewModels()

    private val args by navArgs<EditPlaylistBottomSheetDialogArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPlaylistBottomSheetDialogBinding.inflate(layoutInflater)

        binding.namePlaylist.setText(args.userPlaylist.title)
        binding.description.setText(args.userPlaylist.description)

        binding.namePlaylist.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                binding.save.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.save.setOnClickListener {
                    updatePlaylist()
                }
            }
        })

        binding.description.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                binding.save.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.save.setOnClickListener {
                    updatePlaylist()
                }
            }
        })

        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root

    }

    private fun updatePlaylist() {
        val title = binding.namePlaylist.text.toString()
        val desc = binding.description.text.toString()

        val updatedUserPlaylist = UserPlaylist(args.userPlaylist.id, title, desc)
        mUserPlaylistViewModel.updatePlaylist(updatedUserPlaylist)
        findNavController().popBackStack()

    }

}