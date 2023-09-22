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

    class CodeAPI {
        companion object {
            const val CodeSignOutAllDevices: Int = 493
            const val CodeRevokeAccount: Int = 403
            const val CodeRevokeDeleteAccount: Int = 2006
            const val CodeRevokeChangePassword: Int = 2012
            const val CodeTimeOut: Int = 9999
            const val CodeOfferUsed: Int = 1059
            const val CodeOfferExpires: Int = 1058
            const val CodeUnregisteredAccount: Int = 1006 // Tài khoản chưa tồn tài trong hệ thống
            const val CodeOfferOutOfStock: Int = 1057
            const val CodeAccountDeleteWaiting: Int = 2013 // Tài khoản đang trong thời gian chờ xóa
            const val CodeAccountDeleted: Int = 2015 // Tài khoản đã bị xóa
            const val CodeOTPWaiting: Int = 1033 // Chờ để gửi lại OTP
            const val CodePhoneNumberInValid: Int = 1012 // Sdt không đúng định dạng
            const val CodeNotShowTimes: Int = 1011 // Không có suất chiếu phù hợp
            const val CodeIncorrectPassword: Int = 1060 // Mật khẩu không chính xác
            const val CodeBlockPaymentSupport: Int =
                2017 // Đã đạt tối đa số lượt gửi yêu cầu hỗ trợ
            const val CodeMoviePassTooManyDeadLines: Int =
                1108 // Movie đã mua vượt quá thời hạn tối đa cho phép
        }
    }
}