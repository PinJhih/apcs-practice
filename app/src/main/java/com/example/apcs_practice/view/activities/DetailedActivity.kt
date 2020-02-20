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
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_detailed.*

class DetailedActivity : AppCompatActivity() {

    private lateinit var fireStore: FirebaseFirestore
    private lateinit var db: SQLiteDatabase
    private lateinit var listenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        db = DetailedDBHelper(this).writableDatabase
        fireStore = FirebaseFirestore.getInstance()

        updateData()
        showDetailed()
        setTextView()

        if (settings.getBoolean("darkMode", false)) {
            val backgroundColor = Color.parseColor("#2B2B2B")
            val textColor = Color.parseColor("#FFFFFF")
            layout_detailed.setBackgroundColor(backgroundColor)
            tv_content.setTextColor(textColor)
        }
    }

    private fun setTextView(){
        val size = settings.getFloat("textSize",18F)
        tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    private fun showDetailed() {
        intent?.extras?.let {
            val id = it.getString("detailedId")!!
            val detailed = db.rawQuery("SELECT * FROM detaileds WHERE id LIKE '$id'", null)
            val handler = Handler(Handler.Callback { msg ->
                when (msg.what) {
                    1 -> {
                        layout_loading.isVisible = false
                    }
                }
                true
            })

            layout_loading.isVisible = true
            if (detailed.count == 0) {
                fireStore.collection("detailed").document(id)
                    .get()
                    .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                        Thread(Runnable {
                            val d = documentSnapshot.toObject(Detailed::class.java)!!
                            tv_content.text = d.content
                            Thread.sleep(300)
                            val msg = Message()
                            msg.what = 1
                            handler.sendMessage(msg)

                            db.execSQL(
                                "INSERT INTO detaileds(id,content) VALUES(?,?)",
                                arrayOf<Any?>(id, d.content)
                            )
                        }).start()
                    }
            } else {
                detailed.moveToFirst()
                tv_content.text = detailed.getString(1)
                layout_loading.isVisible = false
            }
            detailed.close()
        }
    }

    private fun updateData() {
        listenerRegistration = fireStore.collection("detailed")
            .addSnapshotListener { querySnapshot, _ ->
                val items: List<Detailed> =
                    querySnapshot?.toObjects(Detailed::class.java) ?: mutableListOf()

                for (i in items) {
                    val detailed =
                        db.rawQuery("SELECT * FROM detaileds WHERE id LIKE '${i.id}'", null)
                    if (detailed.count != 0)
                        db.execSQL("UPDATE detaileds SET content = '${i.content}' WHERE id LIKE '${i.id}'")
                    detailed.close()
                }
                showDetailed()
            }
    }

    override fun onDestroy() {
        super.onDestroy()

        listenerRegistration.remove()
        db.close()
    }
}
