package com.cokimutai.agrobizna.ui.others

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cokimutai.agrobizna.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_others.*
import java.text.DateFormat
import java.text.SimpleDateFormat


class OthersFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_others, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()

        others_expenses_cost_edtx.setOnClickListener {
            val title = "Other Expenses"
            val costWord = others_expenses_cost_edtx.hint
            context?.let { it1 -> showUniversalMenu(it1, title, costWord, others_expenses_cost_edtx ) }
        }
        others_measure_edtx.setOnClickListener {
            val title = "Other Expenses"
            val costWord = others_measure_edtx.hint
            context?.let { it1 -> showUniversalMenu(it1, title, costWord, others_measure_edtx ) }
        }
        save_others_expns_btn.setOnClickListener {
            saveOtherExpsToDb()
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

    fun saveOtherExpsToDb(){
        val capturedOthersExpDetails = others_measure_edtx.text.toString()
        val capturedOthersExpSize = others_expenses_cost_edtx.text.toString()
        val capturedOthersExpMap: HashMap<String, Any> = HashMap()


        val uid = mAuth?.currentUser!!.uid

        val currentTime = System.currentTimeMillis()
        val sdf = DateFormat.getDateInstance(DateFormat.MEDIUM)
                .format(currentTime)

        val teaDbRef = mFirebaseDatabase?.reference?.child(KEY_FARM_ITEM)

        capturedOthersExpMap.put("genExpnsDate", sdf)
        capturedOthersExpMap.put("othersGeneralExpsDescription", capturedOthersExpDetails)
        capturedOthersExpMap.put("othersGeneralExpsCost", capturedOthersExpSize)

        teaDbRef?.push()?.updateChildren(capturedOthersExpMap)
                ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful){
                            others_measure_edtx.setText(" ")
                            Toast.makeText( context,"Expense Records submitted successfully!",
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