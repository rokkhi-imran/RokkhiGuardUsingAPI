package com.rokkhi.rokkhiguard.Model

import kotlinx.android.parcel.Parcelize
import java.util.*


data class FlatTrackCorona(var auto_id: String = "",
                           var flat_id: String = "",
                           var status: String = "",
                           var created_at: Date = Date(),
                           var updated_at: Date = Date()
                            )