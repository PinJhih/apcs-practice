package com.example.apcs_practice.view.activities

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.TypedValue
import androidx.core.view.isVisible
import com.example.apcs_practice.R
import com.example.apcs_practice.database.DetailedDBHelper
import com.example.apcs_practice.models.Detailed
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detailed.*

class DetailedActivity : AppCompatActivity() {

    private lateinit var fireStore: FirebaseFirestore
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        db = DetailedDBHelper(this).writableDatabase
        fireStore = FirebaseFirestore.getInstance()

        showDetailed()
        setTextView()

        if (settings.getBoolean("darkMode", false)) {
            val backgroundColor = Color.parseColor("#2B2B2B")
            val textColor = Color.parseColor("#FFFFFF")
            layout_detailed.setBackgroundColor(backgroundColor)
            tv_content.setTextColor(textColor)
        }
    }

    private fun setTextView() {
        val size = settings.getFloat("textSize", 18F)
        tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    private fun showDetailed() {
        intent?.extras?.let {
            var detailed = Detailed()
            val id = it.getString("detailedId")!!
            val detaileds = db.rawQuery("SELECT * FROM detaileds WHERE id LIKE '$id'", null)
            val handler = Handler(Handler.Callback { msg ->
                when (msg.what) {
                    1 -> {
                        tv_content.text = detailed.content
                        layout_loading.isVisible = false
                    }
                }
                true
            })

            layout_loading.isVisible = true
            if (detaileds.count == 0) {
                fireStore.collection("detailed").document(id)
                    .get()
                    .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                        Thread(Runnable {
                            detailed = documentSnapshot.toObject(Detailed::class.java)!!
                            Thread.sleep(300)
                            db.execSQL(
                                "INSERT INTO detaileds(id,content) VALUES(?,?)",
                                arrayOf<Any?>(id, detailed.content)
                            )
                            val msg = Message()
                            msg.what = 1
                            handler.sendMessage(msg)
                        }).start()
                    }
            } else {
                detaileds.moveToFirst()
                tv_content.text = detaileds.getString(1)
                layout_loading.isVisible = false
            }
            detaileds.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        db.close()
    }
}
