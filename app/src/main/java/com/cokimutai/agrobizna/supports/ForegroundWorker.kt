package com.cokimutai.agrobizna.supports

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.cokimutai.agrobizna.R
import com.cokimutai.agrobizna.ui.tea.TeaFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.DateFormat


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
        val fetchedSharedPrefPluckedTotal = SavedPreference
                .getPluckedTotal(myContext)
        val fetchedcapturedWeightTotal = SavedPreference
                .getRecentTeaWeight(myContext)

        updatePluckTotal(fetchedSharedPrefPluckedTotal, fetchedcapturedWeightTotal!!)

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

                         SavedPreference.setTipping(myContext, tippingAddedUp!!)
                         pluckingAddedUp?.let { SavedPreference.setPlucking(myContext, it) }
                         SavedPreference.setTeaExpnsTotal(myContext, teaExpensesCumulated!!)
                         receivedAmntCumulated?.let { SavedPreference.setTotalMoney(myContext, it) }

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")

            }
        })

    }

    private fun updatePluckTotal(fetchedPluckedTotal: String?, capturedTeaWeight: String) {
        val floatFetched : Float? = fetchedPluckedTotal?.toFloat()
        val floatCaptured : Float = capturedTeaWeight.toFloat()
        val newPluckTotal = (floatFetched?.plus(floatCaptured))

        val teaDbRef = mFirebaseDatabase?.reference?.child("totals")
        val farmDetailsCumulatives = FarmDetailsCumulatives()

        val newPluckTotalMap : HashMap<String, Any> = HashMap()
        //var saveNode = "MlememTYeMBBeTotals"

        val savingPluckNode = SavedPreference.getPluckAccumNode(myContext)


        newPluckTotalMap.put("$savingPluckNode/pluckngAmntCumulative", newPluckTotal.toString())

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
    }

}