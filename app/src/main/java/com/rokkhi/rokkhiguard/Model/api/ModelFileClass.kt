package com.rokkhi.rokkhiguard.Model.api

data class NoticeModelClass(
    val `data`: List<Data>,
    val errors: List<Any>,
    val status: String,
    val statusCode: Int
)

data class Data(
    val addedBy: Any,
    val body: String,
    val createdDate: String,
    val date: String,
    val id: Int,
    val noticeFor: String,
    val title: String,
    val updatedDate: String
)