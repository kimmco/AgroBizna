package com.cokimutai.agrobizna.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.recyclerview.widget.RecyclerView
import com.cokimutai.agrobizna.R
import com.cokimutai.agrobizna.supports.FarmDetails
import com.google.firebase.database.*

class AdminTeaRecords : AppCompatActivity() {


   private lateinit var databaseRef : DatabaseReference
   private lateinit var teaRecyclerView: RecyclerView
   private lateinit var teaArrayList : ArrayList<FarmDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_tea_records)

        val teaTypeIntent = intent.getIntExtra("teaType", 0)

        teaRecyclerView = findViewById(R.id.tea_list_recycler)
        teaRecyclerView.setHasFixedSize(false)

        teaArrayList = arrayListOf<FarmDetails>()

        getTeaPluckingDataDb(teaTypeIntent)


    }

    private fun getTeaPluckingDataDb(teaTypeIntent : Int) {
        databaseRef = FirebaseDatabase.getInstance().getReference("items")

        databaseRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (snap in snapshot.children){
                        val selectedTeaItem =
                                snap.getValue(FarmDetails::class.java)

                         teaArrayList.add(selectedTeaItem!!)
                    }

                    teaRecyclerView.adapter = TeaAdapter(teaArrayList, teaTypeIntent)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }


}