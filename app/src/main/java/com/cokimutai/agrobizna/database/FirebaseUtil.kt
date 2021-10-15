package com.cokimutai.agrobizna.database

import com.cokimutai.agrobizna.admin.TeaAdapter
import com.cokimutai.agrobizna.supports.FarmDetails
import com.cokimutai.agrobizna.supports.FarmDetailsCumulatives
import com.cokimutai.agrobizna.supports.SavedPreference
import com.google.firebase.database.*






class FirebaseUtil(private val database: FirebaseDatabase) : FirebaseDatabaseInterface{

    private fun getTeaPluckingDataDb(teaTypeIntent : Int) {

        database.reference
                .child(KEY_FARM_ITEM)
                .addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (snap in snapshot.children){
                        val selectedItem =
                                snap.getValue(FarmDetailsCumulatives::class.java)
                        val tippingAddedUp = selectedItem?.tippingAmntCumulative
                        val pluckingAddedUp = selectedItem?.pluckngAmntCumulative
                        val teaExpensesCumulated = selectedItem?.teaExpensesCumulative
                        val receivedAmntCumulated = selectedItem?.receivedAmntCumulative

                       // SavedPreference.setTipping(context = get)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    override fun getPluckingAmntCm(onResult: (FarmDetailsCumulatives) -> Unit) {
        database.reference
                .child(KEY_FARM_ITEM)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            for (snap in snapshot.children){
                                val selectedTeaItem =
                                        snap.getValue(FarmDetails::class.java)
                            }
                        }
                        val selectedTeaItem =
                                snapshot.getValue(FarmDetails::class.java)
                        selectedTeaItem?.run {
                            onResult(FarmDetailsCumulatives())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })

    }

    override fun getTippingAmntCm(onResult: (FarmDetailsCumulatives) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getReceivedAmntCm(onResult: (FarmDetailsCumulatives) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getTeaExpensesAmntCm(onResult: (FarmDetailsCumulatives) -> Unit) {
        TODO("Not yet implemented")
    }

    companion object {

        private const val KEY_FARM_ITEM = "items"
        private lateinit var databaseRef : DatabaseReference
    }
}