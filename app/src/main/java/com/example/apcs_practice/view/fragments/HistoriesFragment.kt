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
import com.example.apcs_practice.database.HistoryDBHelper
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
        histories.clear()
        adapter = HistoriesAdapter(this.context!!, histories)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rv_history.layoutManager = linearLayoutManager
        rv_history.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        historiesDB = HistoryDBHelper(this.context!!).writableDatabase
        val data = historiesDB.rawQuery("SELECT * FROM histories", null)

        data.moveToFirst()
        histories.clear()
        for (i in 0 until data.count) {
            val h = History()
            h.id = data.getString(0)
            h.date = data.getString(1)
            h.title = data.getString(2)
            h.session = data.getInt(3)
            h.answer = data.getString(4)
            histories.add(h)

            data.moveToNext()
            adapter.notifyDataSetChanged()
        }

        data.close()
    }

    override fun onDestroy() {
        super.onDestroy()

        historiesDB.close()
    }

}