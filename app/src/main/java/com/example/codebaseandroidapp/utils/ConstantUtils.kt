package com.example.codebaseandroidapp.utils

class ConstantUtils {
    companion object {
        const val API_KEY = "990c4fbb01df42398dcb580b5d8b271e"
        const val FILE_SIZE_LANDSCAPE = "w400"
        const val FILE_SIZE_PORTRAIT = "w200"
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val SONG_URL = "https://docs.google.com/uc?id=1abhacJoSwGnbg89tRYHyGpTyfMskCXT3&export=download"
        const val SONGS_DIRECTORY = "songs"
        const val SONG_FILENAME = "siren.mp3"
        @JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
            "Verbose WorkManager Notifications"
        const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
            "Shows notifications whenever work starts"
        @JvmField val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
        const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
        const val NOTIFICATION_ID = 1

        // The name of the image manipulation work
        const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"

        // Other keys
        const val OUTPUT_PATH = "blur_filter_outputs"
        const val KEY_IMAGE_URI = "KEY_IMAGE_URI"
        const val TAG_OUTPUT = "OUTPUT"

        const val DELAY_TIME_MILLIS: Long = 3000
    }
}