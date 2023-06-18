package com.example.mediaplayerproject

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar = findViewById(R.id.sb_audio)
        handler = Handler(Looper.getMainLooper())
        val playButton = findViewById<FloatingActionButton>(R.id.fb_play)
        playButton.setOnClickListener {
            if(mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(this,R.raw.clap_sound)
                seekBarInt()
            } else {
                mediaPlayer?.start()
            }
        }
        val pauseButton = findViewById<FloatingActionButton>(R.id.fb_pause)
        pauseButton.setOnClickListener {
            if(mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(this,R.raw.clap_sound)
            } else {
                mediaPlayer?.pause()
            }
        }
        val stopButton = findViewById<FloatingActionButton>(R.id.fb_stop)
        stopButton.setOnClickListener {
            if(mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(this,R.raw.clap_sound)
            } else {
                mediaPlayer?.stop()
                mediaPlayer?.reset()
                mediaPlayer?.release()
                mediaPlayer = null
                handler.removeCallbacks(runnable)
                seekBar.progress=0
            }
        }
    }
    private fun seekBarInt(){
        val tvPlayed = findViewById<TextView>(R.id.tvPlayed)
        val tvDue = findViewById<TextView>(R.id.tvDue)
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mediaPlayer?.seekTo(progress)
                val playedTime = mediaPlayer!!.currentPosition/1000
                val totalDuration = mediaPlayer!!.duration/1000
                val dueTime = totalDuration- playedTime
                tvPlayed.text = "$playedTime sec"
                tvDue.text = "$dueTime sec"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

        } )
        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition
            handler.postDelayed(runnable,1000)
        }
        handler.postDelayed(runnable,1000)
    }
}