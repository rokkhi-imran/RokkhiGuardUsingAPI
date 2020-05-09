package com.rokkhi.rokkhiguard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.rokkhi.rokkhiguard.Model.ActiveFlats
import com.rokkhi.rokkhiguard.Model.FlatTrackCorona
import kotlinx.android.synthetic.main.item_flat.view.*
import java.util.*
import kotlin.collections.ArrayList

class ActiveFlatCoronaAdapter(var activeFlatsList: ArrayList<ActiveFlats>, var context: FlatListActivity) : RecyclerView.Adapter<ActiveFlatCoronaAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_flat, parent, false)
        return ViewHolderClass(view)
    }


    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {

        public fun setData(activeFlat: ActiveFlats) {

            itemView.flatnoTV.text = "${activeFlat.f_no}"

            itemView.setOnClickListener {
                showDialog(itemView.context, activeFlat)
            }
        }

        private fun showDialog(context: Context?, activeFlat: ActiveFlats) {

            val builder = AlertDialog.Builder(context!!, R.style.AlertDialog)
            //set title for alert dialog
            builder.setTitle("এই ফ্লাটের লোক কি বাইরে যাচ্ছে ?")
            builder.setCancelable(false)

            //set message for alert dialog
            builder.setMessage(" যদি বাইরে যাই তাহলে হ্যাঁ বাটুন চাপুন ,অন্যথায় বাতিল বাটুন চাপুন ।")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("হ্যাঁ") { dialogInterface, which ->


                var firestore: FirebaseFirestore = FirebaseFirestore.getInstance();
                var autoID = firestore.collection("flatTrackCorona").document().id
                var flatTrackCorona = FlatTrackCorona(autoID, activeFlat.flat_id, "Out", Date(), Date())

                firestore.collection("flatTrackCorona")
                        .document(autoID)
                        .set(flatTrackCorona)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

            }
          /*  //performing cancel action
            builder.setNeutralButton(" বাতিল") { dialogInterface, which ->


            }*/
             //performing negative action
             builder.setNegativeButton("বাতিল"){dialogInterface, which ->

             }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()

        }

    }

    override fun getItemCount(): Int {

        return activeFlatsList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

        return holder.setData(activeFlatsList[position])
    }

}