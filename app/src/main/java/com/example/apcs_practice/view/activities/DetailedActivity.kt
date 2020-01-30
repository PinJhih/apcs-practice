package com.example.apcs_practice.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.core.view.isVisible
import com.example.apcs_practice.R
import com.example.apcs_practice.models.Detailed
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detailed.*

class DetailedActivity : AppCompatActivity() {

    private lateinit var fireStore:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        fireStore = FirebaseFirestore.getInstance()

        intent?.extras?.let {
            val id = it.getString("detailedId")!!
            showDetailed(id)
        }
    }

    private fun showDetailed(id:String){

        val handler = Handler(Handler.Callback { msg->
            when (msg.what){
                1 -> {
                    layout_loading.isVisible = false
                }
            }
            true
        })

        layout_loading.isVisible = true
        fireStore.collection("detailed").document(id)
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                Thread(Runnable {
                    val detailed = documentSnapshot.toObject(Detailed::class.java)!!
                    tv_content.text = detailed.content
                    Thread.sleep(300)
                    val msg = Message()
                    msg.what = 1
                    handler.sendMessage(msg)
                }).start()
            }
    }
}
