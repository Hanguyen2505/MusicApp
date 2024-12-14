package com.example.onlinemusicstreamapp.ui.fragments.bottomsheet

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
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

    private lateinit var coverUrl: String

    private val IMAGE_REQUEST_CODE = 100

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
        Glide.with(binding.playlistImg).load(args.userPlaylist.coverUrl).centerCrop().into(binding.playlistImg)

        coverUrl = args.userPlaylist.coverUrl


        binding.namePlaylist.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                binding.save.setTextColor(ContextCompat.getColor(requireContext(), R.color.cyan))

            }
        })

        binding.description.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                binding.save.setTextColor(ContextCompat.getColor(requireContext(), R.color.cyan))
            }
        })

        binding.save.setOnClickListener {
            val title = binding.namePlaylist.text.toString()
            val desc = binding.description.text.toString()
            Log.d("Image user playlist", "$coverUrl")

            updatePlaylist(title, desc, coverUrl)
        }

        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.playlistImg.setOnClickListener {
            pickImageFromGallery()
        }

        return binding.root

    }

    private fun updatePlaylist(title: String, desc: String, coverUrl: String) {

        val updatedUserPlaylist = UserPlaylist(args.userPlaylist.id, title, desc, coverUrl)
        mUserPlaylistViewModel.updatePlaylist(updatedUserPlaylist)
        findNavController().popBackStack()

    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri = data?.data

            Glide.with(binding.playlistImg).load(imageUri).centerCrop().into(binding.playlistImg)
            binding.save.setTextColor(ContextCompat.getColor(requireContext(), R.color.cyan))
            binding.save.setTypeface(null, Typeface.BOLD)
            coverUrl = imageUri.toString()

        }

    }

}