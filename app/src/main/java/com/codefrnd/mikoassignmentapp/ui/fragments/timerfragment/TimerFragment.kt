package com.codefrnd.mikoassignmentapp.ui.fragments.timerfragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.codefrnd.mikoassignmentapp.R
import com.codefrnd.mikoassignmentapp.data.preference.PreferenceProvider
import kotlinx.android.synthetic.main.fragment_timer.*


private const val TAG = "TimerFragment"
private const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
private const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
private const val CAMERA = Manifest.permission.CAMERA

class TimerFragment : Fragment() {

    val timerHandler: Handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        camera_btn.setOnClickListener {
            if (checkCameraPermissions()) {
                val action = TimerFragmentDirections.actionTimerFragmentToCameraFragment()
                Navigation.findNavController(it).navigate(action)
            } else {
                requestCameraPermissions.launch(
                    arrayOf(
                        CAMERA,
                        RECORD_AUDIO,
                        WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }

        record_btn.setOnClickListener {
            if (checkRecorderPermissions()) {
                val action = TimerFragmentDirections.actionTimerFragmentToVoiceRecorderFragment()
                Navigation.findNavController(it).navigate(action)
            } else {
                requestRecorderPermissions.launch(
                    arrayOf(
                        RECORD_AUDIO,
                        WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    private val mTickRunnable: Runnable = object : Runnable {
        override fun run() {
            runtime_tv.text = "${PreferenceProvider(requireContext()).getRunTime()}"
            timerHandler.postDelayed(this, 1000)
        }
    }

    private val requestCameraPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[CAMERA] == true
                && permissions[RECORD_AUDIO] == true
                && permissions[WRITE_EXTERNAL_STORAGE] == true
            ) {
                val action = TimerFragmentDirections.actionTimerFragmentToCameraFragment()
                Navigation.findNavController(camera_btn).navigate(action)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Accept camera and storage permissions",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private val requestRecorderPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[RECORD_AUDIO] == true
                && permissions[WRITE_EXTERNAL_STORAGE] == true
            ) {
                val action = TimerFragmentDirections.actionTimerFragmentToCameraFragment()
                Navigation.findNavController(camera_btn).navigate(action)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Accept camera and storage permissions",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun checkCameraPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.RECORD_AUDIO
        )
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun checkRecorderPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.RECORD_AUDIO
        )
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    override fun onStop() {
        super.onStop()
        timerHandler.removeCallbacks(mTickRunnable)
    }

    override fun onStart() {
        super.onStart()
        timerHandler.postDelayed(mTickRunnable, 0)
    }
}