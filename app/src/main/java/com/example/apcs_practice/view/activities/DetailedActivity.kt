package com.example.apcs_practice.view.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import androidx.core.text.HtmlCompat
import com.example.apcs_practice.R
import kotlinx.android.synthetic.main.activity_detailed.*

class DetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        showDetailed()
        setTextView()

        if (settings.getBoolean("darkMode", false)) {
            val backgroundColor = Color.parseColor("#2B2B2B")
            val textColor = Color.parseColor("#FFFFFF")
            layout_detailed.setBackgroundColor(backgroundColor)
            tv_content.setTextColor(textColor)
        }
    }

    private fun setTextView() {
        val size = settings.getFloat("textSize", 18F)
        tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    private fun showDetailed() {
        intent?.extras?.let {
            tv_content.text = Html.fromHtml(it.getString("content"), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }
}
