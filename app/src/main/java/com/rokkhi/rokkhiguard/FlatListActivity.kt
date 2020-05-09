package com.rokkhi.rokkhiguard

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rokkhi.rokkhiguard.Model.ActiveFlats
import kotlinx.android.synthetic.main.activity_flat_list.*
import kotlinx.android.synthetic.main.activity_guard_list.*

class FlatListActivity : AppCompatActivity() {
    lateinit var buildid: String
    lateinit var context: Context
    lateinit var firestore: FirebaseFirestore
    lateinit var activeFlatsList:ArrayList<ActiveFlats>
    lateinit var activeFlatCoronaAdapter: ActiveFlatCoronaAdapter
    lateinit var search:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flat_list)

        context = this@FlatListActivity

        search=""

        activeFlatsList= ArrayList();
        firestore= FirebaseFirestore.getInstance()

        flatListRecyclerView.layoutManager = GridLayoutManager(this@FlatListActivity, 5)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        buildid = sharedPref.getString("buildid", "none")


        firestore.collection("activeflats")
                .whereEqualTo("build_id",buildid)
//                .whereEqualTo("f_array",search)
                .orderBy("f_no",Query.Direction.DESCENDING)
                .get().addOnSuccessListener { documents->
                    activeFlatsList.clear()
                    for (document:DocumentSnapshot in documents.documents){
                        var activeFlats=document.toObject(ActiveFlats::class.java)
                        activeFlats?.let { activeFlatsList.add(it) }
                    }
                    progressbarCorrona.visibility=View.GONE
                    flatListRecyclerView.visibility=View.VISIBLE
                    activeFlatCoronaAdapter=ActiveFlatCoronaAdapter(activeFlatsList, context as FlatListActivity)
                    flatListRecyclerView.adapter=activeFlatCoronaAdapter
                }




    }
}
