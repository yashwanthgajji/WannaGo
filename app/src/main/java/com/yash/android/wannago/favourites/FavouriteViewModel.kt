package com.yash.android.wannago.favourites

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yash.android.wannago.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

private const val TAG = "FavouriteViewModel"
private const val COLLECTION_NAME = "Favourites"
class FavouriteViewModel: ViewModel() {
    private val firebaseDb = Firebase.firestore

    fun deleteFavouriteLocation(swipedLocation: Location): Boolean {
        var successfullyDeleted = false
        firebaseDb.collection(COLLECTION_NAME)
            .whereEqualTo("id", swipedLocation.id.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference
                        .delete()
                        .addOnSuccessListener {
                            successfullyDeleted = true
                        }
                        .addOnFailureListener {
                            successfullyDeleted = false
                        }
                }
            }
            .addOnFailureListener {
                successfullyDeleted = false
            }
        return successfullyDeleted
    }

    fun addFavouriteLocation(location: Location): Boolean {
        val loc = hashMapOf(
            "id" to location.id.toString(),
            "latitude" to location.latLng.latitude,
            "longitude" to location.latLng.longitude
        )
        var successfullyAdded = false
        firebaseDb.collection(COLLECTION_NAME)
            .add(loc)
            .addOnSuccessListener {
                successfullyAdded = true
            }
            .addOnFailureListener {
                successfullyAdded = false
            }
        return successfullyAdded
    }

    fun getAllFavouriteLocations(): StateFlow<List<Location>> {
        val favouritesListFlow = MutableStateFlow(emptyList<Location>())
        firebaseDb.collection(COLLECTION_NAME)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    favouritesListFlow.value = snapshot.documents.map { doc ->
                        Location(
                            UUID.fromString(doc["id"] as String),
                            LatLng(doc["latitude"] as Double, doc["longitude"] as Double),
                            "",
                            ""
                        )
                    }
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
        return favouritesListFlow
    }
}