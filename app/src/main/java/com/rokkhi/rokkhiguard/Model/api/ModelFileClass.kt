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