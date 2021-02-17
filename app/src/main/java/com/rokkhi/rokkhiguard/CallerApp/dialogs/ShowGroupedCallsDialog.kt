package com.rokkhi.rokkhiguard.CallerApp.dialogs

import androidx.appcompat.app.AlertDialog
import com.rokkhi.rokkhiguard.R
import com.simplemobiletools.commons.activities.BaseSimpleActivity
import com.simplemobiletools.commons.extensions.setupDialogStuff
import com.rokkhi.rokkhiguard.CallerApp.activities.SimpleActivity
import com.rokkhi.rokkhiguard.CallerApp.adapters.RecentCallsAdapter
import com.simplemobiletools.dialer.helpers.RecentsHelper
import com.simplemobiletools.dialer.models.RecentCall
import kotlinx.android.synthetic.main.dialog_show_grouped_calls.view.*
import java.util.*

class ShowGroupedCallsDialog(val activity: BaseSimpleActivity, callIds: ArrayList<Int>) {
    private var dialog: AlertDialog? = null
    private var view = activity.layoutInflater.inflate(R.layout.dialog_show_grouped_calls, null)

    init {
        view.apply {
            RecentsHelper(activity).getRecentCalls(false) { allRecents ->
                val recents = allRecents.filter { callIds.contains(it.id) }.toMutableList() as ArrayList<RecentCall>
                activity.runOnUiThread {
                    RecentCallsAdapter(activity as SimpleActivity, recents, select_grouped_calls_list, null) {

                    }.apply {
                        select_grouped_calls_list.adapter = this
                    }
                }
            }
        }

        dialog = AlertDialog.Builder(activity)
            .create().apply {
                activity.setupDialogStuff(view, this)
            }
    }
}
