package com.example.codebaseandroidapp.model

import android.R
import android.content.Context

import android.media.MediaPlayer
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicControl @Inject constructor (@ApplicationContext context: Context?) {
    private var mContext: Context? = context
    var mMediaPlayer: MediaPlayer? = null

    fun playMusic(uri: Uri, isLoop: Boolean) {
        try {
            mContext?.let {
                mMediaPlayer = MediaPlayer.create(mContext, uri)
                mMediaPlayer!!.isLooping = isLoop
                mMediaPlayer!!.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopMusic() {
        mMediaPlayer?.stop()
    }

    fun pauseMusic() {
        mMediaPlayer?.pause()
    }

    fun playMusic() {
        mMediaPlayer?.start()
    }
}