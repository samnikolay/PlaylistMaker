package com.samnic.playlistmaker


import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    private val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
    private val trackDetails: TextView = itemView.findViewById(R.id.trackDetails)

    fun bind(model: Track) {
        trackTitle.text = model.trackName
        model.trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        trackDetails.text = "${model.artistName} • ${model.trackTime}"

        val radius=dpToPx(2f,itemView.context)

        Glide
            .with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.track_placeholder)
            .transforms(CenterCrop(), RoundedCorners(radius))
            .into(trackImage)
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}