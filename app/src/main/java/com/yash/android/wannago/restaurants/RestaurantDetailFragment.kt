package com.yash.android.wannago.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.yash.android.wannago.Location
import com.yash.android.wannago.databinding.FragmentRestaurantDetailBinding
import kotlinx.coroutines.launch


private const val DEFAULT_ZOOM = 15f
class RestaurantDetailFragment: Fragment(), OnMapReadyCallback {
    private var _binding: FragmentRestaurantDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Unable to access binding. Is view created"
        }
    private val args: RestaurantDetailFragmentArgs by navArgs()
    private val restaurantDetailViewModel: RestaurantDetailViewModel by viewModels {
        RestaurantDetailViewModelFactory(args.locationId)
    }
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                restaurantDetailViewModel.location.collect { location ->
                    location?.let { updateUi(it) }
                }
            }
        }
        binding.restaurantDetailMapview.onCreate(savedInstanceState)
        binding.restaurantDetailMapview.getMapAsync(this)
    }

    private fun updateUi(location: Location) {
        binding.apply {
            val latitudeText = "Latitude: ${location.latLng.latitude}"
            restaurantDetailLatitude.text = latitudeText
            val longitudeText = "Longitude: ${location.latLng.longitude}"
            restaurantDetailLongitude.text = longitudeText
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.uiSettings.isZoomControlsEnabled = true
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                restaurantDetailViewModel.location.collect { location ->
                    location?.let {
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(it.latLng, DEFAULT_ZOOM)
                        )
                        map.addMarker(MarkerOptions().position(it.latLng))
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}