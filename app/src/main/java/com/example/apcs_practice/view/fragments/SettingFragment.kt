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
    ): View = inflater.inflate(R.layout.fragment_setting, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switch_dark_mode.setOnClickListener {
            settingEditor.putBoolean("darkMode", switch_dark_mode.isChecked)
            settingEditor.commit()
            setView()
        }
        switch_notification_test.setOnClickListener {
            settingEditor.putBoolean("notificationTest", switch_notification_test.isChecked)
            settingEditor.commit()
        }
        switch_sign_up_start.setOnClickListener {
            settingEditor.putBoolean("notificationSignUpStart", switch_sign_up_start.isChecked)
            settingEditor.commit()
        }
        switch_sign_up_end.setOnClickListener {
            settingEditor.putBoolean("notificationSignUpEnd", switch_sign_up_end.isChecked)
            settingEditor.commit()
        }
        switch_query_results.setOnClickListener {
            settingEditor.putBoolean("queryResultsStart", switch_query_results.isChecked)
            settingEditor.commit()
        }

        init()
    }

    private fun init(){
        if (settings.getBoolean("darkMode", false))
            switch_dark_mode.isChecked = true
        if (settings.getBoolean("notificationTest", false))
            switch_notification_test.isChecked = true
        if (settings.getBoolean("notificationSignUpStart", false))
            switch_sign_up_start.isChecked = true
        if (settings.getBoolean("notificationSignUpEnd", false))
            switch_sign_up_end.isChecked = true
        if (settings.getBoolean("queryResultsStart", false))
            switch_query_results.isChecked = true

        setView()
    }

    private fun setView() {
        var backgroundColor = Color.parseColor("#FAFAFA")
        var textColor = Color.parseColor("#000000")
        if (settings.getBoolean("darkMode", false)) {
            backgroundColor = Color.parseColor("#2B2B2B")
            textColor = Color.parseColor("#FFFFFF")
        }
        layout_setting.setBackgroundColor(backgroundColor)
        switch_dark_mode.setTextColor(textColor)
        switch_notification_test.setTextColor(textColor)
        switch_sign_up_start.setTextColor(textColor)
        switch_sign_up_end.setTextColor(textColor)
        switch_query_results.setTextColor(textColor)
    }

    override fun onDestroy() {
        super.onDestroy()

        settingEditor.commit()
    }
}
