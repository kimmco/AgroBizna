package com.cokimutai.agrobizna.ui.cattle

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cokimutai.agrobizna.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_cattle.*
import java.text.DateFormat

class CattleFragment : Fragment() {

    private lateinit var dashboardViewModel: CattleViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(CattleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cattle, container, false)

        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()

        cattle_expns_descrp_edtx.setOnClickListener {
            val title = "Cattle Expenses"
            val costWord = cattle_expns_descrp_edtx.hint
            context?.let { it1 -> showUniversalMenu(it1, title, costWord, cattle_expns_descrp_edtx ) }
        }
        catttle_expens_cost_edtx.setOnClickListener {
            val title = "Cattle Expenses"
            val costWord = catttle_expens_cost_edtx.hint
            context?.let { it1 ->
                showUniversalMenu(it1, title, costWord, catttle_expens_cost_edtx ) }
        }
        save_cattle_btn.setOnClickListener {
            saveCattleExpsToDb()
        }
    }

    fun showUniversalMenu(mContext: Context, title: CharSequence, words: CharSequence?, editText: EditText){
        val inflater = LayoutInflater.from(mContext)
        val teaCostView : View = inflater.inflate(R.layout.tea_size_dialog, null)
        val sizeEdtxt: EditText = teaCostView.findViewById(R.id.size_picker) as EditText
        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        //    sizeEdtxt.focusable = View.FOCUSABLE
        // }
        if (title == "Others"){
            if (words == "Total Cost"){
                sizeEdtxt.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
        val dialog = AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(words)
                .setView(teaCostView)
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    editText.setText(sizeEdtxt.text)
                })
                .setNegativeButton("Cancel", null)
                .create()
        dialog.show()
    }
    fun saveCattleExpsToDb(){

        val capturedCattleExpDetails = cattle_expns_descrp_edtx.text.toString()
        val capturedCattleExpCost = catttle_expens_cost_edtx.text.toString()

        val capturedCattleExpMap: HashMap<String, Any> = HashMap()

        val currentTime = System.currentTimeMillis()
        val sdf = DateFormat.getDateInstance(DateFormat.MEDIUM)
                .format(currentTime)

        capturedCattleExpMap.put("cattleDate", sdf)
        capturedCattleExpMap.put("cattleExpensDescription", capturedCattleExpDetails)
        capturedCattleExpMap.put("cattleExpensCost", capturedCattleExpCost)

        val teaDbRef = mFirebaseDatabase?.reference?.child(KEY_FARM_ITEM)
        //?.child(uid)

        teaDbRef?.push()?.updateChildren(capturedCattleExpMap)
                ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful){
                            cattle_expns_descrp_edtx.setText(" ")
                            catttle_expens_cost_edtx.setText(" ")
                            Toast.makeText( context,"Cattle Records submitted successfully!",
                                    Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(context,
                                    "Failed to Submit!, try again \u2661 ",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }


    companion object {
        private var mAuth: FirebaseAuth? = null
        private var mFirebaseDatabase: FirebaseDatabase? = null
        private const val KEY_FARM_ITEM = "items"
    }

}