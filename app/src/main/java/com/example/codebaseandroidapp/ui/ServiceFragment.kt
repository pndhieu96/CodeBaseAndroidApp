package com.example.codebaseandroidapp.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.codebaseandroidapp.Application
import com.example.codebaseandroidapp.databinding.FragmentServiceBinding
import com.example.codebaseandroidapp.service.DownloadIntentService
import com.example.codebaseandroidapp.service.SongService
import com.example.codebaseandroidapp.utils.ConstantUtils
import com.example.codebaseandroidapp.utils.Utils
import android.content.ComponentName

import android.os.IBinder

import android.content.ServiceConnection
import androidx.room.Delete
import com.example.codebaseandroidapp.service.DeleteBoundService


class ServiceFragment : Fragment() {

    private var binding: FragmentServiceBinding? = null
    private var navController: NavController? = null
    private var mBound = false
    private var mService : DeleteBoundService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServiceBinding.inflate(inflater)
        navController = findNavController(this)
        return binding!!.root
    }

    override fun onStart() {
        super.onStart()

        LocalBroadcastManager.getInstance(Application.getAppContext())
            .registerReceiver(receiver, IntentFilter(DownloadIntentService.DOWNLOAD_COMPLETE))
        LocalBroadcastManager.getInstance(Application.getAppContext())
            .registerReceiver(receiver, IntentFilter(SongService.NOTIFICATION_ACTION))
        val intent = Intent(activity, DeleteBoundService::class.java)
        activity?.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)

        binding?.let { binding ->
            binding.btnDownload.setOnClickListener {
                context?.let {
                    if(binding.progressBar.visibility != VISIBLE) {
                        binding.progressBar.visibility = VISIBLE
                        DownloadIntentService.startActionDownload(it, ConstantUtils.SONG_URL)
                        stopPlaying()
                    }
                }
            }
            binding.btnDelete.setOnClickListener {
                context?.let {
                    if(binding.progressBar.visibility != VISIBLE && mBound) {
                        binding.progressBar.visibility = VISIBLE
//                        Delete file with Intent Service
//                        DownloadIntentService.startActionDelete(it, ConstantUtils.SONG_URL)

                        if(mService?.handleActionDelete(ConstantUtils.SONG_URL) ?: false) {
                            if (Utils.songFile().exists()) {
                                enablePlayButton()
                            } else {
                                disableMediaButtons()
                            }
                        }
                        stopPlaying()
                        binding.progressBar.visibility = GONE
                    }
                }
            }
            binding.btnPlay.setOnClickListener {
                context?.let {

                    //Start a foreground service
                    val intent = Intent(it, SongService::class.java)
                    intent.setAction(SongService.ACTION_CREATE)
                    ContextCompat.startForegroundService(it, intent)
                }
            }
            binding.btnStop.setOnClickListener {
                stopPlaying()
                enablePlayButton()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (Utils.songFile().exists()) {
            if(Application.isPlayingSong) {
                enableStopButton()
            } else {
                enablePlayButton()
            }
        } else {
            disableMediaButtons()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    navController?.popBackStack()
                }
            }
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(Application.getAppContext())
            .unregisterReceiver(receiver)
        activity?.unbindService(mConnection)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ServiceFragment().apply {

            }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getStringExtra("ACTION")?.let{
                if(Utils.songFile().exists()) {
                    if(Application.isPlayingSong) {
                        enableStopButton()
                    } else {
                        enablePlayButton()
                    }
                } else {
                    disableMediaButtons()
                }
            }

            if(intent == null || intent!!.getStringExtra("ACTION").isNullOrEmpty()) {
                val param = intent?.getStringExtra(DownloadIntentService.DOWNLOAD_COMPLETE_KEY)
                Log.i("ServiceFragment", "Receiver broadcast for $param")
                if (Utils.songFile().exists()) {
                    enablePlayButton()
                }
            }
            binding?.progressBar?.visibility = GONE
        }
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            mService = (service as DeleteBoundService.DeleteBoundServiceBinder).getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    private fun stopPlaying() {
        activity?.let {
            it.stopService(Intent(context, SongService::class.java))
        }
    }

    private fun enablePlayButton() {
        binding?.let {
            enableButton(true, it.btnPlay)
            enableButton(false, it.btnStop)
            enableButton(false, it.btnDownload)
            enableButton(true, it.btnDelete)
        }
    }

    private fun enableStopButton() {
        binding?.let {
            enableButton(false, it.btnPlay)
            enableButton(true, it.btnStop)
            enableButton(false, it.btnDownload)
            enableButton(true, it.btnDelete)
        }
    }

    private fun disableMediaButtons() {
        binding?.let {
            enableButton(false, it.btnPlay)
            enableButton(false, it.btnStop)
            enableButton(true, it.btnDownload)
            enableButton(false, it.btnDelete)
        }
    }

    private fun enableButton(isEnable : Boolean, view : Button) {
        view.isEnabled = isEnable
        if(isEnable) {
            view.alpha = 1f
        } else {
            view.alpha = 0.5f
        }
    }
}