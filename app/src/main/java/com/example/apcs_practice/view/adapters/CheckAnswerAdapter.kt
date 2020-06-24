package com.example.apcs_practice.view.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apcs_practice.R
import com.example.apcs_practice.view.activities.CheckAnswerActivity
import kotlinx.android.synthetic.main.item_check_answer.view.*

class CheckAnswerAdapter(
    private val context: Context,
    private val myAnswer: String,
    private val correctAnswers: String
) :
    RecyclerView.Adapter<CheckAnswerAdapter.ViewHolder>() {
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_check_answer, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = correctAnswers.length
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val correctAnswer = "正解: " + correctAnswers[position].toString()
        val itemView = holder.itemView
        val questionNumber = "第${position + 1}題"
        val bgColor =
            if (myAnswer[position] == correctAnswers[position]) Color.parseColor("#D9CBFFC4")
            else Color.parseColor("#D9FFC0C0")
        val textColor =
            if (myAnswer[position] == correctAnswers[position]) Color.parseColor("#3C783C")
            else Color.parseColor("#AA4343")

        itemView.tv_question_number.text = questionNumber
        itemView.tv_correct_answer.text = correctAnswer
        itemView.tv_question_number.setTextColor(textColor)
        itemView.tv_correct_answer.setTextColor(textColor)
        itemView.tv_my_answer.setTextColor(textColor)
        itemView.tv_my_answer.text = myAnswer[position].toString()
        itemView.cv_check_answer.setCardBackgroundColor(bgColor)
        itemView.setOnClickListener {
            (context as CheckAnswerActivity).reviewQuestion(position)
        }
    }
}
