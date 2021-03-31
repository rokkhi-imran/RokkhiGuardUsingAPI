package com.rokkhi.rokkhiguard.Model.api

data class UserDetailsModelClass(
    val `data`: UserDetailsData,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class UserDetailsData(
    val address: String,
    val age: Int,
    val email: String,
    val firebaseId: String,
    val gender: String,
    val image: String,
    val isActive: Boolean,
    val jwtToken: String,
    val name: String,
    val nid: String,
    val organization: String,
    val password: String,
    val phone: String,
    val thumbImage: String,
    val updateInfo: UpdateInfo,
    val userDevices: List<Any>,
    val userFunctions: List<Any>,
    val userId: Int,
    val userRoles: List<UserDetailsRoleData>
)

data class UpdateInfo(
    val createdDate: String,
    val currentVersionAndroid: String,
    val currentVersioniOS: String,
    val deletedDate: Any,
    val forceUpdateAndroid: Boolean,
    val forceUpdateiOS: Boolean,
    val id: Int,
    val previousVersionAndroid: String,
    val previousVersioniOS: String,
    val updateAlertAndroid: Boolean,
    val updateAlertiOS: Boolean,
    val updateMessageAndroid: String,
    val updateMessageiOS: String,
    val updatedDate: String
)

data class UserDetailsRoleData(
    val buildingId: Int,
    val buildingName: String,
    val communityId: Int,
    val communityName: String,
    val joiningDate: String,
    val userRole: Int,
    val userRoleCode: String,
    val userRoleName: String
)

/*
data class UserDetailsModelClass(
    val `data`: UserDetailsData,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class UserDetailsData(
    val address: String,
    val age: Int,
    val email: String,
    val firebaseId: String,
    val gender: String,
    val image: String,
    val isActive: Boolean,
    val jwtToken: String,
    val name: String,
    val nid: String,
    val organization: String,
    val password: String,
    val phone: String,
    val thumbImage: String,
    val updateInfo: UpdateInfo,
    val userDevices: List<Any>,
    val userFunctions: List<Any>,
    val userId: Int,
    val userRoles: List<UserDetailsRoleData>
)

data class UpdateInfo(
    val createdDate: String,
    val currentVersionAndroid: String,
    val currentVersioniOS: String,
    val deletedDate: Any,
    val forceUpdateAndroid: Boolean,
    val forceUpdateiOS: Boolean,
    val id: Int,
    val previousVersionAndroid: String,
    val previousVersioniOS: String,
    val updateAlertAndroid: Boolean,
    val updateAlertiOS: Boolean,
    val updateMessageAndroid: String,
    val updateMessageiOS: String,
    val updatedDate: String
)

data class UserDetailsRoleData(
    val buildingId: Int,
    val buildingName: String,
    val communityId: Int,
    val communityName: String,
    val joiningDate: String,
    val userRole: Int,
    val userRoleName: String
)*/

