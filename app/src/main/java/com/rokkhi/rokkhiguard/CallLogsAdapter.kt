package com.rokkhi.rokkhiguard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rokkhi.rokkhiguard.Model.CallLogClass
import kotlinx.android.synthetic.main.item_view_call_logs.view.*


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

            itemView.callNumber.text=callLogClass.mobileNumberReceiver
            itemView.callTime.text="Time : "+callLogClass.callStart.toString()
            if (callLogClass.isReceived){

                itemView.callStatus.text="Status : Received"
            }else{
                itemView.callStatus.text="Status : Cancel"

            }

        }

    }
}