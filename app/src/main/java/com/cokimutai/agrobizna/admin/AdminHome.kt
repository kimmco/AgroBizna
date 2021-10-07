package com.cokimutai.agrobizna.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.cokimutai.agrobizna.R
import kotlinx.android.synthetic.main.activity_admin_home.*


class AdminHome : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        view_tea_btn.setOnClickListener {

            CustomBottomSheetDialogFragment().apply {
                show(supportFragmentManager, CustomBottomSheetDialogFragment.TAG)
            }

        }

        view_cattle_btn.setOnClickListener {
            val cattleIntent = Intent(it.context, AdminCattleActivity::class.java)
            cattleIntent.putExtra("teaType", 3)
            startActivity(cattleIntent)
        }

        general_expns_btn.setOnClickListener {
            val cattleIntent = Intent(it.context, AdminCattleActivity::class.java)
            cattleIntent.putExtra("teaType", 4)
            startActivity(cattleIntent)
        }
    }
}