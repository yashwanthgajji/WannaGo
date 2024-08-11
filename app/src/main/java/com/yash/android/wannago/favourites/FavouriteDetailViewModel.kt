package com.yash.android.wannago.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yash.android.wannago.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID


private const val TAG = "FavouriteDetailViewModel"
private const val COLLECTION_NAME = "Favourites"
class FavouriteDetailViewModel(locationId: UUID): ViewModel() {
    private var _locationStateFlow: MutableStateFlow<Location?> = MutableStateFlow(null)
    val location: StateFlow<Location?>
        get() = _locationStateFlow.asStateFlow()

    private val firebaseDb = Firebase.firestore

    init {
        viewModelScope.launch {
            firebaseDb.collection(COLLECTION_NAME)
                .whereEqualTo("id", locationId.toString())
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null) {
                        _locationStateFlow.value = Location(
                            UUID.fromString(snapshot.documents[0]["id"] as String),
                            LatLng(
                                snapshot.documents[0]["latitude"] as Double,
                                snapshot.documents[0]["longitude"] as Double
                            ),
                            "",
                            ""
                        )
                    }
                }
        }
    }
}

class FavouriteDetailViewModelFactory(private val locationId: UUID) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavouriteDetailViewModel(locationId) as T
    }
}