package com.ibrahimethemsen.geminiai.chat

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.ibrahimethemsen.geminiai.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var _binding : FragmentChatBinding? = null
    private val binding : FragmentChatBinding get() = _binding!!
    private val viewModel by viewModels<ChatViewModel>()
    private val chatAdapter = ChatAdapter()
    private val messageList = mutableListOf<Pair<String, Int>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        sendMessage()
        observe()
    }
    private fun setAdapter(){
        binding.chatRv.adapter = chatAdapter
    }

    private fun sendMessage(){
        binding.chatSend.setOnClickListener {
            val userMessage = binding.chatPromptTextEt.text.toString()
            viewModel.geminiChat(userMessage)
            messageList.add(Pair(userMessage,ChatAdapter.VIEW_TYPE_USER))
            chatAdapter.setMessages(messageList)
            scrollPosition()
            binding.chatPromptTextEt.setText("")
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun observe(){
        viewModel.messageResponse.observe(viewLifecycleOwner){content ->
            content.text?.let {
                messageList.add(Pair(it,ChatAdapter.VIEW_TYPE_GEMINI))
                chatAdapter.setMessages(messageList)
                scrollPosition()
            }
        }
    }

    private fun scrollPosition(){
        binding.chatRv.smoothScrollToPosition(chatAdapter.itemCount - 1)

    }
}