package com.example.focusskill

import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ankushgrover.hourglass.Hourglass

class PlayActivity : AppCompatActivity() {
    private var timer: CountDownTimer? = null
    private var curretNumber: String? = null
    private var orignialNumber: String? = null
    private var textView: TextView? = null
    private var array = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    private var heartNumbers = 4
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var pauseBtn: Button? = null;
    private var hourglass: Hourglass? = null;
    private var isTimeRunning: Boolean? = true;
    private var firstHartIv: ImageView? = null;
    private var secondHartIv: ImageView? = null;
    private var thirdHartIv: ImageView? = null;
    private var isClicked: Boolean = false;
    private var numbers: List<Int> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        handler = Handler()

        orignialNumber = intent.getStringExtra("keyIdentifier")
        textView = findViewById(R.id.currentNumber)
        pauseBtn = findViewById(R.id.pauseTimerBtn)
        firstHartIv = findViewById(R.id.firstHartIv)
        secondHartIv = findViewById(R.id.secondHartIv)
        thirdHartIv = findViewById(R.id.thirdHartIv)

        clickListner()
        startTimer()
        checkHeartsNumber()


    }

    private fun checkHeartsNumber() {
        if (heartNumbers == 3) {
            firstHartIv?.setImageResource(R.drawable.ic_hart_filled)
            secondHartIv?.setImageResource(R.drawable.ic_hart_filled)
            thirdHartIv?.setImageResource(R.drawable.ic_hart_filled)
        }
    }

    private fun startRunnable() {
       handler = Handler(mainLooper);
        handler?.postDelayed({
            if (!isClicked) {
                if (orignialNumber!=curretNumber) {
                    reduceHearts()
                }
            }
        }, 3000)
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

    private fun startHandler() {
        handler?.postDelayed(runnable!!, 3000.toLong())
    }

    private fun clickListner() {
        textView?.setOnClickListener {
            if(orignialNumber == curretNumber){
                reduceHearts()
            }
            isClicked = true
        }

        pauseBtn?.setOnClickListener {
            if (isTimeRunning == true) {
                pauseBtn?.text = "ResumeTimer"
                pauseTimer()
            }else{
                timer?.start()
                pauseBtn?.text = "PauseTimer"
            }
        }
    }

    private fun pauseTimer() {
        stopHandler()
        timer?.cancel()
        isTimeRunning = false;

    }

    private fun stopHandler() {
        handler?.removeCallbacksAndMessages(null)
    }


    private fun randomNumber() {
        //1000*30*60   300
        startRunnable()
        hourglass = object : Hourglass(1000*30*60, 3000) {
            override fun onTimerTick(timeRemaining: Long) {

                if (!isClicked) {
                    //reduceHearts()
                    hourglass = null
                }
                displayNumber()

                isClicked = false
            }

            override fun onTimerFinish() {
                goToResult("Great you win this time Want to try again?")
            }

        }

        (hourglass as Hourglass).startTimer()

    }

    private fun startTimer() {
        //1000*30*60   300
        timer = object : CountDownTimer(1000 * 30 * 60, 3000) {
            override fun onTick(millisUntilFinished: Long) {

                if (!isClicked) {
                    if (orignialNumber != curretNumber)
                        reduceHearts()
                }
                displayNumber()
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
        heartNumbers = 4
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