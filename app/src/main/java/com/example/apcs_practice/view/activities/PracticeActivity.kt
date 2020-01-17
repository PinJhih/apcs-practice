package com.example.apcs_practice.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.example.apcs_practice.R
import com.example.apcs_practice.models.Question
import kotlinx.android.synthetic.main.activity_practice.*

class PracticeActivity : AppCompatActivity() {

    private var questions = ArrayList<Question>()
    private var questionNumber = 0

    private fun setQuestions(session: Int) {
        val resId = IntArray(2)
        val arrRes = resources.obtainTypedArray(session)

        for (i in 0 until arrRes.length()) {
            resId[i] = arrRes.getResourceId(i, 0)
        }

        val arrStem = resources.obtainTypedArray(resId[0])
        val arrChoice = resources.obtainTypedArray(resId[1])

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        intent?.extras?.let {
            val session = it.getInt("session")
            setQuestions(session)
        }

        val answers = CharArray(questions.size)
        for (i in 0 until answers.count())
            answers[i] = 'x'
        setView()

        //init spinner
        val spinnerItems = ArrayList<String>()
        for (i in 0 until questions.size)
            spinnerItems.add("第${i + 1}題")
        val spinnerAdapter =
            android.widget.ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerItems
            )
        spinner_number.adapter = spinnerAdapter

        //spinner listener
        spinner_number.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                questionNumber = position
                setView()
            }
        }

        //button listener
        btn_last.setOnClickListener {
            questionNumber--
            if (questionNumber < 0)
                questionNumber = questions.size
            setView()
        }
        btn_next.setOnClickListener {
            questionNumber++
            if (questionNumber > questions.size)
                questionNumber = 0
            setView()
        }
    }

    private fun setView() {
        val choice = "A. "+questions[questionNumber].choice_a + "\n" +
                "B. "+questions[questionNumber].choice_b + "\n" +
                "C. "+questions[questionNumber].choice_c + "\n" +
                "D. "+questions[questionNumber].choice_d

        tv_stem.text = questions[questionNumber].stem
        tv_choices.text = choice
    }
}
