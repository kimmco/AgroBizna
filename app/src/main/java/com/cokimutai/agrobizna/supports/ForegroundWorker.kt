package com.cokimutai.agrobizna.supports

import android.app.NotificationManager
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


class ForegroundWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {


    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val myContext = context

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        return@withContext runCatching {
            runTask()

            Result.success()
        }.getOrElse {
            Result.failure()
        }

    }

    private suspend fun runTask() {
        mFirebaseDatabase = FirebaseDatabase.getInstance()

        val fetchedSharedPrefTippedTotal = SavedPreference.getTippedTotal(myContext)
        val fetchedcapturedTippingWeight = SavedPreference.getDayTippedWeight(myContext)

        val fetchedSharedPrefPluckedTotal = SavedPreference
                .getPluckedTotal(myContext)
        val fetchedcapturedWeight = SavedPreference
                .getRecentTeaWeight(myContext)

        updatePluckTotal(fetchedSharedPrefPluckedTotal, fetchedcapturedWeight!!,
                fetchedSharedPrefTippedTotal, fetchedcapturedTippingWeight)

        getTheSavedTotals()

    }

    private fun getTheSavedTotals() {
        val databaseQuery = FirebaseDatabase.getInstance().getReference("totals").limitToLast(1)

        databaseQuery.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (snap in snapshot.children){
                        val selectedItem =
                            snap.getValue(FarmDetailsCumulatives::class.java)

                        val pluckingAddedUp = selectedItem?.pluckngAmntCumulative
                        val tippingAddedUp = selectedItem?.tippingAmntCumulative
                        val teaExpensesCumulated = selectedItem?.pluckngAmntCumulative
                        val receivedAmntCumulated = selectedItem?.receivedAmntCumulative

                        pluckingAddedUp?.let { Log.d("SEE", it) }

                         //SavedPreference.setTipping(myContext, tippingAddedUp!!)
                         tippingAddedUp?.let { SavedPreference.setTipping(myContext, it) }
                         pluckingAddedUp?.let { SavedPreference.setPlucking(myContext, it) }
                         teaExpensesCumulated?.let { SavedPreference.setTeaExpnsTotal(myContext, it) }
                         receivedAmntCumulated?.let { SavedPreference.setTotalMoney(myContext, it) }

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")

            }
        })

    }

    private fun updatePluckTotal(fetchedPluckedTotal: String?, capturedTeaWeight: String,
                                 fetchedTippedTotal: String?, fetchedTippingWeight: String?) {

        var newPluckTotal: Float? = 0.0f
       // var savingPluckNode = SavedPreference.getPluckAccumNode(myContext)
        if (fetchedTippingWeight == "") {
            val floatFetched: Float? = fetchedPluckedTotal?.toFloat()
            val floatCaptured: Float = capturedTeaWeight.toFloat()
            newPluckTotal = (floatFetched?.plus(floatCaptured))
            savingPluckNode = SavedPreference.getPluckAccumNode(myContext)!!+"/pluckngAmntCumulative"

        } else if (capturedTeaWeight == "") {
        savingPluckNode = SavedPreference.getTippingAccumNode(myContext)!! + "/tippingAmntCumulative"
        val floatFetched: Float? = fetchedTippedTotal?.toFloat()
        val floatCaptured: Float? = fetchedTippingWeight?.toFloat()
        newPluckTotal = (floatFetched?.plus(floatCaptured!!))
    }
        val teaDbRef = mFirebaseDatabase?.reference?.child("totals")
        val farmDetailsCumulatives = FarmDetailsCumulatives()

        val newPluckTotalMap : HashMap<String, Any> = HashMap()
        //var saveNode = "MlememTYeMBBeTotals"


        newPluckTotalMap.put(savingPluckNode, newPluckTotal.toString())

        teaDbRef?.updateChildren(newPluckTotalMap)
                ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful) {
                            //  measure_edtx.setText(" ")
                            Toast.makeText(myContext, "Weight Totals submitted successfully!",
                                    Toast.LENGTH_SHORT).show()

                            Log.d("NODE", savingPluckNode.toString())

                        } else {
                            Toast.makeText(myContext,
                                    "Failed to Submit!, try again \u2661 ",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }


    companion object {
        private lateinit var databaseRef : DatabaseReference
        private var mFirebaseDatabase: FirebaseDatabase? = null
        private lateinit var savingPluckNode : String
    }

}