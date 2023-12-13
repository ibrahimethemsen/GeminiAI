package com.ibrahimethemsen.geminiai.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.ibrahimethemsen.geminiai.di.GeminiProVision
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    @GeminiProVision private val geminiProVision : GenerativeModel
) : ViewModel() {
    private val _promptResponse = MutableLiveData<GenerateContentResponse>()
    val promptResponse : LiveData<GenerateContentResponse> get() = _promptResponse

    fun geminiPromptResponse(
        inputContent : Content
    ){
        viewModelScope.launch {
            geminiProVision.generateContentStream(inputContent).collect { response ->
                _promptResponse.value = response
            }
        }
    }
}