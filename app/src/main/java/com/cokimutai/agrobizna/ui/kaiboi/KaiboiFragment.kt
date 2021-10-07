package com.cokimutai.agrobizna.ui.kaiboi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cokimutai.agrobizna.R

class KaiboiFragment : Fragment() {

    private lateinit var kaiboiViewModel: KaiboiViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        kaiboiViewModel =
                ViewModelProvider(this).get(KaiboiViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        kaiboiViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }
}