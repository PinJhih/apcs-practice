package com.example.apcs_practice.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.apcs_practice.R
import com.example.apcs_practice.database.HistoryDBHelper
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

        setSwitches()
        setTextView()
        /*
        switch_dark_mode.setOnClickListener {
            settingEditor.putBoolean("darkMode", switch_dark_mode.isChecked)
            settingEditor.commit()
            setView()
        }
        */
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
        tv_delete_histories.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("刪除所有歷史紀錄")
                .setNegativeButton("取消") { _, _ -> }
                .setPositiveButton("確定") { _, _ ->
                    val db = HistoryDBHelper(context!!).writableDatabase
                    val histories = db.rawQuery("SELECT * FROM histories", null)
                    if (histories.count != 0) {
                        db.execSQL("DELETE FROM histories")
                        Toast.makeText(context, "已刪除${histories.count}筆資料", Toast.LENGTH_SHORT)
                            .show()
                    } else
                        Toast.makeText(context, "沒有歷史紀錄", Toast.LENGTH_SHORT).show()
                    histories.close()
                    db.close()
                }.show()
        }
        tv_reset_settings.setOnClickListener {
            settingEditor.putBoolean("notificationTest", true)
            settingEditor.putBoolean("notificationSignUpStart", true)
            settingEditor.putBoolean("notificationSignUpEnd", true)
            settingEditor.putBoolean("queryResultsStart", true)
            settingEditor.putString("textSize", "mid")

            settingEditor.commit()
            setSwitches()
        }
        tv_text_size.setOnClickListener {
            val item = arrayOf("大", "中", "小")
            var position = when (settings.getFloat("textSize", 18F)) {
                20F -> 0
                18F -> 1
                else -> 2
            }
            AlertDialog.Builder(context)
                .setTitle("字體大小")
                .setSingleChoiceItems(item, position) { _, i ->
                    position = i
                }
                .setPositiveButton("確定") { _, _ ->
                    val size = when (position) {
                        0 -> 20F
                        1 -> 18F
                        else -> 16F
                    }
                    settingEditor.putFloat("textSize", size)
                    settingEditor.commit()
                    setTextView()
                }
                .show()
        }
    }

    private fun setTextView() {
        val size = settings.getFloat("textSize", 18F)
        tv_test_date.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_sign_up_start.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_sign_up_end.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_query_results.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_delete_histories.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_reset_settings.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_text_size.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_info.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    private fun setSwitches() {
        /*
        if (settings.getBoolean("darkMode", false))
            switch_dark_mode.isChecked = true
        */
        if (settings.getBoolean("notificationTest", true))
            switch_notification_test.isChecked = true
        if (settings.getBoolean("notificationSignUpStart", true))
            switch_sign_up_start.isChecked = true
        if (settings.getBoolean("notificationSignUpEnd", true))
            switch_sign_up_end.isChecked = true
        if (settings.getBoolean("queryResultsStart", true))
            switch_query_results.isChecked = true
    }

    /*

    dark mode
    private fun setColors() {
        var backgroundColor = Color.parseColor("#FAFAFA")
        var textColor = Color.parseColor("#000000")
        if (settings.getBoolean("darkMode", false)) {
            backgroundColor = Color.parseColor("#2B2B2B")
            textColor = Color.parseColor("#FFFFFF")
        }
        layout_setting.setBackgroundColor(backgroundColor)
        /*
        switch_dark_mode.setTextColor(textColor)
        */
    }
    */

    override fun onDestroy() {
        super.onDestroy()

        settingEditor.commit()
    }
}
