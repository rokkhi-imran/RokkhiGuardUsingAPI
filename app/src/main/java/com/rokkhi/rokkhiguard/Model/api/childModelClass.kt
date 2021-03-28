package com.rokkhi.rokkhiguard.Model.api

data class ChildModelClass(
    val `data`: List<ChildData>,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class ChildData(
    val address: String,
    val age: Int,
    val building: ChildBuilding,
    val community: ChildCommunity,
    val contactPersonName: String,
    val contactPersonPhone: String,
    val createdDate: String,
    val deletedDate: Any,
    val email: String,
    val firebaseId: String,
    val flat: ChildFlat,
    val gender: String,
    val id: Int,
    val image: String,
    val isActive: Boolean,
    val name: String,
    val nid: String,
    val organization: String,
    val password: String,
    val phone: Any,
    val thumbImage: String,
    val updatedDate: String,
    val userDetails: Any
)

data class ChildBuilding(
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

data class ChildCommunity(
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

data class ChildFlat(
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
    val updatedDate: String
)