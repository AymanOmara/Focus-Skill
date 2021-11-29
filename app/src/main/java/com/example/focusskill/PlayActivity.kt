package com.example.focusskill

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PlayActivity : AppCompatActivity() {
    private var timer: CountDownTimer? = null
    private var fullTimer: CountDownTimer? = null
    private var resumeTimer: CountDownTimer? = null
    private var curretNumber: String? = null
    private var orignialNumber: String? = null
    private var textView: TextView? = null
    private var pauseFrameLayout : FrameLayout?= null
    private var array = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    private var heartNumbers = 3
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var isTimeRunning: Boolean? = true;
    private var firstHartIv: ImageView? = null;
    private var secondHartIv: ImageView? = null;
    private var thirdHartIv: ImageView? = null;
    private var isClicked: Boolean = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        handler = Handler()

        orignialNumber = intent.getStringExtra("keyIdentifier")
        textView = findViewById(R.id.currentNumber)
        firstHartIv = findViewById(R.id.firstHartIv)
        secondHartIv = findViewById(R.id.secondHartIv)
        thirdHartIv = findViewById(R.id.thirdHartIv)
        pauseFrameLayout = findViewById(R.id.pauseFrameLayout)

        calculateDisplayHeight()
        clickListener()
        displayNumber()
        checkHeartsNumber()

        startRunnable()

    }

    private fun startRunnable() {
        runnable = Runnable {
            startTimer()
        }

        handler!!.postDelayed(runnable!!, 3000)
    }

    @SuppressLint("NewApi")
    private fun calculateDisplayHeight() {
        var height : Int = display!!.height

        pauseFrameLayout?.minimumHeight = (height/5)
    }

    private fun checkHeartsNumber() {
        if (heartNumbers == 3) {
            firstHartIv?.setImageResource(R.drawable.ic_hart_filled)
            secondHartIv?.setImageResource(R.drawable.ic_hart_filled)
            thirdHartIv?.setImageResource(R.drawable.ic_hart_filled)
        }
    }

    private fun reduceHearts() {
        vibrate()
        if (heartNumbers > 0) {
            heartNumbers--
        }
        if (heartNumbers == 1) {
            firstHartIv?.setImageResource(R.drawable.ic_hart_filled)
            secondHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
            thirdHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
        }

        if (heartNumbers == 2) {
            firstHartIv?.setImageResource(R.drawable.ic_hart_filled)
            secondHartIv?.setImageResource(R.drawable.ic_hart_filled)
            thirdHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
        }

        if (heartNumbers == 3) {
            firstHartIv?.setImageResource(R.drawable.ic_hart_filled)
            secondHartIv?.setImageResource(R.drawable.ic_hart_filled)
            thirdHartIv?.setImageResource(R.drawable.ic_hart_filled)
        }

        if(heartNumbers == 0) {
            firstHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
            secondHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
            thirdHartIv?.setImageResource(R.drawable.ic_hart_un_fill)


            runnable = null
            handler = null
            timer?.cancel()
            goToResult("loose")
        }


    }

    private fun clickListener() {
        textView?.setOnClickListener {
            if (orignialNumber == textView?.text) {
                reduceHearts()
            }
            isClicked = true
        }

        pauseFrameLayout?.setOnClickListener {
            if (isTimeRunning == true) {
                pauseTimer()
            } else {
               startRunnable()
            }
        }
    }

    private fun pauseTimer() {
        timer?.cancel()
        isTimeRunning = false;
    }


    private fun startTimer() {
        //1000*30*60   300
        timer = object : CountDownTimer(1000 * 30 * 60, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                displayNumber()
                if (!isClicked) {
                    if (orignialNumber+ "" != textView?.text) {
                        reduceHearts()
                    }
                }
                isClicked = false
            }

            override fun onFinish() {
                goToResult("Great you win this time Want to try again?")
            }

        }.start()
        isTimeRunning = true

    }


    override fun onRestart() {
        super.onRestart()
        heartNumbers = 3
    }

    private fun displayNumber() {
        curretNumber = array.random().toString()
        textView?.text = curretNumber
    }



    fun goToResult(message: String) {
        val newIntent = Intent(this, ResultActivity::class.java)
        newIntent.putExtra("Message", message)
        newIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(newIntent)
        finish()
    }
    private fun vibrate(){
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
}