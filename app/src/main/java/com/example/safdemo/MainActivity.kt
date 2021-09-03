package com.example.safdemo

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.View
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("可读：${ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)}")
        println("可写：${ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)}")
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            0
        )
    }

    fun documentDir(view: View) {
        //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() /storage/emulated/0/Documents
//getExternalFilesDir(null) // /storage/emulated/0/Android/data/com.example.safdemo/files
        val downloadDir = Environment.DIRECTORY_DOCUMENTS
        var path = getExternalFilesDir(null)?.canonicalPath?.replace("Android/data/$packageName/files", downloadDir)
        println("downloadDir:$path")
        val intent = Intent(this,FileExploreActivity::class.java)
        intent.putExtra("rootPath", path)
        startActivity(intent)
    }

    fun downloadDir(view: View) {
        //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() /storage/emulated/0/Download
        // /storage/emulated/0/Android/data/com.example.safdemo/files
        val downloadDir = Environment.DIRECTORY_DOWNLOADS
        var path = getExternalFilesDir(null)?.canonicalPath?.replace("Android/data/$packageName/files", downloadDir)
        println("downloadDir:$path")
        val intent = Intent(this,FileExploreActivity::class.java)
        intent.putExtra("rootPath", path)
        startActivity(intent)
    }

    fun weChatDir(view: View) {
        // /storage/emulated/0/Android/data/com.example.safdemo/files
        val wxDownload = "com.tencent.mm/MicroMsg/Download"
        var path = getExternalFilesDir(null)?.canonicalPath?.replace("$packageName/files", wxDownload)
        println("weChatDir:$path")
        val intent = Intent(this,FileExploreActivity::class.java)
        intent.putExtra("rootPath", path)
        startActivity(intent)
    }

    fun qqChatDir(view: View) {
        val wxDownload = "com.tencent.mobileqq/Tencent/QQfile_recv"
        var path = getExternalFilesDir(null)?.canonicalPath?.replace("$packageName/files", wxDownload)
        println("weChatDir:$path")
        val intent = Intent(this,FileExploreActivity::class.java)
        intent.putExtra("rootPath", path)
        startActivity(intent)
    }

}