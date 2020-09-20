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