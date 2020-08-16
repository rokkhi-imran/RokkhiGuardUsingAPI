package com.rokkhi.rokkhiguard.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


data class FlatTrackCorona(var auto_id: String = "",
                           var flat_id: String = "",
                           var status: String = "",
                           var created_at: Date = Date(),
                           var updated_at: Date = Date()
                            )


data class BuildingServiceWorkers(
        var auto_id: String?="",
        var build_id: String?="",
        var comm_id: String?="",
        var s_id: String?="",
        var worked_id: String?="",  /// provided by building
        var worker_type: String?="", /////// BuildingRegularWorker, FlatRegularWorker, OneTimeServiceWorker, ExpiredWorkerFromCommunity, ExpiredWorkerFromBuilding, BannedWorkerFromBuilding, BannedWorkerFromCommunity
        var s_array: List<String>? = null,
        var totalVisited: Int?=0,   ///// number
        var added_by: String?="",
        var added_at: Date?= Date(),
        var approved_by: String?="",
        var Approved_at: Date?= Date(),
        var removed_by: String?="",
        var removed_at: Date?=Date(),
        var created_at: Date?= Date(),
        var updated_at: Date?=Date()

)


data class FlatServiceWorkers(

        var auto_id: String? = "",
        var f_no: String? = "",
        var flat_id: String? = "",
        var build_id: String? = "",
        var comm_id: String? = "",
        var s_id: String? = "",
        var worker_type: String? = "",  /////// BuildingRegularWorker, FlatRegularWorker, OneTimeServiceWorker, ExpiredWorkerFromCommunity, ExpiredWorkerFromBuilding, ExpiredWorkerFromFlat, BannedWorkerFromBuilding, BannedWorkerFromCommunity
        var s_array: List<String>? = null,
        var monthlySalary: Int? = -0,
        var daily_work_amount: Int? = 0,  ///// 8/3/1
        var daily_work_unit: String? = "",      ///// hour/unit/subject
        var work_start_time: Date? = Date(),
        val work_end_time: Date? = Date(),
        var totalVisited: Int? = 0,   ///// number
        var added_by: String? = "",
        var added_at: Date? = Date(),
        var removed_by: String? = "",
        val removed_at: Date? = Date(),
        val created_at: Date? = Date(),
        var updated_at: Date? = Date(),
        var s_phone: String? = ""

)