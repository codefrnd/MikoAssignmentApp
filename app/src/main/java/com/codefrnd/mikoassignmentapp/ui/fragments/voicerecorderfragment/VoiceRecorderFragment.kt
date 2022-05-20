package com.codefrnd.mikoassignmentapp.ui.fragments.voicerecorderfragment

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.fragment.app.Fragment
import com.codefrnd.mikoassignmentapp.R
import kotlinx.android.synthetic.main.fragment_voice_recorder.*
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.io.IOException


private const val TAG = "VoiceRecorderFragment"

class VoiceRecorderFragment : Fragment() {

    var recorder: MediaRecorder? = null
    private var fileName: String = ""
    private var player: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_voice_recorder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fileName = "${requireContext().externalCacheDir?.absolutePath}/audiorecordtest.3gp"

        startRecordBtn.setOnClickListener {
            startRecording()
            val timer = object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    record_time_tv.text = "${millisUntilFinished / 1000}"
                }

                override fun onFinish() {
                    stopRecording()
                    tv_record.visibility = View.GONE
                    record_time_tv.visibility = View.GONE
                }
            }
            timer.start()
        }

        playRecordBtn.setOnClickListener {
            startPlaying()
            val timer = object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    record_time_tv.text = "${millisUntilFinished / 1000}"
                }

                override fun onFinish() {
                    startRecordBtn.isEnabled = true
                    playRecordBtn.isEnabled = true
                    tv_record.visibility = View.GONE
                    record_time_tv.visibility = View.GONE
                }
            }
            timer.start()
        }
    }

    private fun startRecording() {
        startRecordBtn.isEnabled = false
        playRecordBtn.isEnabled = false
        tv_record.text = "Recording..."
        tv_record.visibility = View.VISIBLE
        record_time_tv.visibility = View.VISIBLE

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(TAG, "prepare() failed")
            }
            start()
        }
    }

    private fun stopRecording() {
        startRecordBtn.isEnabled = true
        playRecordBtn.isEnabled = true

        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    private fun startPlaying() {
        startRecordBtn.isEnabled = false
        playRecordBtn.isEnabled = false
        tv_record.text = "Playing..."
        tv_record.visibility = View.VISIBLE
        record_time_tv.visibility = View.VISIBLE

        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(TAG, "prepare() failed")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }
}