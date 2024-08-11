package com.yash.android.wannago.restaurants

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yash.android.wannago.Location
import com.yash.android.wannago.databinding.LocationItemLayoutBinding
import java.util.UUID

class RestaurantHolder(private val binding: LocationItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(location: Location, onRestaurantClicked: (locationId: UUID) -> Unit) {
        binding.apply {
            root.setOnClickListener {
                onRestaurantClicked(location.id)
            }
            val latitudeText = "Latitude: ${location.latLng.latitude}"
            itemLatitude.text = latitudeText
            val longitudeText = "Longitude: ${location.latLng.longitude}"
            itemLongitude.text = longitudeText
        }
    }
}

class RestaurantAdapter(
    private val restaurants: List<Location>,
    private val onRestaurantClicked: (locationId: UUID) -> Unit
): RecyclerView.Adapter<RestaurantHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LocationItemLayoutBinding.inflate(layoutInflater, parent, false)
        return RestaurantHolder(binding)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onBindViewHolder(holder: RestaurantHolder, position: Int) {
        holder.bind(restaurants[position], onRestaurantClicked)
    }

    fun getItem(position: Int): Location {
        return restaurants[position]
    }
}