package com.example.apcs_practice.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.apcs_practice.R
import com.example.apcs_practice.view.activities.PracticeActivity
import com.example.apcs_practice.viewmodels.SessionSelectViewModel
import kotlinx.android.synthetic.main.fragment_session_select.view.*

class SessionSelectFragment : Fragment() {

    private lateinit var sessionSelectViewModel: SessionSelectViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sessionSelectViewModel =
            ViewModelProviders.of(this).get(SessionSelectViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_session_select, container, false)

        root.btn_10503.setOnClickListener {
            val id = R.array.res_10503
            startPractice(id)
        }

        root.btn_10510.setOnClickListener {
            val id = R.array.res_10510
            startPractice(id)
        }

        root.btn_10603.setOnClickListener {
            val id = R.array.res_10603
            startPractice(id)
        }

        return root
    }

    private fun startPractice(resId:Int){
        val intent = Intent(this.context, PracticeActivity::class.java)

        intent.putExtra("session",resId)
        startActivity(intent)
    }

}