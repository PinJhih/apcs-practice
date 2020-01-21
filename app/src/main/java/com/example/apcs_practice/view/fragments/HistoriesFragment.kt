package com.example.apcs_practice.view.fragments

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apcs_practice.R
import com.example.apcs_practice.models.History
import com.example.apcs_practice.view.adapters.HistoriesAdapter
import kotlinx.android.synthetic.main.fragment_histories.*


class HistoriesFragment : Fragment() {

    private lateinit var historiesDB: SQLiteDatabase
    private lateinit var adapter: HistoriesAdapter
    private var histories = ArrayList<History>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_histories, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(this.context)
        adapter = HistoriesAdapter(this.context!!, histories)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rv_history.layoutManager = linearLayoutManager
        rv_history.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()

        historiesDB.close()
    }

}