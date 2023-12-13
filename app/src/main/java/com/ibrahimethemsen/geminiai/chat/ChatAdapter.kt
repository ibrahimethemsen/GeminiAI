package com.ibrahimethemsen.geminiai.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ibrahimethemsen.geminiai.R
import com.ibrahimethemsen.geminiai.databinding.AdapterGeminiBinding
import com.ibrahimethemsen.geminiai.databinding.AdapterUserBinding

class ChatAdapter : RecyclerView.Adapter<ViewHolder>() {
    private val messages = mutableListOf<Pair<String, Int>>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMessages(messages: List<Pair<String, Int>>) {
        this.messages.apply {
            clear()
            addAll(messages)
        }
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: AdapterUserBinding) :
        ViewHolder(binding.root) {
        fun bind(message: Pair<String, Int>) {
            binding.adapterUserTv.text = message.first
            binding.adapterUserCard.setBackgroundResource(R.drawable.card_radius_user)
            binding.adapterUserCard.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.user_background))

        }
    }

    inner class GeminiViewHolder(private val binding: AdapterGeminiBinding) :
        ViewHolder(binding.root) {
        fun bind(message: Pair<String, Int>) {
            binding.adapterGeminiTv.text = message.first

            binding.adapterGeminiCard.setBackgroundResource(R.drawable.card_radius_gemini)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val binding =
                    AdapterUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UserViewHolder(binding)
            }

            VIEW_TYPE_GEMINI -> {
                val binding =
                    AdapterGeminiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GeminiViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        when (holder.itemViewType) {
            VIEW_TYPE_USER -> {
                val userViewHolder = holder as UserViewHolder
                userViewHolder.bind(message)
            }

            VIEW_TYPE_GEMINI -> {
                val geminiViewHolder = holder as GeminiViewHolder
                geminiViewHolder.bind(message)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return messages[position].second
    }

    companion object {
         const val VIEW_TYPE_USER = 1
         const val VIEW_TYPE_GEMINI = 2
    }
}