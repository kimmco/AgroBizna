package com.cokimutai.agrobizna.supports

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AmountTotalWorker(context: Context, parameters: WorkerParameters) :
        CoroutineWorker(context, parameters) {

    val myContext = context

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
       // setForeground(createForegroundInfo())

        return@withContext runCatching {

            runTask()

            Result.success()
        }.getOrElse {
            Result.failure()
        }

    }

    private suspend fun runTask() {
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        getTheSavedTotals()
        val moneyReceived = SavedPreference.getRecentReceivedAmount(myContext)
        updateMoneyRecvdTotal(moneyReceived!!)

    }

    private fun updateMoneyRecvdTotal(moneyReceived: String) {


        val teaDbRef = mFirebaseDatabase?.reference?.child("totals")
        val farmDetailsCumulatives = FarmDetailsCumulatives()

        val dbMoneyTotal = SavedPreference.getTotalReceivedAmount(myContext)

        val floatFetched : Float? = moneyReceived.toFloat()
        val floatCaptured : Float? = dbMoneyTotal?.toFloat()

        val updatedMoneyTotal = floatFetched!! + floatCaptured!!



        var newPluckTotalMap : HashMap<String, Any> = HashMap()
        //newPluckTotalMap.put("MlemeeMOneyTotals/receivedAmntCumulative", moneyReceived)
        newPluckTotalMap.put("MlemeeMOneyTotals/receivedAmntCumulative", updatedMoneyTotal.toString())

        teaDbRef?.updateChildren(newPluckTotalMap)
                ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful) {
                            //  measure_edtx.setText(" ")
                            Toast.makeText(myContext, "Money accumulated successfully!",
                                    Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(myContext,
                                    "Failed to Submit!, try again \u2661 ",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }

    private fun getTheSavedTotals() {
        databaseRef = FirebaseDatabase.getInstance().getReference("totals")

        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (snap in snapshot.children){
                        val selectedItem =
                                snap.getValue(FarmDetailsCumulatives::class.java)

                        val pluckingAddedUp = selectedItem?.pluckngAmntCumulative
                        val tippingAddedUp = selectedItem?.tippingAmntCumulative
                        val teaExpensesCumulated = selectedItem?.pluckngAmntCumulative
                        val receivedAmntCumulated = selectedItem?.receivedAmntCumulative

                        //SavedPreference.setTipping(myContext, tippingAddedUp!!)
                       // pluckingAddedUp?.let { SavedPreference.setPlucking(myContext, it) }
                        //  SavedPreference.setTeaExpnsTotal(myContext, teaExpensesCumulated!!)
                        receivedAmntCumulated?.let { SavedPreference.setTotalMoney(myContext, it) }

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }


    companion object {
        private lateinit var databaseRef : DatabaseReference
        private var mFirebaseDatabase: FirebaseDatabase? = null
    }

}