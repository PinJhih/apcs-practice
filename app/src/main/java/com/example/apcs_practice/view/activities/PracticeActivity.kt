package com.example.apcs_practice.view.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apcs_practice.R
import com.example.apcs_practice.database.HistoryDBHelper
import com.example.apcs_practice.models.Question
import com.example.apcs_practice.view.adapters.CodeAdapter
import kotlinx.android.synthetic.main.activity_practice.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PracticeActivity : AppCompatActivity() {

    private var questions = ArrayList<Question>()
    private var session = 0
    private lateinit var answers: CharArray
    private lateinit var adapter: CodeAdapter
    private var code = ArrayList<String>()
    private var questionNumber = 0
    private var correctAnswer = ""
    private var title = ""

    private fun setQuestions(session: Int) {
        val arrRes = resources.obtainTypedArray(session)
        val resId = IntArray(arrRes.length())
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

        title = resources.getString(resId[3])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rv_code.layoutManager = linearLayoutManager
        adapter = CodeAdapter(code)
        rv_code.adapter = adapter

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
            ArrayAdapter(
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
                questionNumber = questions.size - 1
            setView()
        }
        btn_next.setOnClickListener {
            questionNumber++
            if (questionNumber >= questions.size)
                questionNumber = 0
            setView()
        }
        btn_finish.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("確定結束?")
                .setNegativeButton("取消") { _, _ -> }
                .setPositiveButton("結束") { _, _ ->
                    checkAnswer(session)
                }
                .show()
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

        if (settings.getBoolean("darkMode", false))
            setDarkMode()
        setTextView()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("確定結束?")
            .setNegativeButton("取消") { _, _ -> }
            .setPositiveButton("結束") { _, _ ->
                checkAnswer(session)
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putCharArray("answers", answers)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        answers = savedInstanceState.getCharArray("answers")!!
    }

    private fun setView() {
        tv_choice_a.text = questions[questionNumber].choice_a
        tv_choice_b.text = questions[questionNumber].choice_b
        tv_choice_c.text = questions[questionNumber].choice_c
        tv_choice_d.text = questions[questionNumber].choice_d
        spinner_number.setSelection(questionNumber)
        setStem()

        when (answers[questionNumber]) {
            'A' -> btn_choice_a.isChecked = true
            'B' -> btn_choice_b.isChecked = true
            'C' -> btn_choice_c.isChecked = true
            'D' -> btn_choice_d.isChecked = true
            else -> radioGroup.clearCheck()
        }
        scrollView.scrollTo(0, 0)
    }

    private fun setStem() {
        var stem = ""
        var i = 0
        while (!(questions[questionNumber].stem[i] == '\n' && questions[questionNumber].stem[i + 1] == '\n')) {
            stem += questions[questionNumber].stem[i]
            i++
        }
        i += 2
        setCode(i)
        tv_stem.text = stem
    }

    private fun setCode(index: Int) {
        code.clear()
        var i = index
        while (i < questions[questionNumber].stem.length) {
            var singleLine = ""
            while (questions[questionNumber].stem[i] != '\n') {
                singleLine += questions[questionNumber].stem[i]
                i++
                if (i >= questions[questionNumber].stem.length)
                    break
            }
            code.add(singleLine)
            adapter.notifyDataSetChanged()
            i++
            if (i >= questions[questionNumber].stem.length)
                break
        }
        if (code.size == 0)
            rv_code.isVisible = false
    }

    private fun checkAnswer(session: Int) {
        val intent = Intent(this, CheckAnswerActivity::class.java)
        val b = Bundle()
        var numberOfCorrectAnswer = 0
        for (i in correctAnswer.indices)
            if (answers[i] == correctAnswer[i])
                numberOfCorrectAnswer++
        addHistory(session, numberOfCorrectAnswer)

        b.putInt("session", session)
        b.putString("answer", String(answers))
        intent.putExtras(b)
        startActivityForResult(intent, 1)
    }

    private fun addHistory(session: Int, numberOfCorrectAnswer: Int) {
        val db = HistoryDBHelper(this).writableDatabase
        val cal = Calendar.getInstance()
        cal.get(Calendar.YEAR)
        cal.get(Calendar.MONTH)
        cal.get(Calendar.DAY_OF_MONTH)
        val myFormat = "yyyy/MM/dd"
        val sdf = SimpleDateFormat(myFormat, Locale.TAIWAN)
        val date = sdf.format(cal.time)
        val id = "${System.currentTimeMillis()}"
        val correctRate = "$numberOfCorrectAnswer/25"

        db.execSQL(
            "INSERT INTO histories(id,date,title,session,myAnswer,correctRate) VALUES(?,?,?,?,?,?)",
            arrayOf<Any?>(id, date, title, session, String(answers), correctRate)
        )
        db.close()
    }

    private fun setDarkMode() {
        val backgroundColor = Color.parseColor("#2B2B2B")
        val textColor = Color.parseColor("#FAFAFA")

        tv_stem.setTextColor(textColor)
        tv_a.setTextColor(textColor)
        tv_b.setTextColor(textColor)
        tv_c.setTextColor(textColor)
        tv_d.setTextColor(textColor)
        tv_choice_a.setTextColor(textColor)
        tv_choice_b.setTextColor(textColor)
        tv_choice_c.setTextColor(textColor)
        tv_choice_d.setTextColor(textColor)
        btn_choice_a.setTextColor(textColor)
        btn_choice_b.setTextColor(textColor)
        btn_choice_c.setTextColor(textColor)
        btn_choice_d.setTextColor(textColor)
        layout_practice.setBackgroundColor(backgroundColor)
    }

    private fun setTextView() {
        val size = settings.getFloat("textSize", 18F)

        tv_stem.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_a.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_b.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_c.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_d.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_choice_a.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_choice_b.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_choice_c.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        tv_choice_d.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }
}
