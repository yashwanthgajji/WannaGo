package com.yash.android.wannago

import com.google.android.gms.maps.model.LatLng
import java.util.UUID

data class Location(val id: UUID, val latLng: LatLng, val name: String, val address: String)