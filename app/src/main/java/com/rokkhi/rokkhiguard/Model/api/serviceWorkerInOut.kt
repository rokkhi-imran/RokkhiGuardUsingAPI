package com.rokkhi.rokkhiguard.Model.api

data class ServiceWorkerInOutModel(
    val acknowledgedBy: Int,
    val buildingId: Int,
    val communityId: Int,
    val flatId: List<Int>,
    val guardId: Int,
    val limit: String,
    val pageId: String,
    val serviceWorkerId: Int,
    val timeZone: String,
    val requesterFlatId: Int,
    val requesterBuildingId: Int,
    val requesterCommunityId: Int,
    val requesterUserRole: Int,

)

