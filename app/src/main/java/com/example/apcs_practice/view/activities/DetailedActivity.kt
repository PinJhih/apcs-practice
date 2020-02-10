package com.example.apcs_practice.view.activities

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
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

        intent?.extras?.let {
            val id = it.getString("detailedId")!!
            showDetailed(id)
        }
    }

    private fun showDetailed(id: String) {

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
        if(detailed.count ==0){
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
        }else{
            detailed.moveToFirst()
            tv_content.text = detailed.getString(1)
        }
        detailed.close()
    }

    override fun onDestroy() {
        super.onDestroy()

        db.close()
    }
}
