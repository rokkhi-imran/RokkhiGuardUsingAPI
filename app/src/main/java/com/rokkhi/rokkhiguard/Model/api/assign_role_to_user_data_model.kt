package com.rokkhi.rokkhiguard.Model.api

data class AssignRoleToUserDataModelPost(
        val timeZone: String,
        val requesterFlatId: Int,
        val requesterBuildingId: Int,
        val requesterCommunityId: Int,
        val requesterUserRole: Int,

        val data: List<DataFlatInfoPost>,
        val limit: String,
        val pageId: String,
        val roleCodes: String,
        val toCommunityId: Int,
        val toUserId: Int
)

data class DataFlatInfoPost(
        val buildingId: Int,
        val flats: List<Int>,
        val roleCodes: String
)


