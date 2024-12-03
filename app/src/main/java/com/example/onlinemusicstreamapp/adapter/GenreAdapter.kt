package com.example.onlinemusicstreamapp.adapter
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlinemusicstreamapp.R
import com.example.onlinemusicstreamapp.database.data.entities.Genre
import com.example.onlinemusicstreamapp.databinding.CardviewGenreBinding
import com.example.onlinemusicstreamapp.ui.fragments.search.SearchFragment
import com.example.onlinemusicstreamapp.ui.fragments.search.SearchFragmentDirections

class GenreAdapter: RecyclerView.Adapter<GenreAdapter.MyViewHolder>() {

    private lateinit var genre: List<Genre>

    class MyViewHolder(
        private val binding: CardviewGenreBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bindData(genre: Genre) {

            binding.titleGenre.text = genre.name

            Glide.with(binding.imgGenre).load(genre.genreImg).into(binding.imgGenre)

            val parseColor = Color.parseColor(genre.color)
            binding.layoutCardView.setBackgroundColor(parseColor)


        }
        fun genreListener(genre: Genre) {
            binding.layoutCardView.setOnClickListener {
                val action = SearchFragmentDirections.actionSearchFragmentToGenreDetailFragment(genre)
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardviewGenreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = genre[position]
        holder.bindData(currentItem)
        holder.genreListener(currentItem)
        Log.d("CurrentGenreItem", "$currentItem")
    }

    override fun getItemCount(): Int {
        return genre.size
    }

    fun updateData(newGenre: List<Genre>) {
        genre = newGenre
        notifyDataSetChanged()
    }

}