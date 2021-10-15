package com.cokimutai.agrobizna

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.cokimutai.agrobizna.supports.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat

class MainActivity : AppCompatActivity() {


    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,
            R.id.navigation_others))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        firebaseAuth= FirebaseAuth.getInstance()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            showUniversalMenu(this, "Money Received", "Money received/from any sales")

        }
    }

    fun showUniversalMenu(mContext: Context, title: CharSequence, words: CharSequence?){
        val inflater = LayoutInflater.from(mContext)
        val teaCostView : View = inflater.inflate(R.layout.tea_size_dialog, null)

        val sizeEdtxt: EditText = teaCostView.findViewById(R.id.size_picker) as EditText
        sizeEdtxt.inputType = InputType.TYPE_CLASS_NUMBER

        val dialog = AlertDialog.Builder(mContext)
            .setTitle(title)
            .setMessage(words)
            .setView(teaCostView)
            .setPositiveButton(getString(R.string.ok_dialog_txt), DialogInterface.OnClickListener { dialog, which ->


                val value =  sizeEdtxt.getText().toString().trim()

                Log.d("MONIS", value)
                val isEmptyContent =  value.isEmpty()

                if (value.isNullOrEmpty()){

                    val toast = Toast.makeText(this,
                            getString(R.string.empty_view_msg), Toast.LENGTH_LONG)
                    val view = toast.view
                    view?.background?.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
                    val txt = view?.findViewById<TextView>(android.R.id.message)
                    txt?.setTextColor(Color.WHITE)
                    toast.show()

                   // return@OnClickListener
                }else {

                    showSureDialog(mContext, title, words, value)
                }
            })
            .setNegativeButton(getString(R.string.cancel_dialog_txt), null)
            .create()
        dialog.show()
    }

    fun showSureDialog(mContext: Context, title: CharSequence, words: CharSequence?, value: String) {
        val inflater = LayoutInflater.from(mContext)
        val teaCostView : View = inflater.inflate(R.layout.tea_size_dialog, null)

        val sizeEdtxt: EditText = teaCostView.findViewById(R.id.size_picker) as EditText
         sDialog = AlertDialog.Builder(this)
                .setTitle("Sure")
                .setMessage("Are you sure the amount is correct?")
               // .setView(teaCostView)
                .setPositiveButton(getString(R.string.ok_dialog_txt), DialogInterface.OnClickListener { dialog, which ->

                    cummutaleAmountReceived(value)

                })
                .setNegativeButton(getString(R.string.cancel_dialog_txt), DialogInterface.OnClickListener{dialog, which ->
                    sizeEdtxt.setText(value)
                    showUniversalMenu(mContext, title, words)
                })
                .create()
        sDialog.show()
    }

    private fun cummutaleAmountReceived(theAmount: String) {

        SavedPreference.setRecentMoney(this, theAmount)

        val toast = Toast.makeText(this,
                "adding amount to database", Toast.LENGTH_LONG)
        val view = toast.view
        view?.background?.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
        val txt = view?.findViewById<TextView>(android.R.id.message)
        txt?.setTextColor(Color.WHITE)
        toast.show()

        WorkManager.getInstance(this)
                .beginUniqueWork("AmountTotalWorker", ExistingWorkPolicy.APPEND_OR_REPLACE,
                        OneTimeWorkRequest.from(AmountTotalWorker::class.java)).enqueue().state
                .observe(this) { state ->
                    Log.d("MONY", "AmountTotalWorker: $state")
                }
    }

    /*
    fun addMoneyReceivedToDb(pesa: String){
        farmDetailsMap.put(farmDetails.receivedAmnt!!, pesa)
        farmDetailsMap.put(farmDetails.dateReceivedAmnt!!, sdf)

        amountRecAccumulatorMap.put(farmDetails.receivedAmntCumulative!!, sdf)

        teaDbRef?.push()?.updateChildren(farmDetailsMap)
                ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful) {
                         //   measure_edtx.setText(" ")
                            Toast.makeText(this@MainActivity, "Amount savedd successfully!",
                                    Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(this@MainActivity,
                                    "Failed to Submit!, try again \u2661 ",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                })

      /*  teaDbRef?.updateChildren(amountRecAccumulatorMap)
                ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful) {

                        } else {
                            Toast.makeText(this@MainActivity,
                                    "Didn't sum up Totals, please report",
                                    Toast.LENGTH_LONG).show()
                        }
                    }
                })

        */

    }


    override fun onOkClick() {

        addMoneyReceivedToDb(" 22")
        Log.d("TAG", "Tag")
    }
    */

    companion object {
        val farmDetails: FarmDetails = FarmDetails()
        val currentTime = System.currentTimeMillis()
        val sdf = DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentTime)
        val farmDetailsMap: HashMap<String, Any> = HashMap()
        val amountRecAccumulatorMap: HashMap<String, Any> = HashMap()
        private var mFirebaseDatabase: FirebaseDatabase? = null
        private const val KEY_FARM_ITEM = "items"
        private lateinit var  sDialog: AlertDialog

        val teaDbRef = mFirebaseDatabase?.reference?.child(KEY_FARM_ITEM)

    }

}