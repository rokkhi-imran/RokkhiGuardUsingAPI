package com.rokkhi.rokkhiguard.Model.api
/*

data class AssignRoleToUserServiceWorkerResponse(
        val `data`: List<DataServiceWorkerResponse>,
        val errors: List<Any>,
        val status: String,
        val statusCode: Int
)

data class DataServiceWorkerResponse(
        val _value: ValueServiceWorkerResponse,
        val isFailure: Boolean,
        val isSuccess: Boolean
)

data class ValueServiceWorkerResponse(
        val building: Building,
        val community: Community,
        val createdBy: CreatedByServiceWorkerResponse,
        val createdDate: String,
        val deletedDate: Any,
        val flat: FlatServiceWorkerResponse,
        val id: Int,
        val updatedBy: UpdatedByServiceWorkerResponse,
        val updatedDate: String,
        val user: UserServiceWorkerResponse,
        val userRoleCode: String,
        val validity: String
)

data class BuildingServiceWorkerResponse(
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
        val updatedDate: String,
        val validity: String
)

data class CommunityServiceWorkerResponse(
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
        val subscribePackages: SubscribePackagesServiceWorkerResponse,
        val type: String,
        val updatedDate: String,
        val validity: String
)

data class CreatedByServiceWorkerResponse(
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
        val primaryRoleCode: String,
        val thumbImage: String,
        val updatedDate: String,
        val validity: String
)

data class FlatServiceWorkerResponse(
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

data class UpdatedByServiceWorkerResponse(
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
        val primaryRoleCode: String,
        val thumbImage: String,
        val updatedDate: String,
        val validity: String
)

data class UserServiceWorkerResponse(
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
        val primaryRoleCode: String,
        val thumbImage: String,
        val updatedDate: String,
        val validity: String
)

class SubscribePackagesServiceWorkerResponse(
)*/

data class AssignRoleToUserServiceWorkerResponse(
    val `data`: List<List<DataServiceWorkerResponse>>,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class DataServiceWorkerResponse(
    val building: Int,
    val community: Int,
    val createdDate: String,
    val deletedDate: Any,
    val flat: Int,
    val id: Int,
    val updatedBy: Int,
    val updatedDate: String,
    val user: Int,
    val userRole: AssignRoleToUserServiceWorkerUserRole
)

data class AssignRoleToUserServiceWorkerUserRole(
    val code: String,
    val createdDate: String,
    val deletedDate: Any,
    val description: String,
    val group: String,
    val id: Int,
    val name: String,
    val updatedDate: String
)
