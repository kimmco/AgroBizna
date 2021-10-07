package com.cokimutai.agrobizna.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.cokimutai.agrobizna.R
import com.cokimutai.agrobizna.supports.FarmDetails
import com.google.firebase.database.*

class AdminCattleActivity : AppCompatActivity() {


    private lateinit var databaseRef : DatabaseReference
    private lateinit var cattleaRecyclerView: RecyclerView
    private lateinit var teaArrayList : ArrayList<FarmDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_cattle)

        val teaTypeIntent = intent.getIntExtra("teaType", 0)

        cattleaRecyclerView = findViewById(R.id.cattle_expns_recycler)
        cattleaRecyclerView.setHasFixedSize(true)

        teaArrayList = arrayListOf<FarmDetails>()

        getCattleExpnsDataDb(teaTypeIntent)
    }

    private fun getCattleExpnsDataDb(teaTypeIntent : Int) {
        databaseRef = FirebaseDatabase.getInstance().getReference("items")

        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (snap in snapshot.children){
                        val selectedItem =
                            snap.getValue(FarmDetails::class.java)

                        teaArrayList.add(selectedItem!!)
                    }

                    cattleaRecyclerView.adapter = TeaAdapter(teaArrayList, teaTypeIntent)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

}