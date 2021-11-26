package com.example.focusskill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
var array  = intArrayOf(0,1, 2, 3, 4, 5,6,7,8,9)
    var textView:TextView? = null
    var myIntent:Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button:Button = findViewById(R.id.button)
        textView = findViewById(R.id.NumberTV)

        myIntent =  Intent(this,PlayActivity::class.java )
        textView?.text = array.random().toString()
        myIntent?.putExtra("keyIdentifier", textView?.text.toString())


        button.setOnClickListener {
            startActivity(myIntent)
        }
    }
    override fun onRestart() {
        super.onRestart()
        textView?.text = array.random().toString()
        myIntent?.putExtra("keyIdentifier", textView?.text.toString())
    }
}