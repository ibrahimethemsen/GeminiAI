package com.ibrahimethemsen.geminiai.image

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.ai.client.generativeai.type.content
import com.ibrahimethemsen.geminiai.databinding.FragmentImageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class ImageFragment : Fragment() {
    private var _binding: FragmentImageBinding? = null
    private val binding: FragmentImageBinding get() = _binding!!
    private val viewModel by viewModels<ImageViewModel>()
    private var pickBitmap = mutableListOf<Bitmap?>()
    private var fullResponse : String = ""
    private val imageAdapter = ImageAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendPrompt()
        observer()
        binding.imageRv.adapter = imageAdapter
        binding.imageBtn.setOnClickListener {
            pickPhoto()
            binding.imagePromptResponse.text = ""
        }
    }

    private fun sendPrompt() {
        binding.imageSendPrompt.setOnClickListener {
            fullResponse = ""
            binding.imagePromptResponse.text = ""
            CoroutineScope(Dispatchers.IO).launch {
                val inputContent = content {
                    pickBitmap.forEach{
                        it?.let {
                            image(compressBitmap(it))
                        }
                    }
                    text(binding.imagePromptTextEt.text.toString())
                }
                viewModel.geminiPromptResponse(inputContent)
            }
            binding.imagePromptProgress.visibility = View.VISIBLE
        }
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, true)
        val byteArrayOutputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        return BitmapFactory.decodeByteArray(
            byteArrayOutputStream.toByteArray(),
            0,
            byteArrayOutputStream.size()
        )
    }
    private fun observer() {
        viewModel.promptResponse.observe(viewLifecycleOwner) {
            binding.imagePromptProgress.visibility = View.GONE
            fullResponse += it.text
            binding.imagePromptResponse.text = fullResponse
        }
    }

    private fun pickPhoto() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            galleryLauncher.launch("image/*")
            galleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            galleryIntent()
            galleryLauncher.launch("image/*")
        }
    }

    private fun galleryIntent() {
        startActivity(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }

    @Suppress("DEPRECATION")
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent(),
    ) { uri ->
        uri?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
            }.also { bitmap ->
                pickBitmap.add(bitmap)
                imageAdapter.setImageList(pickBitmap)
            }
        }
    }
    private val galleryPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            galleryIntent()
        }
    }
}