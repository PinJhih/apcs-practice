package com.example.apcs_practice.view.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apcs_practice.R
import com.example.apcs_practice.models.Question
import com.example.apcs_practice.view.adapters.CheckAnswerAdapter
import kotlinx.android.synthetic.main.activity_check_answer.*
import kotlin.collections.ArrayList

class CheckAnswerActivity : AppCompatActivity() {

    private lateinit var adapter: CheckAnswerAdapter
    private var title = ""
    private var questions = ArrayList<Question>()
    private var correctAnswer = ""
    private var myAnswer = ""
    private var numberOfCorrectAnswer = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_answer)

        val session: Int
        intent?.extras?.let {
            myAnswer = it.getString("answer")!!
            session = it.getInt("session")
            getQuestion(session)
        }

        //init RecyclerView
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rv_check_answer.layoutManager = linearLayoutManager
        adapter = CheckAnswerAdapter(this, myAnswer, correctAnswer)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rv_check_answer.addItemDecoration(decoration)
        rv_check_answer.adapter = adapter

        for (i in 0 until myAnswer.count())
            if (myAnswer[i] == correctAnswer[i])
                numberOfCorrectAnswer++
        tv_num_correct_answers.text = "$numberOfCorrectAnswer"

        if (settings.getBoolean("darkMode", false))
            setDarkMode()
    }

    private fun getQuestion(session: Int) {
        val arrRes = resources.obtainTypedArray(session)
        val resId = IntArray(arrRes.length())
        for (i in 0 until arrRes.length()) {
            resId[i] = arrRes.getResourceId(i, 0)
        }

        val arrStem = resources.obtainTypedArray(resId[0])
        val arrChoice = resources.obtainTypedArray(resId[1])
        val arrDetailed = resources.obtainTypedArray(resId[4])
        correctAnswer = resources.getString(resId[2])
        for (i in 0 until arrStem.length()) {
            val q = Question()
            q.stem = arrStem.getString(i)!!
            q.choice_a = arrChoice.getString(i * 4)!!
            q.choice_b = arrChoice.getString(i * 4 + 1)!!
            q.choice_c = arrChoice.getString(i * 4 + 2)!!
            q.choice_d = arrChoice.getString(i * 4 + 3)!!
            q.detailed = arrDetailed.getString(i)!!
            questions.add(q)
        }

        arrRes.recycle()
        arrStem.recycle()
        arrChoice.recycle()
        arrDetailed.recycle()

        title = resources.getString(resId[3])
    }

    fun reviewQuestion(questionNumber: Int) {
        val msg = questions[questionNumber].stem + "\n" +
                "A. ${questions[questionNumber].choice_a}\n" +
                "B. ${questions[questionNumber].choice_b}\n" +
                "C. ${questions[questionNumber].choice_c}\n" +
                "D. ${questions[questionNumber].choice_d}\n" +
                "正解: ${correctAnswer[questionNumber]}"

        AlertDialog.Builder(this)
            .setTitle("第${questionNumber + 1}題")
            .setMessage(msg)
            .setPositiveButton("觀看詳解") { _, _ ->
                showDetailed(msg, questionNumber)
            }
            .setNegativeButton("關閉") { _, _ -> }
            .show()
    }

    private fun showDetailed(question: String, number: Int) {
        val i = Intent(this, DetailedActivity::class.java)
        val content = "題目:\n" + question + "\n\n" + "解析:\n" + questions[number].detailed

        i.putExtra("content", content)
        startActivity(i)
    }

    private fun setDarkMode() {
        val backgroundColor = Color.parseColor("#2B2B2B")
        val textColor = Color.parseColor("#FFFFFF")

        tv_num_correct_answers.setTextColor(textColor)
        tv_title_num_correct_answers.setTextColor(textColor)
        layout_check_answer.setBackgroundColor(backgroundColor)
    }
}
