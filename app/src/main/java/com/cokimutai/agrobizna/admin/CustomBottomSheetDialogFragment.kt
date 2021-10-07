package com.cokimutai.agrobizna.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cokimutai.agrobizna.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_view.*


class CustomBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {

        const val TAG = "CustomBottomSheetDialogFragment"

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        firstButton.setOnClickListener {
            val pluckIntent = Intent(it.context, AdminTeaRecords::class.java)
            pluckIntent.putExtra("teaType", 0)
            startActivity(pluckIntent)
        }

        secondButton.setOnClickListener {
            val weedingIntent = Intent(it.context, AdminTeaRecords::class.java)
            weedingIntent.putExtra("teaType", 1)
            startActivity(weedingIntent)
        }
        thirdButton.setOnClickListener {
            val otherTeaExpnsIntent = Intent(it.context, AdminTeaRecords::class.java)
            otherTeaExpnsIntent.putExtra("teaType", 2)
            startActivity(otherTeaExpnsIntent)
        }

    }
}