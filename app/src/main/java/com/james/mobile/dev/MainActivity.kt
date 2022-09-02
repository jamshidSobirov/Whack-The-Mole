package com.james.mobile.dev

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.View.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var myScore = 0
    var varRandMole = 0
    private var mTimeView: TextView? = null
    private var mScoreView: TextView? = null
    private var varLives = 5
    var handler = Handler(Looper.myLooper()!!)
    var varClose = false
    private val maxTime = 60 * 1000
    private val stepTime = (1 * 1000).toLong()
    var timeInterval = 1000
    var moleUpTime = 350
    var mTimer: CountDownTimer = MyTimer(maxTime, stepTime)
    var molesClick = arrayOfNulls<ImageView>(9)
    var yValue = 0f

    lateinit var adView: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTimeView = findViewById<View>(R.id.textTimeVal) as TextView
        mScoreView = findViewById<View>(R.id.textScoreVal) as TextView
        adView = findViewById(R.id.bannerAdView)
        MobileAds.initialize(applicationContext)
        setUpAdView()

        varClose = false

        molesClick[0] = findViewById<View>(R.id.imageMole1) as ImageView
        molesClick[1] = findViewById<View>(R.id.imageMole2) as ImageView
        molesClick[2] = findViewById<View>(R.id.imageMole3) as ImageView
        molesClick[3] = findViewById<View>(R.id.imageMole4) as ImageView
        molesClick[4] = findViewById<View>(R.id.imageMole5) as ImageView
        molesClick[5] = findViewById<View>(R.id.imageMole6) as ImageView
        molesClick[6] = findViewById<View>(R.id.imageMole7) as ImageView
        molesClick[7] = findViewById<View>(R.id.imageMole8) as ImageView
        molesClick[8] = findViewById<View>(R.id.imageMole9) as ImageView
        yValue = 1f
    }

    inner class MyTimer(maxTime: Int, stepTime: Long) :
        CountDownTimer(maxTime.toLong(), stepTime) {
        override fun onFinish() {
            mTimer.cancel()
            handler.removeCallbacks(moleLoop)
            var lose = false
            if (varLives <= 0) {
                varLives = 0
                lose = true
                playGameOverSound()
            } else {
                playWinGameSound()
            }

            MyDialog.showEndDialog(
                this@MainActivity,
                RelativeLayout(this@MainActivity),
                object : MyDialog.OnRestartPressed {
                    override fun onRestartPressed() {
                        cancel()
                        varLives = 5
                        updateLives(varLives)
                        myScore = 0
                        updateScore(myScore)
                        timeInterval = 1000
                        moleUpTime = 350
                        mTimer.start()
                        handler.post(moleLoop)

                        setUpAdView()
                    }
                },
                varLives,
                myScore,
                lose
            )

        }

        override fun onTick(millisUntilFinished: Long) {
            mTimeView?.text = (millisUntilFinished / 1000).toString()
            if (varLives <= 0) {
                runOnUiThread {
                    playGameOverSound()
                }
                mTimer.cancel()
                handler.removeCallbacks(moleLoop)

                runOnUiThread {
                    MyDialog.showEndDialog(
                        this@MainActivity,
                        RelativeLayout(this@MainActivity),
                        object : MyDialog.OnRestartPressed {
                            override fun onRestartPressed() {
                                varLives = 5
                                updateLives(varLives)
                                myScore = 0
                                updateScore(myScore)
                                timeInterval = 1000
                                moleUpTime = 350
                                mTimer.start()
                                handler.post(moleLoop)

                                setUpAdView()
                            }
                        },
                        0,
                        myScore,
                        true
                    )
                }
            }
        }
    }


    private var moleLoop: Runnable = object : Runnable {
        var varPrevRandMole = 10

        override fun run() {
            varRandMole = Random().nextInt(8)
            if (varRandMole == varPrevRandMole) {
                do varRandMole = Random().nextInt(8) while (varRandMole == varPrevRandMole)
            }
            varPrevRandMole = varRandMole
            molesClick[varRandMole]!!.animate().scaleX(1f).scaleY(1f).duration =
                100L

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (!varClose) {
                        for (i in 0..8) {
                            if (molesClick[i]!!.scaleX == yValue) {
                                runOnUiThread {
                                    molesClick[i]!!.animate().scaleX(0f).scaleY(0f).duration = 10
                                    playWrongSound()
                                }

                                varLives -= 1
                                updateLives(varLives)
                            }
                        }
                    }
                }
            }, timeInterval.toLong())

            if (!varClose) {
                handler.postDelayed(this, timeInterval.toLong())
            }
        }
    }

    private fun updateScore(Score: Int) {
        mScoreView!!.text = Score.toString()
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.imageMole1 -> if (molesClick[0]!!.scaleX > 0.8f) {
                molesClick[0]!!.animate().scaleX(0f).scaleY(0f).duration = 30
                directHit()
            }
            R.id.imageMole2 -> if (molesClick[1]!!.scaleX > 0.8f) {
                molesClick[1]!!.animate().scaleX(0f).scaleY(0f).duration = 30
                directHit()
            }
            R.id.imageMole3 -> if (molesClick[2]!!.scaleX > 0.8f) {
                molesClick[2]!!.animate().scaleX(0f).scaleY(0f).duration = 30
                directHit()
            }
            R.id.imageMole4 -> if (molesClick[3]!!.scaleX > 0.8f) {
                molesClick[3]!!.animate().scaleX(0f).scaleY(0f).duration = 30
                directHit()
            }
            R.id.imageMole5 -> if (molesClick[4]!!.scaleX > 0.8f) {
                molesClick[4]!!.animate().scaleX(0f).scaleY(0f).duration = 30
                directHit()
            }
            R.id.imageMole6 -> if (molesClick[5]!!.scaleX > 0.8f) {
                molesClick[5]!!.animate().scaleX(0f).scaleY(0f).duration = 30
                directHit()
            }
            R.id.imageMole7 -> if (molesClick[6]!!.scaleX > 0.8f) {
                molesClick[6]!!.animate().scaleX(0f).scaleY(0f).duration = 30
                directHit()
            }
            R.id.imageMole8 -> if (molesClick[7]!!.scaleX > 0.8f) {
                molesClick[7]!!.animate().scaleX(0f).scaleY(0f).duration = 30
                directHit()
            }
            R.id.imageMole9 -> if (molesClick[8]!!.scaleX > 0.8f) {
                molesClick[8]!!.animate().scaleX(0f).scaleY(0f).duration = 30
                directHit()
            }
        }
    }

    private fun directHit() {
        myScore += 1
        timeInterval -= 6
        updateScore(myScore)
        playSound()

    }

    override fun onPause() {
        super.onPause()
        varClose = true
        mTimer.cancel()
        handler.removeCallbacks(moleLoop)
        varLives = 5
        updateLives(varLives)
        myScore = 0
        updateScore(myScore)
        timeInterval = 1000
        moleUpTime = 350
    }

    override fun onStop() {
        super.onStop()
        varClose = true
        mTimer.cancel()
        handler.removeCallbacks(moleLoop)
        varLives = 5
        updateLives(varLives)
        myScore = 0
        updateScore(myScore)
        timeInterval = 1000
        moleUpTime = 350
    }

    override fun onResume() {
        super.onResume()
        varClose = false
        MyDialog.showStartDialog(this, RelativeLayout(this), object : MyDialog.OnStartPressed {
            override fun onStartPressed() {
                mTimer.start()
                handler.post(moleLoop)
            }
        })
    }

    private fun playSound() {
        var mPlayerWhack = MediaPlayer.create(applicationContext, R.raw.whack);
        mPlayerWhack.setOnCompletionListener { mp ->
            mp.reset()
            mp.release()
            mPlayerWhack = null
        }
        mPlayerWhack.start()
    }

    private fun playWrongSound() {
        var mPlayerWhack = MediaPlayer.create(applicationContext, R.raw.wrong);
        mPlayerWhack.setOnCompletionListener { mp ->
            mp.reset()
            mp.release()
            mPlayerWhack = null
        }
        mPlayerWhack.start()
    }

    private fun playGameOverSound() {
        var mPlayerWhack = MediaPlayer.create(applicationContext, R.raw.game_over);
        mPlayerWhack.setOnCompletionListener { mp ->
            mp.reset()
            mp.release()
            mPlayerWhack = null
        }
        mPlayerWhack.start()
    }

    private fun playWinGameSound() {
        var mPlayerWhack = MediaPlayer.create(applicationContext, R.raw.win_game);
        mPlayerWhack.setOnCompletionListener { mp ->
            mp.reset()
            mp.release()
            mPlayerWhack = null
        }
        mPlayerWhack.start()
    }

    fun updateLives(Lives: Int) {
        val heart1 = findViewById<View>(R.id.imageHeart1) as ImageView
        val heart2 = findViewById<View>(R.id.imageHeart2) as ImageView
        val heart3 = findViewById<View>(R.id.imageHeart3) as ImageView
        val heart4 = findViewById<View>(R.id.imageHeart4) as ImageView
        val heart5 = findViewById<View>(R.id.imageHeart5) as ImageView

        if (Lives == 5) {
            runOnUiThread {
                heart5.setImageResource(R.drawable.placeholder_heart)
                heart4.setImageResource(R.drawable.placeholder_heart)
                heart3.setImageResource(R.drawable.placeholder_heart)
                heart2.setImageResource(R.drawable.placeholder_heart)
                heart1.setImageResource(R.drawable.placeholder_heart)
            }
        }
        when (Lives) {
            4 -> {
                runOnUiThread {
                    heart5.setImageResource(R.drawable.placeholder_heart_empty)
                }
                varLives = 4

            }
            3 -> {
                runOnUiThread {
                    heart4.setImageResource(R.drawable.placeholder_heart_empty)
                }
                varLives = 3
            }
            2 -> {
                runOnUiThread {
                    heart3.setImageResource(R.drawable.placeholder_heart_empty)
                }
                varLives = 2
            }
            1 -> {
                runOnUiThread {
                    heart2.setImageResource(R.drawable.placeholder_heart_empty)
                }
                varLives = 1
            }
            0 -> {
                runOnUiThread {
                    heart1.setImageResource(R.drawable.placeholder_heart_empty)
                }
                varLives = 0
            }
        }
    }

    private fun setUpAdView() {
        val ad = AdView(this)
        ad.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                adView.visibility = INVISIBLE

            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                adView.visibility = VISIBLE
            }
        }

        ad.also {
            val adRequest = AdRequest.Builder().build()
            it.setAdSize(adSize)
//            it.adUnitId = "ca-app-pub-5110564680408312/9374556161"
            it.adUnitId = "ca-app-pub-3940256099942544/6300978111"
            it.loadAd(adRequest)
            adView.addView(it)
        }


    }

    private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = adView.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }


}