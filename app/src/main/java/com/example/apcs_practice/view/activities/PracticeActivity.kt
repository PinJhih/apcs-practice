package com.example.apcs_practice.view.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.example.apcs_practice.R
import com.example.apcs_practice.models.Question
import kotlinx.android.synthetic.main.activity_practice.*

class PracticeActivity : AppCompatActivity() {

    private var questions = ArrayList<Question>()
    private lateinit var answers: CharArray
    private var questionNumber = 0

    private fun setQuestions(session: Int) {
        val resId = IntArray(2)
        val arrRes = resources.obtainTypedArray(session)

        for (i in 0 until 2) {
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

        var session = 0

        intent?.extras?.let {
            session = it.getInt("session")
            setQuestions(session)
        }

        answers = CharArray(questions.size)
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
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                //reset the view when user select the question
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
        btn_finish.setOnClickListener {
            checkAnswer(session)
        }
        //save the answer
        radioGroup.setOnCheckedChangeListener { _, i ->
            answers[questionNumber] = when (i) {
                btn_choice_a.id -> 'A'
                btn_choice_b.id -> 'B'
                btn_choice_c.id -> 'C'
                btn_choice_d.id -> 'D'
                else -> 'x'
            }
        }

        if(settings.getBoolean("darkMode",false))
            setDarkMode()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        finish()
    }

    private fun setView() {
        val choice = "A. " + questions[questionNumber].choice_a + "\n" +
                "B. " + questions[questionNumber].choice_b + "\n" +
                "C. " + questions[questionNumber].choice_c + "\n" +
                "D. " + questions[questionNumber].choice_d

        tv_stem.text = questions[questionNumber].stem
        tv_choices.text = choice
        spinner_number.setSelection(questionNumber)

        when (answers[questionNumber]) {
            'A' -> btn_choice_a.isChecked = true
            'B' -> btn_choice_b.isChecked = true
            'C' -> btn_choice_c.isChecked = true
            'D' -> btn_choice_d.isChecked = true
            else -> radioGroup.clearCheck()
        }
    }

    private fun checkAnswer(session: Int) {

        val i = Intent(this, CheckAnswerActivity::class.java)
        val b = Bundle()
        b.putInt("session", session)
        b.putString("answer", String(answers))
        i.putExtras(b)
        startActivityForResult(i, 1)
    }

    private fun setDarkMode(){
        val backgroundColor = Color.parseColor("#000000")
        val textColor = Color.parseColor("#ffffff")

        tv_stem.setTextColor(textColor)
        tv_choices.setTextColor(textColor)
        btn_choice_a.setTextColor(textColor)
        btn_choice_b.setTextColor(textColor)
        btn_choice_c.setTextColor(textColor)
        btn_choice_d.setTextColor(textColor)
        layout_practice.setBackgroundColor(backgroundColor)
    }
}
