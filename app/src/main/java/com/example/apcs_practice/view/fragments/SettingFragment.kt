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
    ): View= inflater.inflate(R.layout.fragment_setting, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switch_dark_mode.setOnClickListener {
            settingEditor.putBoolean("darkMode", switch_dark_mode.isChecked)
            settingEditor.commit()
            setView()
        }

        setView()
    }

    private fun setView() {
        if(settings.getBoolean("darkMode",false))
            switch_dark_mode.isChecked = true

        var backgroundColor = Color.parseColor("#ffffff")
        var textColor = Color.parseColor("#000000")
        if (settings.getBoolean("darkMode", false)) {
            backgroundColor = Color.parseColor("#000000")
            textColor = Color.parseColor("#ffffff")
        }
        layout_setting.setBackgroundColor(backgroundColor)
        switch_dark_mode.setTextColor(textColor)
    }

    override fun onDestroy() {
        super.onDestroy()

        settingEditor.commit()
    }
}
