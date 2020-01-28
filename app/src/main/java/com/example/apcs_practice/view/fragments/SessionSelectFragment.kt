package com.example.apcs_practice.view.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apcs_practice.R
import com.example.apcs_practice.view.activities.PracticeActivity
import com.example.apcs_practice.view.activities.settings
import kotlinx.android.synthetic.main.fragment_session_select.*
import kotlinx.android.synthetic.main.fragment_session_select.view.*

class SessionSelectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_session_select, container, false)

        root.btn_10503.setOnClickListener {
            startPractice(R.array.res_10503)
        }

        root.btn_10510.setOnClickListener {
            startPractice(R.array.res_10510)
        }

        root.btn_10603.setOnClickListener {
            startPractice(R.array.res_10603)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(settings.getBoolean("darkMode",false)){
            val backgroundColor = Color.parseColor("#000000")
            layout_session_select.setBackgroundColor(backgroundColor)
        }
    }

    private fun startPractice(session: Int) {
        val intent = Intent(this.context, PracticeActivity::class.java)

        intent.putExtra("session", session)
        startActivity(intent)
    }
}
