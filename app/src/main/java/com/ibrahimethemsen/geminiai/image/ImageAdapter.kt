package com.ibrahimethemsen.geminiai.image

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibrahimethemsen.geminiai.databinding.AdapterImageBinding

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private val imageList = mutableListOf<Bitmap?>()

    @SuppressLint("NotifyDataSetChanged")
    fun setImageList(imageList : List<Bitmap?>){
        this.imageList.apply {
            clear()
            addAll(imageList)
        }
        notifyDataSetChanged()
    }

    class ImageViewHolder(private val binding : AdapterImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bitmap : Bitmap?){
            binding.adapterImage.setImageBitmap(bitmap)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = AdapterImageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position])
    }
}