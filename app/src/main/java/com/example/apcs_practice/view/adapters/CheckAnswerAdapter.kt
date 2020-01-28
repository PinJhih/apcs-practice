package com.example.apcs_practice.view.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apcs_practice.R
import com.example.apcs_practice.view.activities.CheckAnswerActivity
import com.example.apcs_practice.view.activities.settings
import kotlinx.android.synthetic.main.item_check_answer.view.*

class CheckAnswerAdapter(
    private val context: Context,
    private val myAnswer: String,
    private val correctAnswer: String
) :
    RecyclerView.Adapter<CheckAnswerAdapter.ViewHolder>() {
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_check_answer, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = correctAnswer.length
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemView = holder.itemView
        val questionNumber = "第${position + 1}題"
        itemView.tv_question_number.text = questionNumber
        itemView.tv_correct_answer.text = correctAnswer[position].toString()
        itemView.tv_my_answer.text = myAnswer[position].toString()

        itemView.layout_item_check_answer.setOnClickListener {
            (context as CheckAnswerActivity).reviewQuestion(position)
        }

        if(settings.getBoolean("darkMode",false)){
            val backgroundColor = Color.parseColor("#000000")
            val textColor = Color.parseColor("#ffffff")

            itemView.tv_question_number.setTextColor(textColor)
            itemView.tv_correct_answer.setTextColor(textColor)
            itemView.tv_my_answer.setTextColor(textColor)
            itemView.layout_item_check_answer.setBackgroundColor(backgroundColor)
        }
    }
}
