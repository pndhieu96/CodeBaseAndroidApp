package com.example.codebaseandroidapp.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.codebaseandroidapp.utils.Utils.Companion.printLog

object NetworkUtils {
    private var listListener: ArrayList<NetworkLiveData> = arrayListOf()
    private var isNetworkConnection: Boolean = true

    fun isNetworkAvailable() = isNetworkConnection
    fun isNetworkConnected() = isNetworkConnection
    private fun setNetworkAvailable(isOnline: Boolean) {
        isNetworkConnection = isOnline
    }

    private fun updateConnection(isOnline: Boolean) {
        try {
            setNetworkAvailable(isOnline = isOnline)
            val listIterator = listListener.iterator()
            while (listIterator.hasNext()) {
                val child = listIterator.next()
                if (child.hasObservers()) {
                    if (child.hasActiveObservers()) {
                        child.postNetworkValue(value = isOnline)
                    }
                } else {
                    listIterator.remove()
                }
            }
        } catch (e: Exception) {
            e.printLog()
        }
    }

    @SuppressLint("MissingPermission")
    fun start(context: Context) {
        try {
            val connectivityManager =
                ContextCompat.getSystemService(
                    context,
                    ConnectivityManager::class.java
                ) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        updateConnection(isOnline = true)
                    }

                    override fun onLost(network: Network) {
                        updateConnection(isOnline = false)
                    }
                })
            } else {
                val networkChangeFilter = NetworkRequest.Builder().build()
                connectivityManager.registerNetworkCallback(
                    networkChangeFilter,
                    object : ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            updateConnection(isOnline = true)
                        }

                        override fun onLost(network: Network) {
                            updateConnection(isOnline = false)
                        }
                    })
            }
        } catch (e: Exception) {
            e.printLog()
        }
    }

    @SuppressLint("MissingPermission")
    fun registerNetworkChange(lifecycleOwner: LifecycleOwner, callback: (Boolean) -> Unit) {
        val networkLiveData = NetworkLiveData()
        networkLiveData.registerListener(onActive = {
            networkLiveData.postNetworkValue(value = isNetworkConnection)
        }, onInactive = {
        })

        networkLiveData.observe(lifecycleOwner) { isOnline ->
            callback.invoke(isOnline)
        }
        listListener.add(networkLiveData)
    }

    /*@SuppressLint("MissingPermission")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }*/
}

class NetworkLiveData : LiveData<Boolean>() {
    private var onActive: (() -> Unit)? = null
    private var onInactive: (() -> Unit)? = null

    override fun onActive() {
        super.onActive()
        onActive?.invoke()
    }

    override fun onInactive() {
        super.onInactive()
        onInactive?.invoke()
    }

    fun registerListener(onActive: () -> Unit, onInactive: () -> Unit) {
        this.onActive = onActive
        this.onInactive = onInactive
    }

    fun postNetworkValue(value: Boolean) {
        if (hasActiveObservers()) {
            postValue(value)
        }
    }
}