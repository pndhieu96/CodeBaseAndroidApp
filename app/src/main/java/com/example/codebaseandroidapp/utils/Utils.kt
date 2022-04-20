package com.example.codebaseandroidapp.utils

import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavDeepLinkBuilder
import com.example.codebaseandroidapp.Application
import com.example.codebaseandroidapp.MainActivity
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.receiver.NotificationActionReceiver
import com.example.codebaseandroidapp.service.SongService
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.CHANNEL_ID
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.DELAY_TIME_MILLIS
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.NOTIFICATION_ID
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.NOTIFICATION_TITLE
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.OUTPUT_PATH
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import com.example.codebaseandroidapp.utils.ConstantUtils.Companion.VERBOSE_NOTIFICATION_CHANNEL_NAME
import java.io.*
import java.net.URL
import java.util.*


class Utils {
    companion object {
        fun getOriginImagePath(path: String): String {
            path?.let {
                return "https://image.tmdb.org/t/p/original$it"
            }
            return ""
        }

        fun getImagePath(path: String, fileSize: String): String {
            var url = ""
            path?.let {
                url = "https://image.tmdb.org/t/p/$fileSize$it"
            }
            Log.d("getImagePath", url)
            return url
        }

        fun hideKeyboard(activity: Activity) {
            val imm: InputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view: View? = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }

        fun showKeyBoard(activity: Activity, view: EditText) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }

        fun download(urlString: String) {
            try {
                // Lấy đường dẫn thư mục lưu ảnh
                val songDir = songDirectory()
                // Kiểm tra thư mục đã tồn tại chưa
                if (!songDir.exists()) {
                    // Tạo thư mục
                    songDir.mkdirs()
                }

                // Lấy đường dẫn đến file trên thiết bị
                val f = songFile()
                // Lấy URL của file nhạc ở trên internet
                val url = URL(urlString)

                //Đọc file theo định dạng byte
                val input: InputStream = BufferedInputStream(url.openStream())
                //Viết file theo định dạng byte
                val output: OutputStream = FileOutputStream(f)

                val data = ByteArray(1024)

                var total = 0L
                // Đọc dữ liệu file nhạc tải về từ internet
                var count = input.read(data)
                while (count != -1) {
                    total++
                    Log.i("SongUtils", "$total")
                    // GHi liệu file nhạc tải về vào thư mục trên máy
                    output.write(data, 0, count)
                    count = input.read(data)
                }
                //Xoá dữ liệu đang được lưu trong output stream và buộc nó ghi dữ liệu xuống điểm đích.
                output.flush()
                //Đóng output stream, giải phóng tất cả các tài nguyên đang được kết nối với luồng này.
                output.close()
                //Đóng input stream, giải phóng tất cả các tài nguyên đang được kết nối với luồng này.
                input.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun delete(directory: File) {
            try {
                if (directory.exists()) {
                    val files: Array<File> = directory.listFiles()
                    if (files != null) {
                        var j: Int
                        j = 0
                        while (j < files.size) {
                            System.out.println(files[j].absolutePath)
                            System.out.println(files[j].delete())
                            j++
                        }
                    }
                    if (directory.delete()) {
                        Log.i("Utils.delete","file Deleted :" + directory.absolutePath);
                    } else {
                        Log.i("Utils.delete","file not Deleted :" + directory.absolutePath);
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun songDirectory() = Application.getAppContext().getDir(ConstantUtils.SONGS_DIRECTORY, Context.MODE_PRIVATE)

        fun songFile() = File(songDirectory(), ConstantUtils.SONG_FILENAME)

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createChannel(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                SongService.CHANNEL_ID, context.getString(R.string.media_playback),
                NotificationManager.IMPORTANCE_HIGH)
            channel.setShowBadge(false)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }

        fun createNotification(context: Context): Notification {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel(context)
            }

            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val intent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

//            val args = Bundle()
//            args.putStringf("myarg", "From Notification");
//            val intent = NavDeepLinkBuilder(context)
//                .setGraph(R.navigation.extenstion_navigation)
//                .setDestination(R.id.serviceFragment)
//                .setComponentName(MainActivity::class.java)
//                .setArguments(args)
//                .createPendingIntent()

            val contentView = RemoteViews(context.packageName, R.layout.notification_custom)
            contentView.setImageViewResource(R.id.image, R.drawable.song)
            contentView.setTextViewText(R.id.title, context.getString(R.string.notification_title))
            contentView.setTextViewText(R.id.text, "This is a custom layout")
            val buttonsIntent = Intent(context, NotificationActionReceiver::class.java)

            if(Application.isPlayingSong) {
                contentView.setImageViewResource(R.id.btnPlay, R.drawable.pause)
                buttonsIntent.putExtra("ACTION", SongService.ACTION_PAUSE)
            } else {
                contentView.setImageViewResource(R.id.btnPlay, R.drawable.play)
                buttonsIntent.putExtra("ACTION", SongService.ACTION_PLAY)
            }
            contentView.setOnClickPendingIntent(
                R.id.btnPlay,
                PendingIntent.getBroadcast(context, 0, buttonsIntent, PendingIntent.FLAG_CANCEL_CURRENT)
            )

            return NotificationCompat.Builder(context, SongService.CHANNEL_ID)
                .setContentIntent(intent)
                .setOnlyAlertOnce(true)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.song)
                .setCustomContentView(contentView)
                .setCustomBigContentView(contentView)
                .build()
        }

        fun makeStatusNotification(message: String, context: Context) {

            // Make a channel if necessary
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
                val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance)
                channel.description = description

                // Add the channel
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

                notificationManager?.createNotificationChannel(channel)
            }

            // Create the notification
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(LongArray(0))

            // Show the notification
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        }

        /**
         * Method for sleeping for a fixed amount of time to emulate slower work
         */
        fun sleep() {
            try {
                Thread.sleep(DELAY_TIME_MILLIS, 0)
            } catch (e: InterruptedException) {
                Log.e("Utils", e.message.toString())
            }

        }

        /**
         * Blurs the given Bitmap image
         * @param bitmap Image to blur
         * @param applicationContext Application context
         * @return Blurred bitmap image
         */
        @WorkerThread
        fun blurBitmap(bitmap: Bitmap, applicationContext: Context): Bitmap {
            lateinit var rsContext: RenderScript
            try {

                // Create the output bitmap
                val output = Bitmap.createBitmap(
                    bitmap.width, bitmap.height, bitmap.config)

                // Blur the image
                rsContext = RenderScript.create(applicationContext, RenderScript.ContextType.DEBUG)
                val inAlloc = Allocation.createFromBitmap(rsContext, bitmap)
                val outAlloc = Allocation.createTyped(rsContext, inAlloc.type)
                val theIntrinsic = ScriptIntrinsicBlur.create(rsContext, Element.U8_4(rsContext))
                theIntrinsic.apply {
                    setRadius(10f)
                    theIntrinsic.setInput(inAlloc)
                    theIntrinsic.forEach(outAlloc)
                }
                outAlloc.copyTo(output)

                return output
            } finally {
                rsContext.finish()
            }
        }

        /**
         * Writes bitmap to a temporary file and returns the Uri for the file
         * @param applicationContext Application context
         * @param bitmap Bitmap to write to temp file
         * @return Uri for temp file with bitmap
         * @throws FileNotFoundException Throws if bitmap file cannot be found
         *
         * Xem thư mục file đã lưu:
         * data > data > com.example.background > files > blur_filter_outputs> <URI>
         */
        @Throws(FileNotFoundException::class)
        fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap): Uri {
            val name = String.format("blur-filter-output-%s.png", UUID.randomUUID().toString())
            val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
            if (!outputDir.exists()) {
                outputDir.mkdirs() // should succeed
            }
            val outputFile = File(outputDir, name)
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(outputFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
            } finally {
                out?.let {
                    try {
                        it.close()
                    } catch (ignore: IOException) {
                    }

                }
            }
            return Uri.fromFile(outputFile)
        }

        @Throws(IOException::class)
        fun saveBitmap(
            context: Context, bitmap: Bitmap, format: Bitmap.CompressFormat,
            mimeType: String, displayName: String
        ): Uri {

            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            }

            var uri: Uri? = null

            return runCatching {
                with(context.contentResolver) {
                    insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.also {
                        uri = it // Keep uri reference so it can be removed on failure

                        openOutputStream(it)?.use { stream ->
                            if (!bitmap.compress(format, 95, stream))
                                throw IOException("Failed to save bitmap.")
                        } ?: throw IOException("Failed to open output stream.")

                    } ?: throw IOException("Failed to create new MediaStore record.")
                }
            }.getOrElse {
                uri?.let { orphanUri ->
                    // Don't leave an orphan entry in the MediaStore
                    context.contentResolver.delete(orphanUri, null, null)
                }

                throw it
            }
        }
    }
}