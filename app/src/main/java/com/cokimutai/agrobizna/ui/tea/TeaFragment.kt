 package com.cokimutai.agrobizna.ui.tea

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.cokimutai.agrobizna.R
import com.cokimutai.agrobizna.R.*
import com.cokimutai.agrobizna.database.FirebaseUtil
import com.cokimutai.agrobizna.supports.FarmDetails
import com.cokimutai.agrobizna.supports.FarmDetailsCumulatives
import com.cokimutai.agrobizna.supports.ForegroundWorker
import com.cokimutai.agrobizna.supports.SavedPreference
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_tea.*
import java.text.DateFormat
import java.text.SimpleDateFormat

import kotlin.collections.HashMap

 class TeaFragment : Fragment() {

    private lateinit var teaViewModel: TeaViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        teaViewModel =
                ViewModelProvider(this).get(TeaViewModel::class.java)
        val root = inflater.inflate(layout.fragment_tea, container, false)

        teaViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()

        val listItemsTxt = (resources.getStringArray(R.array.Tea_Products))

        val spinnez: Spinner = view.findViewById(R.id.tea_spinner) as Spinner
       // var spinnerAdapter: CustomDropDownAdapter = CustomDropDownAdapter(requireContext(), listItemsTxt)
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.view_drop_down_menu, listItemsTxt)


        spinnez.adapter = spinnerAdapter

        val tea_jobs = resources.getStringArray(R.array.Tea_Products)

        spinnez.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                when(tea_jobs[position]){
                    "Select from this list" -> {
                        tea_pluck_img.setImageResource(R.drawable.ic_tea_hallow)
                       // tea_pluck_img.isVisible = true
                        plucking_views.visibility = View.GONE
                        others_cost_edtx.isVisible = false
                        if(!validate()){
                            measure_edtx.setText(" ")
                            cost_edtx.setText(" ")
                            others_cost_edtx.setText(" ")
                        }
                    }

                    getString(string.tea_plucking_txt) -> {
                        tea_pluck_img.setImageResource(R.drawable.ic_pipo_pluck)
                        if (cost_edtx.isShown){
                            cost_edtx.isVisible = false
                            others_cost_edtx.isVisible = false
                        }
                       // tea_pluck_img.isVisible = false
                        measure_edtx.isVisible = true
                        cost_edtx.isVisible = false
                        save_pluck_btn.isVisible = true
                        switch_tipping.isVisible = true
                      //  save_pluck_btn.isVisible = false
                        measure_edtx.hint = getString(string.plucking_hint)
                        others_cost_edtx.isVisible = false
                        titleTxt.text = getString(string.plucking_txt)
                        callTippingSwich()
                        if(!validate()){
                            measure_edtx.setText(" ")
                            cost_edtx.setText(" ")
                            others_cost_edtx.setText(" ")
                        }
                        save_pluck_btn.setOnClickListener {
                            saveTeaPluckingToDb()
                        }

                    }
                    getString(string.tea_weeding_txt) -> {
                       // if(!validate()){
                            measure_edtx.setText(" ")
                            cost_edtx.setText(" ")
                            others_cost_edtx.setText(" ")
                       // }
                        //tea_pluck_img.isVisible = false
                        tea_pluck_img.setImageResource(R.drawable.ic_jembe_tea)
                        plucking_views.visibility = View.VISIBLE
                        switch_tipping.isVisible = false
                        measure_edtx.hint = getString(string.weeding_size_txt)
                        cost_edtx.hint = getString(string.weding_cost_hint_txt)
                        others_cost_edtx.isVisible = false
                        titleTxt.text = getString(string.weeding_txt)
                        save_pluck_btn.setOnClickListener {
                            saveTeaWeedingExpsToDb()
                        }
                    }

                    getString(string.tea_other_expns_txt) -> {
                     //   if(!validate()){
                            measure_edtx.setText(" ")
                            cost_edtx.setText(" ")
                            others_cost_edtx.setText(" ")
                      // } */
                        //tea_pluck_img.isVisible = false
                        tea_pluck_img.setImageResource(R.drawable.ic_tea_spraying)
                        measure_edtx.isVisible = true
                        cost_edtx.isVisible = true
                        switch_tipping.isVisible = false
                        save_pluck_btn.isVisible = true
                        measure_edtx.hint = getString(string.tea_other_exp_details_hint)
                        cost_edtx.hint = getString(string.tea_others_unit_hint)
                        others_cost_edtx.isVisible = true
                        titleTxt.text = getString(string.others_txt)
                        save_pluck_btn.setOnClickListener {
                            val capturedTeaOthersDetails = measure_edtx.text.toString()
                            val capturedTeaOthersSize = cost_edtx.text.toString()
                            val capturedTeaOthersCost = others_cost_edtx.text.toString()
                            if ((capturedTeaOthersDetails.isNullOrEmpty()) ||
                                    (capturedTeaOthersSize.isNullOrEmpty()) ||
                                    (capturedTeaOthersCost.isNullOrEmpty())) {

                                val toast = Toast.makeText(requireContext(),
                                        getString(string.empty_view_msg), Toast.LENGTH_LONG)
                                val view = toast.view
                                view?.background?.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
                                val txt = view?.findViewById<TextView>(android.R.id.message)
                                txt?.setTextColor(Color.WHITE)
                                toast.show()
                            }else {
                                saveOtherTeaExpsToDb()
                            }
                        }

                    }

                }

            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")

          }
    })

        measure_edtx.setOnClickListener {
            val title = titleTxt.text
            val useWord = measure_edtx.hint
            context?.let { it1 -> showUniversalMenu(it1, title, useWord, measure_edtx ) }
        }

        cost_edtx.setOnClickListener {
            val title = titleTxt.text
            val costWord = cost_edtx.hint
            context?.let { it1 -> showUniversalMenu(it1, title, costWord, cost_edtx) }
        }
        others_cost_edtx.setOnClickListener {
            val title = titleTxt.text
            val costWord = others_cost_edtx.hint
            context?.let { it1 -> showUniversalMenu(it1, title, costWord, others_cost_edtx ) }
        }
    }

     private fun callTippingSwich() {
         val toast = Toast.makeText(requireContext(),
                 getString(string.switch_tipping_msg), Toast.LENGTH_LONG)
         val view = toast.view
         view?.background?.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
         val txt = view?.findViewById<TextView>(android.R.id.message)
         txt?.setTextColor(Color.WHITE)

         switch_tipping.setOnClickListener {
             if (switch_tipping.isChecked){
                 toast.show()
                 tea_layout.setBackgroundColor(Color.RED)
                 switch_tipping.setTextColor(Color.RED)
                 measure_edtx.setTextColor(Color.RED)
                 measure_edtx.setHintTextColor(Color.RED)
             //    measure_edtx.setBackgroundColor(Color.RED)
                 tea_pluck_img.setImageResource(R.drawable.ic_young_tea)
                 save_pluck_btn.setBackgroundColor(Color.RED)
                 pluckType = true
             }
             else{
                 pluckType = false
                 tea_pluck_img.setImageResource(R.drawable.ic_pipo_pluck)
                 tea_layout.setBackgroundColor(Color.WHITE)
                 switch_tipping.setTextColor(Color.WHITE)
                 measure_edtx.setHintTextColor(Color.WHITE)
               //  measure_edtx.setBackgroundColor(Color.WHITE)
                 measure_edtx.setTextColor(Color.WHITE)
                 save_pluck_btn.setBackgroundColor(resources.getColor(R.color.teal_800))
             }
         }
     }

     fun showUniversalMenu(mContext: Context, title: CharSequence, words: CharSequence?, editText: EditText){
         val inflater = LayoutInflater.from(mContext)
         val teaCostView : View = inflater.inflate(R.layout.tea_size_dialog, null)
         //  val sizeEdtxt = EditText(mContext)
         val sizeEdtxt: EditText = teaCostView.findViewById(R.id.size_picker) as EditText
         //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
         //    sizeEdtxt.focusable = View.FOCUSABLE
        // }
         if (title == getString(string.others_txt)){
             if (words == "Total Cost"){
                 sizeEdtxt.inputType = InputType.TYPE_CLASS_NUMBER
             }else if (words == getString(string.tea_other_exp_details_hint)){
                 sizeEdtxt.inputType = InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE
             }else if (words == getString(string.tea_others_unit_hint)){
                 sizeEdtxt.inputType = InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE
             }

         }else if (title == getString(string.plucking_txt)){
             if (words == getString(string.plucking_hint)) {
                 sizeEdtxt.inputType = InputType.TYPE_CLASS_NUMBER
             }

         }else if (title == getString(string.weeding_txt)){
             if (words == getString(string.weeding_size_txt) ||
                     words == getString(string.weding_cost_hint_txt)) {
                 sizeEdtxt.inputType = InputType.TYPE_CLASS_NUMBER
             }
         }
         val dialog = AlertDialog.Builder(mContext)
                 .setTitle(title)
                 .setMessage(words)
                 .setView(teaCostView)
                 .setPositiveButton(getString(string.ok_dialog_txt), DialogInterface.OnClickListener { dialog, which ->
                     editText.setText(sizeEdtxt.text)
                 })
                 .setNegativeButton(getString(string.cancel_dialog_txt), null)
                 .create()
         dialog.show()
     }

     fun hasText(edisTtxt: EditText) : Boolean {
         return iSText(edisTtxt, " " )
     }

     fun iSText(ediTtxt: EditText, errorMsg: String): Boolean {
         val txt = ediTtxt.text.toString().trim()
      //   ediTtxt.setError(null)
         if (txt.isNotEmpty()){
             ediTtxt.setError(null)
             return false
         }
        // ediTtxt.setError(errorMsg)
         return true
     }
     fun validate() : Boolean{
         var check = true
         if (!hasText(measure_edtx)) check = false
         if (!hasText(cost_edtx)) check = false
         if (!hasText(others_cost_edtx)) check = false
         return check
     }

     fun saveTeaPluckingToDb(){
         if (hasText(measure_edtx)){
             val toast = Toast.makeText(requireContext(),
                     getString(string.empty_view_msg), Toast.LENGTH_LONG)
             val view = toast.view
             view?.background?.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
             val txt = view?.findViewById<TextView>(android.R.id.message)
             txt?.setTextColor(Color.WHITE)
             toast.show()
             return
         }else {
             val farmDetails: FarmDetails = FarmDetails()
             val capturedTeaWeight = measure_edtx.text.toString()
             val farmDetailsMap: HashMap<String, Any> = HashMap()

             val currentTime = System.currentTimeMillis()
             val sdf = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                     DateFormat.MEDIUM).format(currentTime)

             if (pluckType.equals(true)) {
                 farmDetailsMap.put(farmDetails.tippingWeight!!, capturedTeaWeight)
                 farmDetailsMap.put("date", sdf)
                 cummulateTipping(capturedTeaWeight)
             } else {
                 farmDetailsMap.put("pluckingWeight", capturedTeaWeight)
                 farmDetailsMap.put("date", sdf)
             }

             val teaDbRef = mFirebaseDatabase?.reference?.child(KEY_FARM_ITEM)
             teaDbRef?.push()?.updateChildren(farmDetailsMap)
                     ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                         override fun onComplete(task: Task<Void>) {
                             if (task.isSuccessful) {
                                 SavedPreference.setRecentPluckedTeaWeight(
                                         requireContext(), capturedTeaWeight)
                                 measure_edtx.setText(" ")
                                 Toast.makeText(context, "Kilos submitted successfully!",
                                         Toast.LENGTH_SHORT).show()
                              //   getTheSavedTotals()
                                 WorkManager.getInstance(requireActivity())
                                     .beginUniqueWork("ForegroundWorker", ExistingWorkPolicy.APPEND_OR_REPLACE,
                                         OneTimeWorkRequest.from(ForegroundWorker::class.java)).enqueue().state
                                     .observe(requireActivity()) { state ->
                                         Log.d("CORNE", "ForegroundWorker: $state")
                                     }
                              /*   val fetchedSharedPrefPluckedTotal = SavedPreference
                                         .getPluckedTotal(requireContext()) */

                                 // updatePluckTotal(fetchedSharedPrefPluckedTotal, capturedTeaWeight)


                             } else {
                                 Toast.makeText(context,
                                         "Failed to Submit!, try again \u2661 ",
                                         Toast.LENGTH_SHORT).show()
                             }
                         }
                     })
         }
     }

     private fun updatePluckTotal(fetchedPluckedTotal: String?, capturedTeaWeight: String) {
         val floatFetched : Float? = fetchedPluckedTotal?.toFloat()
         val floatCaptured : Float = capturedTeaWeight.toFloat()
         val newPluckTotal = (floatFetched?.plus(floatCaptured))
                // (floatFetched!! + floatCaptured)

                 //(floatFetched?.plus(floatCaptured!!))

         val teaDbRef = mFirebaseDatabase?.reference?.child("totals")
         val farmDetailsCumulatives = FarmDetailsCumulatives()

         var newPluckTotalMap : HashMap<String, Any> = HashMap()
         newPluckTotalMap.put("MlememTYeMBBeTotals/pluckngAmntCumulative", newPluckTotal.toString())

         teaDbRef?.updateChildren(newPluckTotalMap)
                 ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                     override fun onComplete(task: Task<Void>) {
                         if (task.isSuccessful) {
                           //  measure_edtx.setText(" ")
                             Toast.makeText(context, "Weight Totals submitted successfully!",
                                     Toast.LENGTH_SHORT).show()

                         } else {
                             Toast.makeText(context,
                                     "Failed to Submit!, try again \u2661 ",
                                     Toast.LENGTH_SHORT).show()
                         }
                     }
                 })
     }

     private fun getTheSavedTotals(){
         FirebaseDatabase.getInstance().getReference("items")
                 .addValueEventListener(object : ValueEventListener {

                     override fun onDataChange(snapshot: DataSnapshot) {
                         if (snapshot.exists()){
                             for (snap in snapshot.children){
                                 val selectedItem =
                                         snap.getValue(FarmDetails::class.java)
                                 val tippingAddedUp = selectedItem?.tippingAmntCumulative
                                 val pluckingAddedUp = selectedItem?.pluckngAmntCumulative
                                 val teaExpensesCumulated = selectedItem?.teaExpensesCumulative
                                 val receivedAmntCumulated = selectedItem?.receivedAmntCumulative

                                 Log.d("AMT", pluckingAddedUp.toString())

                             //     SavedPreference.setTipping(requireContext(), tippingAddedUp!!)
                                  SavedPreference.setPlucking(requireContext(), pluckingAddedUp!!)
                              //    SavedPreference.setTeaExpnsTotal(requireContext(), teaExpensesCumulated!!)
                               //   SavedPreference.setTotalMoney(requireContext(), receivedAmntCumulated!!)
                             }
                         }
                     }

                     override fun onCancelled(error: DatabaseError) {
                         TODO("Not yet implemented")
                     }
                 })


     }

     private fun cummulateTipping(capturedTeaWeight: String) {

     }

     fun saveTeaWeedingExpsToDb(){
         val capturedTeaWeedingSize = measure_edtx.text.toString()
         val capturedTeaWeedingCost = cost_edtx.text.toString()
         val farmDetailsMap: HashMap<String, Any> = HashMap()
         val teaDbRef = mFirebaseDatabase?.reference?.child(KEY_FARM_ITEM)
         val currentTime = System.currentTimeMillis()
         val sdf = DateFormat.getDateInstance(DateFormat.MEDIUM)
                 .format(currentTime)

         if ((measure_edtx.text.toString() == " ") ||
                 (cost_edtx.text.toString() == " ")) {

             val toast = Toast.makeText(requireContext(),
                     getString(string.empty_view_msg), Toast.LENGTH_LONG)
             val view = toast.view
             view?.background?.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
             val txt = view?.findViewById<TextView>(android.R.id.message)
             txt?.setTextColor(Color.WHITE)
             toast.show()
             return
         }else {
             farmDetailsMap.put("weedingDate", sdf)
             farmDetailsMap.put("weedingSize", capturedTeaWeedingSize)
             farmDetailsMap.put("weedingCost", capturedTeaWeedingCost)

             teaDbRef?.push()?.updateChildren(farmDetailsMap)
                     ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                         override fun onComplete(task: Task<Void>) {
                             if (task.isSuccessful) {
                                 measure_edtx.setText(" ")
                                 Toast.makeText(context, "Weeding Records submitted successfully!",
                                         Toast.LENGTH_SHORT).show()

                             } else {
                                 Toast.makeText(context,
                                         "Failed to Submit!, try again \u2661 ",
                                         Toast.LENGTH_SHORT).show()
                             }
                         }
                     })
         }
     }

     fun saveOtherTeaExpsToDb(){
         val capturedTeaOthersDetails = measure_edtx.text.toString()
         val capturedTeaOthersSize = cost_edtx.text.toString()
         val capturedTeaOthersCost = others_cost_edtx.text.toString()
         val farmDetailsMap: HashMap<String, Any> = HashMap()
         val teaDbRef = mFirebaseDatabase?.reference?.child(KEY_FARM_ITEM)

         if ((measure_edtx.text.toString() == " ") ||
                 (cost_edtx.text.toString() == " ") ||
                 (others_cost_edtx.text.toString() == " ")) {

             val toast = Toast.makeText(requireContext(),
                     getString(string.empty_view_msg), Toast.LENGTH_LONG)
             val view = toast.view
             view?.background?.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
             val txt = view?.findViewById<TextView>(android.R.id.message)
             txt?.setTextColor(Color.WHITE)
             toast.show()
             return
         }

         val currentTime = System.currentTimeMillis()
         val sdf = DateFormat.getDateInstance(DateFormat.MEDIUM)
                 .format(currentTime)

         farmDetailsMap.put("othersTeaExpnsDate", sdf)
         farmDetailsMap.put("othersTeaExpsCost", capturedTeaOthersCost)
         farmDetailsMap.put("othersTeaExpsDescription", capturedTeaOthersDetails)
         farmDetailsMap.put("othersTeaExpsSize", capturedTeaOthersSize)


         teaDbRef?.push()?.updateChildren(farmDetailsMap)
                 ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                     override fun onComplete(task: Task<Void>) {
                         if (task.isSuccessful){
                             measure_edtx.setText(" ")
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
         private var pluckType: Boolean = false
     }

 }