package com.example.focusskill

import android.content.Context
import android.content.Intent
import android.os.*
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.ankushgrover.hourglass.Hourglass
import java.util.*

class PlayActivity : AppCompatActivity() {
    private var timer: CountDownTimer? = null
    private var curretNumber: String? = null
    private var orignialNumber: String? = null
    private var textView: TextView? = null
    private var timerTv: TextView? = null
    private var array = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    private var heartNumbers = 4
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var pauseBtn:Button?=null;
    private var hourglass: Hourglass?=null;
    private var isTimeRunning : Boolean?=true;
    private var firstHartIv : ImageView?=null;
    private var secondHartIv : ImageView?=null;
    private var thirdHartIv : ImageView?=null;
    private var fourHartIv : ImageView?=null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        handler = Handler()

        orignialNumber = intent.getStringExtra("keyIdentifier")
        textView = findViewById(R.id.currentNumber)
        timerTv = findViewById(R.id.timerTv)
        pauseBtn = findViewById(R.id.pauseTimerBtn)
        firstHartIv = findViewById(R.id.firstHartIv)
        secondHartIv = findViewById(R.id.secondHartIv)
        thirdHartIv = findViewById(R.id.thirdHartIv)
        fourHartIv = findViewById(R.id.fourHartIv)
        clickListner()
        randomNumber()
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
        if (heartNumbers == 1) {
            firstHartIv?.setImageResource(R.drawable.ic_hart_filled)
            secondHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
            thirdHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
            fourHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
        }

        if (heartNumbers == 2) {
            firstHartIv?.setImageResource(R.drawable.ic_hart_filled)
            secondHartIv?.setImageResource(R.drawable.ic_hart_filled)
            thirdHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
            fourHartIv?.setImageResource(R.drawable.ic_hart_un_fill)

        }

        if (heartNumbers == 3) {
            firstHartIv?.setImageResource(R.drawable.ic_hart_filled)
            secondHartIv?.setImageResource(R.drawable.ic_hart_filled)
            thirdHartIv?.setImageResource(R.drawable.ic_hart_filled)
            fourHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
        }

        if (heartNumbers == 4) {
            firstHartIv?.setImageResource(R.drawable.ic_hart_filled)
            secondHartIv?.setImageResource(R.drawable.ic_hart_filled)
            thirdHartIv?.setImageResource(R.drawable.ic_hart_filled)
            fourHartIv?.setImageResource(R.drawable.ic_hart_filled)
        }

        if(heartNumbers == 0) {
            firstHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
            secondHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
            thirdHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
            fourHartIv?.setImageResource(R.drawable.ic_hart_un_fill)


            stopHandler()
            handler = null
            timer = null
            hourglass = null
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
            }
        }

        pauseBtn?.setOnClickListener {
            if (isTimeRunning == true) {
                pauseTimer()
                pauseBtn?.text = "ResumeTimer"
                stopHandler()
            }else{
                resumeTimer()
                pauseBtn?.text = "PauseTimer"
            }
        }
    }

    private fun pauseTimer() {
        (hourglass as Hourglass).pauseTimer();
        isTimeRunning = false
    }

    private fun resumeTimer() {
        isTimeRunning = true;
        (hourglass as Hourglass).resumeTimer()
    }

    private fun stopHandler() {
        handler?.removeCallbacksAndMessages(null)
    }


    private fun randomNumber() {
        //1000*30*60   300
        startRunnable()
        hourglass = object : Hourglass(1000*30*60, 3000) {
            override fun onTimerTick(timeRemaining: Long) {
                displayNumber()
                startHandler()
                checkIfTheNumbermatches()
                updateCountDownText(timeRemaining);
            }

            override fun onTimerFinish() {
                goToResult("Great you win this time Want to try again?")
            }

        }

        (hourglass as Hourglass).startTimer()

    }

    private fun updateCountDownText(timeLeft:Long) {
        var minutes : Long = (timeLeft / 1000) / 60

        var seconds : Long = (timeLeft/1000) % 60;

        var timeLeftFormatted : String = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds)

        timerTv?.text = timeLeftFormatted
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
        stopHandler()
        hourglass = null
        handler = null
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