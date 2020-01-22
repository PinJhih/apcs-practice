package com.example.apcs_practice.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apcs_practice.R
import com.example.apcs_practice.models.History
import com.example.apcs_practice.view.activities.MainActivity
import kotlinx.android.synthetic.main.item_history.view.*

class HistoriesAdapter(
    private val context: Context,
    private val histories: ArrayList<History>
) :
    RecyclerView.Adapter<HistoriesAdapter.ViewHolder>() {
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_history, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = histories.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemView = holder.itemView

        itemView.tv_date.text = histories[position].date
        itemView.tv_session.text = histories[position].title

        itemView.layout_history.setOnClickListener {
            val session = histories[position].session
            val answer = histories[position].answer

            (context as MainActivity).checkAnswer(session, answer)
        }
    }
}
