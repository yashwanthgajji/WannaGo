package com.yash.android.wannago.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yash.android.wannago.Location
import com.yash.android.wannago.databinding.LocationItemLayoutBinding
import java.util.UUID

class FavouriteHolder(private val binding: LocationItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(location: Location, onFavouriteClicked: (locationId: UUID) -> Unit) {
        binding.apply {
            root.setOnClickListener {
                onFavouriteClicked(location.id)
            }
            val latitudeText = "Latitude: ${location.latLng.latitude}"
            itemLatitude.text = latitudeText
            val longitudeText = "Longitude: ${location.latLng.longitude}"
            itemLongitude.text = longitudeText
        }
    }
}

class FavouriteAdapter(
    private val favourites: List<Location>,
    private val onFavouriteClicked: (locationId: UUID) -> Unit
): RecyclerView.Adapter<FavouriteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LocationItemLayoutBinding.inflate(layoutInflater, parent, false)
        return FavouriteHolder(binding)
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    override fun onBindViewHolder(holder: FavouriteHolder, position: Int) {
        holder.bind(favourites[position], onFavouriteClicked)
    }

    fun getItem(position: Int): Location {
        return favourites[position]
    }
}