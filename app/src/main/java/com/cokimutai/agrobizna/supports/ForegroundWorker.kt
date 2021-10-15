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


class ForegroundWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {


    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val myContext = context

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        setForeground(createForegroundInfo())

        return@withContext runCatching {
             runTask()

            Result.success()
        }.getOrElse {
            Result.failure()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(id: String, channelName: String) {
        notificationManager.createNotificationChannel(
            NotificationChannel(id, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        )
    }

    //Creates notifications for service
    private fun createForegroundInfo(): ForegroundInfo {
        val id = "1225"
        val channelName = "Downloads Notification"
        val title = "Downloading"
        val cancel = "Cancel"
        val body = "Long running task is running"

        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(id, channelName)
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        return ForegroundInfo(1, notification)
    }

    private suspend fun runTask() {
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        val fetchedSharedPrefPluckedTotal = SavedPreference
                .getPluckedTotal(myContext)
        val fetchedcapturedWeightTotal = SavedPreference
                .getRecentTeaWeight(myContext)
        getTheSavedTotals()
        updatePluckTotal(fetchedSharedPrefPluckedTotal, fetchedcapturedWeightTotal!!)

    }

    private fun getTheSavedTotals() {
        databaseRef = FirebaseDatabase.getInstance().getReference("totals")

        databaseRef.addValueEventListener(object : ValueEventListener{

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
                        pluckingAddedUp?.let { SavedPreference.setPlucking(myContext, it) }
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

    private fun updatePluckTotal(fetchedPluckedTotal: String?, capturedTeaWeight: String) {
        val floatFetched : Float? = fetchedPluckedTotal?.toFloat()
        val floatCaptured : Float = capturedTeaWeight.toFloat()
        val newPluckTotal = (floatFetched?.plus(floatCaptured))

        val teaDbRef = mFirebaseDatabase?.reference?.child("totals")
        val farmDetailsCumulatives = FarmDetailsCumulatives()

        var newPluckTotalMap : HashMap<String, Any> = HashMap()
        newPluckTotalMap.put("MlememTYeMBBeTotals/pluckngAmntCumulative", newPluckTotal.toString())

        teaDbRef?.updateChildren(newPluckTotalMap)
                ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful) {
                            //  measure_edtx.setText(" ")
                            Toast.makeText(myContext, "Weight Totals submitted successfully!",
                                    Toast.LENGTH_SHORT).show()

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