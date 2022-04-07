package com.example.codebaseandroidapp.model

import android.R
import android.content.Context

import android.media.MediaPlayer
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Hilt-4
 * Xác định phạm vi của instance bằng những chú thích như:
 *
 * @singleton để tạo đối tượng ở phạm vi application, có nghĩa luôn truy cập đến 1 instance
 * duy nhất của đối tượng trong cả ứng dụng
 *
    * @ActivityRetainedScoped
    * @ServiceScoped
         *
         * @ActivityScoped để tạo đối tượng ở phạm vi của 1 activity, có nghĩa là luôn truy cập đến
         * 1 instance của đối tượng trong 1 activity
         * @ViewModelScoped
             *
             * @FragmentScoped
             * @ViewScoped
 */
@Singleton
class MusicControl @Inject constructor (@ApplicationContext context: Context) {
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