package com.example.focusskill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.time.Instant

class ResultActivity : AppCompatActivity() {
    var incomingIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result2)

        val restartbtn = findViewById<Button>(R.id.restartBtn)
        val message = findViewById<TextView>(R.id.resultMessage)

        message.text = incomingIntent?.getStringExtra("Message")
        restartbtn.setOnClickListener {
            goToHome()
        }

    }
    fun goToHome(){
        val myIntent = Intent(this,MainActivity::class.java)
        startActivity(myIntent)
    }

}