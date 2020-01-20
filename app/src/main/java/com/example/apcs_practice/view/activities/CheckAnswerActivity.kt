package com.example.apcs_practice.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apcs_practice.R
import com.example.apcs_practice.models.Question
import com.example.apcs_practice.view.adapters.CheckAnswerAdapter
import kotlinx.android.synthetic.main.activity_answer_check.*

class CheckAnswerActivity : AppCompatActivity() {

    private lateinit var adapter: CheckAnswerAdapter
    private var correctAnswer = ""
    private var myAnswer = ""
    private var numberOfCorrectAnswer = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_check)

        var session: Int
        intent?.extras?.let {
            myAnswer = it.getString("answer")!!
            session = it.getInt("session")
            getQuestion(session)
        }

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rv_check_answer.layoutManager = linearLayoutManager
        adapter = CheckAnswerAdapter(this, myAnswer, correctAnswer)
        rv_check_answer.adapter = adapter

        for(i in 0 until myAnswer.count()){
            if(myAnswer[i] == correctAnswer[i])
                numberOfCorrectAnswer++
        }
        tv_num_correct_answers.text = "$numberOfCorrectAnswer"
    }

    private fun getQuestion(session: Int) {
        val resId = IntArray(3)
        val arrRes = resources.obtainTypedArray(session)

        for (i in 0 until arrRes.length()) {
            resId[i] = arrRes.getResourceId(i, 0)
        }

        val arrStem = resources.obtainTypedArray(resId[0])
        val arrChoice = resources.obtainTypedArray(resId[1])
        correctAnswer = resources.getString(resId[2])

        for (i in 0 until arrStem.length()) {
            val q = Question()
            q.stem = arrStem.getString(i)!!
            q.choice_a = arrChoice.getString(i * 4)!!
            q.choice_b = arrChoice.getString(i * 4 + 1)!!
            q.choice_c = arrChoice.getString(i * 4 + 2)!!
            q.choice_d = arrChoice.getString(i * 4 + 3)!!
        }

        arrRes.recycle()
        arrStem.recycle()
        arrChoice.recycle()
    }
}
