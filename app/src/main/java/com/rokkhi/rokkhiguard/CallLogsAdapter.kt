package com.rokkhi.rokkhiguard

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rokkhi.rokkhiguard.Model.CallLogClass
import kotlinx.android.synthetic.main.item_view_call_logs.view.*
import java.text.SimpleDateFormat


class CallLogsAdapter(var callLogClassList: MutableList<CallLogClass>) : RecyclerView.Adapter<CallLogsAdapter.ViewHolderClass>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_call_logs, parent, false)
        return ViewHolderClass(view)
    }

    override fun getItemCount(): Int {

        return callLogClassList.size;
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

        return holder.setData(callLogClassList[position])
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(callLogClass: CallLogClass) {

            itemView.flatNumber.text = callLogClass.flatName

            val formatter = SimpleDateFormat("hh:mm:ss a")

            val strTime: String = formatter.format(callLogClass.callStart)
            val endTime: String = formatter.format(callLogClass.callStart)

//get diffrence between two date
            val diff: Long = callLogClass.callEnd.getTime() - callLogClass.callStart.getTime()
            val diffSeconds = diff / 1000 % 60
            Log.e("difference = second  = ", "" + diffSeconds)
            var minute = diffSeconds / 60
            var seconds = diffSeconds % 60

            itemView.callTime.text = strTime


//            itemView.callTime.text="Time : "+callLogClass.callStart.toString()


            if (callLogClass.isReceived) {
                itemView.callStatus.text = "Received"
                if (seconds < 9) {
                    itemView.duration.text = minute.toString() + ":0" + seconds.toString()
                } else {

                    itemView.duration.text = minute.toString() + ":" + seconds.toString()
                }

            } else {
                itemView.callStatus.text = "Cancel"
                itemView.duration.text = "0:00"


            }

        }

    }
}