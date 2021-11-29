package com.example.focusskill

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ankushgrover.hourglass.Hourglass

class PlayActivity : AppCompatActivity() {
    private var timer: CountDownTimer? = null
    private var currentNumber: String? = null
    private var originalNumber: String? = null
    private var textView: TextView? = null
    private var pauseFrameLayout: FrameLayout? = null
    private var array = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    private var heartNumbers = 3
    private var handler: Handler? = null
    private var resumeHandler: Handler? = null
    private var runnable: Runnable? = null
    private var resumeRunnable: Runnable? = null
    private var isTimeRunning: Boolean? = true;
    private var firstHartIv: ImageView? = null;
    private var secondHartIv: ImageView? = null;
    private var thirdHartIv: ImageView? = null;
    private var isClicked: Boolean = false;
    private var currentCountTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        handler = Handler(mainLooper)
        resumeHandler = Handler(mainLooper)

        originalNumber = intent.getStringExtra("keyIdentifier")
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

    private fun clickListener() {
        textView?.setOnClickListener {
            if (originalNumber == textView?.text) {
                reduceHearts()
            }
            isClicked = true
        }

        pauseFrameLayout?.setOnClickListener {
            isTimeRunning = if (isTimeRunning == true) {
                pauseTimer()
                false
            } else {
                startResumeRunnable()
                true
            }
        }
    }

    private fun startResumeRunnable() {
        resumeRunnable = Runnable {
            startResumeTimer()
        }
        startResumeHandler()
    }

    private fun startResumeHandler() {
        resumeHandler!!.postDelayed(resumeRunnable!!, 3000)
    }

    private fun startResumeTimer() {
        timer = object : CountDownTimer(currentCountTime, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                displayNumber()
                isClickedOrNot()
                currentCountTime = millisUntilFinished
                isClicked = false
            }

            override fun onFinish() {
                isTimeRunning = false
                goToResult("Great you win this time Want to try again?")
            }

        }.start()
        isTimeRunning = true
    }


    private fun startRunnable() {
        runnable = Runnable {
            startTimer()
        }

        startHandler()
    }

    private fun startHandler() {
        handler!!.postDelayed(runnable!!, 3000)
    }

    @SuppressLint("NewApi")
    private fun calculateDisplayHeight() {
        var height : Int = display!!.height
        pauseFrameLayout?.minimumHeight = (height/5)
    }

    private fun checkHeartsNumber() {
        if (heartNumbers == 3) {
            setThreeHearts()
        }
    }

    private fun reduceHearts() {
        vibrate()
        if (heartNumbers > 0) {
            heartNumbers--
        }
        if (heartNumbers == 1) {
            setOneHeart()
        }

        if (heartNumbers == 2) {
            setTwoHearts()
        }

        if (heartNumbers == 3) {
            setThreeHearts()
        }

        if(heartNumbers == 0) {
            setZeroHeart()
            goToResult("loose")
            isTimeRunning = false
        }


    }

    private fun setZeroHeart() {
        firstHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
        secondHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
        thirdHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
    }

    private fun setThreeHearts() {
        firstHartIv?.setImageResource(R.drawable.ic_hart_filled)
        secondHartIv?.setImageResource(R.drawable.ic_hart_filled)
        thirdHartIv?.setImageResource(R.drawable.ic_hart_filled)
    }

    private fun setTwoHearts() {
        firstHartIv?.setImageResource(R.drawable.ic_hart_filled)
        secondHartIv?.setImageResource(R.drawable.ic_hart_filled)
        thirdHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
    }

    private fun setOneHeart() {
        firstHartIv?.setImageResource(R.drawable.ic_hart_filled)
        secondHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
        thirdHartIv?.setImageResource(R.drawable.ic_hart_un_fill)
    }

    private fun pauseTimer() {
        timer?.cancel()
        isTimeRunning = false;
    }

    private fun startTimer() {
        timer = object : CountDownTimer(1000 * 30 * 60, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                displayNumber()
                isClickedOrNot()
                currentCountTime = millisUntilFinished
                isClicked = false
            }

            override fun onFinish() {
                isTimeRunning = false
                goToResult("Great you win this time Want to try again?")
            }

        }.start()
        isTimeRunning = true

    }

    private fun isClickedOrNot() {
        if (!isClicked) {
            if (originalNumber + "" != textView?.text) {
                reduceHearts()
            }
        }
    }


    override fun onRestart() {
        super.onRestart()
        heartNumbers = 3
    }

    private fun displayNumber() {
        currentNumber = array.random().toString()
        textView?.text = currentNumber
    }


    fun goToResult(message: String) {
        timer?.cancel()
        stopHandler()
        stopResumeHandler()
        val newIntent = Intent(this, ResultActivity::class.java)
        newIntent.putExtra("Message", message)
        newIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(newIntent)
    }
    private fun vibrate(){
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

    private fun stopHandler() {
        handler?.removeCallbacksAndMessages(null)
    }

    private fun stopResumeHandler() {
        resumeHandler?.removeCallbacksAndMessages(null)
    }

}