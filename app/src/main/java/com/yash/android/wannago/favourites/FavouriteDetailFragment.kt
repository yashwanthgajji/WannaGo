package com.yash.android.wannago.favourites

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
import com.yash.android.wannago.databinding.FragmentFavouriteDetailBinding
import kotlinx.coroutines.launch


private const val DEFAULT_ZOOM = 15f
class FavouriteDetailFragment: Fragment(), OnMapReadyCallback {
    private var _binding: FragmentFavouriteDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Unable to access binding. Is view created"
        }
    private val args: FavouriteDetailFragmentArgs by navArgs()
    private val favouriteDetailViewModel: FavouriteDetailViewModel by viewModels {
        FavouriteDetailViewModelFactory(args.locationId)
    }
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favouriteDetailViewModel.location.collect { location ->
                    location?.let { updateUi(it) }
                }
            }
        }
        binding.favouriteDetailMapview.onCreate(savedInstanceState)
        binding.favouriteDetailMapview.getMapAsync(this)
    }

    private fun updateUi(location: Location) {
        binding.apply {
            val latitudeText = "Latitude: ${location.latLng.latitude}"
            favouriteDetailLatitude.text = latitudeText
            val longitudeText = "Longitude: ${location.latLng.longitude}"
            favouriteDetailLongitude.text = longitudeText
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.uiSettings.isZoomControlsEnabled = true
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favouriteDetailViewModel.location.collect { location ->
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