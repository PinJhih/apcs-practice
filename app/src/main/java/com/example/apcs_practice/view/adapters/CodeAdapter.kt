package com.example.apcs_practice.view.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apcs_practice.R
import kotlinx.android.synthetic.main.item_code.view.*

class CodeAdapter(
    private val code: ArrayList<String>
) :
    RecyclerView.Adapter<CodeAdapter.ViewHolder>() {
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_code, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = code.count()
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.itemView
        itemView.tv_code.text = code[position]

        itemView.setBackgroundColor(
            if (position % 2 == 0)
                Color.parseColor("#3C3F3F")
            else
                Color.parseColor("#2B2B2B")
        )
    }
}
