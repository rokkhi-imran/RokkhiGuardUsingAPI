
package com.rokkhi.rokkhiguard.Model.api

data class ServiceWorkerListModelClass(
        val `data`: List<ServiceWorkerListModelData>,
        val errors: List<Any>,
        val status: String,
        val statusCode: Int
)

data class ServiceWorkerListModelData(
        val address: String,
        val age: Int,
        val contactPersonName: String,
        val contactPersonPhone: String,
        val createdDate: String,
        val deletedDate: Any,
        val email: String,
        val firebaseId: String,
        val gender: String,
        val id: Int,
        val image: String,
        val isActive: Boolean,
        val name: String,
        val nid: String,
        val organization: String,
        val password: String,
        val phone: String,
        val thumbImage: String,
        val updatedDate: String,
        val workPlace: List<ServiceWorkerListModelWorkPlace>
)

data class ServiceWorkerListModelWorkPlace(
        val building: ServiceWorkerListModelBuilding,
        val community: ServiceWorkerListModelCommunity,
        val flat: ServiceWorkerListModelFlat
)

data class ServiceWorkerListModelBuilding(
        val address: String,
        val contactInfo: String,
        val contactPerson: String,
        val createdDate: String,
        val deletedDate: Any,
        val flatFormat: Any,
        val id: Int,
        val isActive: Boolean,
        val latitude: Any,
        val longitude: Any,
        val name: String,
        val totalFlat: Int,
        val totalFloor: Int,
        val totalGate: Int,
        val totalParking: Int,
        val updatedDate: String
)

data class ServiceWorkerListModelCommunity(
        val address: String,
        val contactInfo: String,
        val contactPerson: String,
        val createdDate: String,
        val deletedDate: Any,
        val email: String,
        val firebaseId: String,
        val id: Int,
        val isActive: Boolean,
        val latitude: Any,
        val longitude: Any,
        val name: String,
        val password: String,
        val subscribePackages: SubscribePackages,
        val type: String,
        val updatedDate: String
)

data class ServiceWorkerListModelFlat(
        val contact: String,
        val contactInfo: String,
        val contactPerson: String,
        val createdDate: String,
        val deletedDate: Any,
        val description: String,
        val id: Int,
        val isRented: Boolean,
        val isVacant: Boolean,
        val name: String,
        val number: String,
        val size: Any,
        val totalBalcony: Any,
        val totalRoom: Any,
        val totalWashRoom: Any,
        val updatedDate: String,
        val validity: String
)
class ServiceWorkerListModelSubscribePackages(
)

/*




*/
