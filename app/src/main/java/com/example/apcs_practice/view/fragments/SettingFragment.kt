package com.example.apcs_practice.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apcs_practice.R
import com.example.apcs_practice.view.activities.settings
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {

    private val settingEditor = settings.edit()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_setting, container, false)
        setView()

        switch_dark_mode.setOnClickListener {
            settingEditor.putBoolean("darkMode", switch_dark_mode.isChecked)
            setView()
        }

        radioGroup_text_size.setOnCheckedChangeListener { _, i ->
            when (i) {
                btn_small.id -> settingEditor.putString("textSize", "small")
                btn_mid.id -> settingEditor.putString("textSize", "mid")
                else -> settingEditor.putString("textSize", "big")
            }
            setView()
        }

        return root
    }

    private fun setView() {
        var backgroundColor = Color.parseColor("#ffffff")
        var textColor = Color.parseColor("#000000")
        if (settings.getBoolean("darkMode", false)) {
            backgroundColor = Color.parseColor("#000000")
            textColor = Color.parseColor("#ffffff")
        }
        layout_setting.setBackgroundColor(backgroundColor)

        tv_text_size.setTextColor(textColor)
        btn_big.setTextColor(textColor)
        btn_mid.setTextColor(textColor)
        btn_small.setTextColor(textColor)
        switch_dark_mode.setTextColor(textColor)

        var textSize = btn_mid.textSize
        when (settings.getString("textSize", "mid")) {
            "big" -> {
                btn_big.isChecked = true
                textSize = btn_big.textSize
            }
            "small" -> {
                btn_small.isChecked = true
                textSize = btn_small.textSize
            }
            else -> btn_mid.isChecked = true
        }

        tv_text_size.textSize = textSize
        switch_dark_mode.textSize = textSize
    }

    override fun onDestroy() {
        super.onDestroy()

        settingEditor.commit()
    }

}