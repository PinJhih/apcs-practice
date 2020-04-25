package com.example.apcs_practice.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apcs_practice.R
import com.example.apcs_practice.view.adapters.CodeAdapter
import kotlinx.android.synthetic.main.activity_practice.*
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    private var code=ArrayList<String>()
    private lateinit var adapter:CodeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rv.layoutManager = linearLayoutManager
        adapter = CodeAdapter(code)
        rv.adapter = adapter
        code.add("aaaa")
        adapter.notifyDataSetChanged()
        code.add("bbb")
        adapter.notifyDataSetChanged()
    }
}
