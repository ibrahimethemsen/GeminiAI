package com.ibrahimethemsen.geminiai.text

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.ibrahimethemsen.geminiai.di.GeminiPro
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextViewModel @Inject constructor(
    @GeminiPro private val  geminiPro : GenerativeModel
): ViewModel() {
    private val _promptResponse = MutableLiveData<GenerateContentResponse>()
    val promptResponse : LiveData<GenerateContentResponse> get() = _promptResponse

    fun geminiTextPrompt(
        prompt : String
    ){
        viewModelScope.launch {
            _promptResponse.value = geminiPro.generateContent(prompt)
        }
    }
}