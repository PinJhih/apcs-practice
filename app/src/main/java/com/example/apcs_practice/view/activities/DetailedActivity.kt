package com.example.apcs_practice.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.apcs_practice.R
import kotlinx.android.synthetic.main.activity_detailed.*

class DetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        intent?.extras?.let {
            wv_detailed.loadUrl(it.getString("url"))
        }
    }
}
