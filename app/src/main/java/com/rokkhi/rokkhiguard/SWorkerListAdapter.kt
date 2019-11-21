package com.rokkhi.rokkhiguard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rokkhi.rokkhiguard.Model.SLastHistory
import com.rokkhi.rokkhiguard.Model.Swroker
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader
import kotlinx.android.synthetic.main.sworker_item_view.view.*

class SWorkerListAdapter(var swrokerList: MutableList<Swroker>,var sLastHistoryList: MutableList<SLastHistory>, var context: SWorkersActivity) : RecyclerView.Adapter<SWorkerListAdapter.ViewHolderClass>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SWorkerListAdapter.ViewHolderClass {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.sworker_item_view, parent, false)
        return ViewHolderClass(view)
    }

    override fun getItemCount(): Int {

        return 10
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

        return holder.setData(swrokerList[position],sLastHistoryList[position])
    }


    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(swroker: Swroker, sLastHistory: SLastHistory) {

            UniversalImageLoader.setImage(swroker.s_pic, itemView.picImage, null, "")
            itemView.name.text = swroker.s_name
            itemView.flats.text = sLastHistory.flatsNo.toString();


        }

    }
}