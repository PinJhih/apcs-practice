package com.example.apcs_practice.view.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apcs_practice.R
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

        itemView.layout_check_answer.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("抬頭")
                .setPositiveButton("OK") { _, _ ->
                    TODO("review the question")
                }
                .show()
        }
    }
}