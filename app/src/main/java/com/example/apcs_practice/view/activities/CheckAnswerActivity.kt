package com.example.apcs_practice.view.activities

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apcs_practice.R
import com.example.apcs_practice.database.HistoryDBHelper
import com.example.apcs_practice.models.Question
import com.example.apcs_practice.view.adapters.CheckAnswerAdapter
import kotlinx.android.synthetic.main.activity_answer_check.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckAnswerActivity : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase
    private lateinit var adapter: CheckAnswerAdapter
    private var questions = ArrayList<Question>()
    private var correctAnswer = ""
    private var myAnswer = ""
    private var numberOfCorrectAnswer = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_check)

        db = HistoryDBHelper(this).writableDatabase

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

        for (i in 0 until myAnswer.count()) {
            if (myAnswer[i] == correctAnswer[i])
                numberOfCorrectAnswer++
        }

        tv_num_correct_answers.text = "$numberOfCorrectAnswer"
    }

    private fun getQuestion(session: Int) {
        val resId = IntArray(4)
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

            questions.add(q)
        }

        arrRes.recycle()
        arrStem.recycle()
        arrChoice.recycle()

        val title = resources.getString(resId[3])
        addHistory(session, title)
    }

    private fun addHistory(session: Int, title: String) {
        val cal = Calendar.getInstance()
        cal.get(Calendar.YEAR)
        cal.get(Calendar.MONTH)
        cal.get(Calendar.DAY_OF_MONTH)

        val myFormat = "yyyy/MM/dd"
        val sdf = SimpleDateFormat(myFormat, Locale.TAIWAN)

        val date = sdf.format(cal.time)
        val id = "${System.currentTimeMillis()}"

        db.execSQL(
            "INSERT INTO histories(id,date,session,title,myAnswer) VALUES(?,?,?,?,?)",
            arrayOf<Any?>(id, date, title, session, title, myAnswer)
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        db.close()
    }
}
