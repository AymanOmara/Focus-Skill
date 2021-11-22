package com.example.focusskill

import android.content.Context
import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast

class PlayActivity : AppCompatActivity() {
    private var timer: CountDownTimer? = null
    private var myIntent: Intent? = null
    private var curretNumber: String? = null
    private var orignialNumber: String? = null
    private var textView: TextView? = null
    private var array = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    private var heartNumbers = 4
    private var handler: Handler? = null
    private var runnable: Runnable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        myIntent = intent
        handler = Handler()

        orignialNumber = myIntent?.getStringExtra("keyIdentifier")
        textView = findViewById(R.id.currentNumber)

        randomNumber()
        clickListner()
    }
    private fun startRunnable(){
        runnable = Runnable {
            if (orignialNumber != curretNumber) {
                reduceHearts()
                startHandler()
            }
        }
    }
    private fun reduceHearts() {
        vibrate()
        if(heartNumbers >0){
            heartNumbers--

        }
        Toast.makeText(this, "You lost one heart${heartNumbers}", Toast.LENGTH_SHORT).show()
        if(heartNumbers == 0) {
            stopHandler()
            Toast.makeText(this, "You lost the game", Toast.LENGTH_SHORT).show()
            handler = null
            timer = null
            goToResult("You loose")
        }
    }

    private fun startHandler() {
        handler?.postDelayed(runnable!!, 3000.toLong())
    }
    
    private fun clickListner() {
        textView?.setOnClickListener {
            if(orignialNumber == curretNumber){
                reduceHearts()
                Toast.makeText(this, "lost one heart ", Toast.LENGTH_SHORT).show()
            }
            stopHandler()

        }
    }

    private fun stopHandler() {
        handler?.removeCallbacks(runnable!!)
    }


    fun randomNumber() {
        //1000*30*60   300
        startRunnable()
        timer = object : CountDownTimer(1000 * 30 * 60, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                displayNumber()
                startHandler()

                checkIfTheNumbermatches()
            }
            override fun onFinish() {
                goToResult("Great you win this time Want to try again?")
            }

        }.start()
    }
    override fun onRestart() {
        super.onRestart()
        heartNumbers = 3
    }

    private fun checkIfTheNumbermatches() {
        if (orignialNumber == curretNumber) {
            reduceHearts()
        }
    }

    private fun displayNumber() {
        curretNumber = array.random().toString()
        textView?.text = curretNumber
    }



    fun goToResult(message: String) {
        val newIntent = Intent(this, ResultActivity::class.java)
        newIntent.putExtra("Message", message)
        startActivity(newIntent)
        finish()
    }
    private fun vibrate(){
        val vibrator = this?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
}