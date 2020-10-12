package com.rokkhi.rokkhiguard.Model.api

data class NoticeModelClass(
    val data: List<NoticeData>,
    val errors: List<String>,
    val status: String,
    val statusCode: Int
)

data class NoticeData(
    val addedBy: String,
    val body: String,
    val createdDate: String,
    val date: String,
    val id: Int,
    val noticeFor: String,
    val title: String,
    val updatedDate: String
)

data class SWorkerModelClass(
        val `data`: List<SworkerData>,
        val errors: List<Any>,
        val status: String,
        val statusCode: Int
)

data class SworkerData(
    val address: String,
    val age: Int,
    val createdDate: String,
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
    val updatedDate: String
)


data class ParcelResponseModelClass(
    val data: ParcelData,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class ParcelData(
    val address: String,
    val building: Int,
    val community: Int,
    val company: String,
    val contact: String,
    val createdDate: String,
    val email: String,
    val exitTime: Any,
    val flat: Int,
    val id: Int,
    val image: String,
    val inTime: String,
    val name: String,
    val purpose: String,
    val receivedBy: Int,
    val status: String,
    val thumbImage: String,
    val type: String,
    val updatedDate: String
)

data class ActiveFlatsModelClass(
    val data: List<ActiveFlatData>,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class ActiveFlatData(
    val contact: String,
    val contactInfo: String,
    val contactPerson: String,
    val createdDate: String,
    val description: String,
    val id: Int,
    val isRented: Boolean,
    val isVacant: Boolean,
    val name: String,
    val number: String,
    val size: Int,
    val totalBalcony: Int,
    val totalRoom: Int,
    val totalWashRoom: Int,
    val updatedDate: String
)


data class ChildModelClass(
    val `data`: List<ChildData>,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class ChildData(
    val address: String,
    val age: Int,
    val createdDate: String,
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
    val updatedDate: String
)

data class RegisterUserModelClass(
    val data: RegisterUserData,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class RegisterUserData(
    val address: String,
    val age: Int,
    val building: Int,
    val community: Int,
    val createdDate: String,
    val email: String,
    val firebaseId: String,
    val flat: Int,
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
    val userRoles: List<UserRole>
)

data class UserRole(
    val code: String,
    val createdDate: String,
    val description: String,
    val id: Int,
    val name: String,
    val updatedDate: String
)

data class GetVisitorInsideModelClass(
    val `data`: List<GetInsideVisitorData>,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class GetInsideVisitorData(
    val address: String,
    val company: String,
    val contact: String,
    val createdDate: String,
    val email: String,
    val exitTime: Any,
    val id: Int,
    val image: String,
    val inTime: String,
    val name: String,
    val purpose: String,
    val status: String,
    val thumbImage: String,
    val type: String,
    val updatedDate: String
)

data class VisitorOutModelClass(
    val data: VisitorOutData,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class VisitorOutData(
    val address: String,
    val company: String,
    val contact: String,
    val createdDate: String,
    val email: String,
    val exitTime: String,
    val id: Int,
    val image: String,
    val inTime: String,
    val name: String,
    val purpose: String,
    val status: String,
    val thumbImage: String,
    val type: String,
    val updatedDate: String
)

data class VehicleListModelClass(
    val `data`: List<VehicleData>,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class VehicleData(
    val color: String,
    val createdDate: String,
    val id: Int,
    val image: String,
    val model: String,
    val name: String,
    val number: String,
    val registrationNumber: String,
    val taxTokenNumber: String,
    val thumbImage: String,
    val type: String,
    val updatedDate: String
)

data class RecordVehicleEntryModleClass(
    val `data`:  RecordVehicleEntryData,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class  RecordVehicleEntryData(
    val address: String,
    val associatedVehicle: Int,
    val building: Int,
    val community: Int,
    val company: String,
    val contact: String,
    val createdDate: String,
    val email: String,
    val exitTime: Any,
    val flat: Int,
    val id: Int,
    val image: String,
    val inTime: String,
    val name: String,
    val purpose: String,
    val receivedBy: Int,
    val status: String,
    val thumbImage: String,
    val type: String,
    val updatedDate: String
)


data class UserDetailsModelClass(
    val data: UserDetailsData,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class UserDetailsData(
    val address: String,
    val age: Int,
    val buildingId: Int,
    val buildingName: String,
    val communityId: Int,
    val communityName: String,
    val email: String,
    val firebaseId: String,
    val flatId: Int,
    val flatName: String,
    val gender: String,
    val image: String,
    val isActive: Boolean,
    val name: String,
    val nid: String,
    val organization: String,
    val password: String,
    val phone: String,
    val primaryRoleCode: String,
    val thumbImage: String,
    val userDevices: List<Any>,
    val userFunctions: List<Any>,
    val userId: Int,
    val userRoles: List<UserDetailsRoleData>
)

data class UserDetailsRoleData(
    val code: String,
    val createdDate: String,
    val description: String,
    val id: Int,
    val name: String,
    val updatedDate: String
)

data class GuardListModelClass(
    val `data`: List<GuardListData>,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class GuardListData(
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
    val updatedDate: String
)

data class GetUserByPhoneNumberModelClass(
    val `data`: GetUserByPhoneData,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class GetUserByPhoneData(
    val address: String,
    val age: Int,
    val building: Building,
    val community: CommunityX,
    val contactPersonName: String,
    val contactPersonPhone: String,
    val createdDate: String,
    val deletedDate: Any,
    val email: String,
    val firebaseId: String,
    val flat: Any,
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
    val updatedDate: String
)

data class Building(
    val address: String,
    val community: Community,
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

data class CommunityX(
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
    val type: String,
    val updatedDate: String
)

data class Community(
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
    val type: String,
    val updatedDate: String
)

data class AllFlatsModelClass(
    val `data`: List<Data>,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class Data(
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
    val size: Int,
    val totalBalcony: Int,
    val totalRoom: Int,
    val totalWashRoom: Int,
    val updatedDate: String
)