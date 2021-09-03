package com.example.safdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.RecyclerView
import com.example.safdemo.adapter.FileAdapter
import com.example.safdemo.adapter.FileItem
import com.example.safdemo.utils.DocumentHelper
import java.io.File


class FileExploreActivity : AppCompatActivity() {

    private lateinit var rootPath: String
    private val fileList = mutableListOf<FileItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var fileAdapter: FileAdapter
    private lateinit var contentView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_explore)
        recyclerView = findViewById(R.id.rcv)
        contentView = findViewById(R.id.content)
        rootPath = intent.getStringExtra("rootPath") ?: ""
        println("rootPath------------:$rootPath")
        initRcv()
        listFile(rootPath)
    }

    private fun initRcv() {
        fileAdapter = FileAdapter(fileList)
        fileAdapter.itemClick = View.OnClickListener {
            val item = it.tag as FileItem
            if (item.isDirectory) {
                val intent = Intent(this, FileExploreActivity::class.java)
                intent.putExtra("rootPath", DocumentHelper.treeToPath(item.filePath))
                startActivity(intent)
            } else {
                readFileContent(item)
            }
        }
        recyclerView.adapter = fileAdapter
    }

    private fun readFileContent(item: FileItem) {
        try {
            val testContent = DocumentHelper.getTestContent(this, item)
            contentView.visibility = View.VISIBLE
            contentView.text = "文件内容:$testContent"
            println("testContent:$testContent")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun listFile(filePath: String) {
        val file = File(filePath)
        val documentFile = DocumentFile.fromFile(file)
        println("documentFile.canRead():${documentFile.canRead()}")
        if (file.canRead()) {
            file.listFiles().forEach {
                fileList.add(FileItem(it, null, it.canonicalPath, it.name, it.isDirectory))
            }
            fileAdapter.notifyDataSetChanged()
            return
        }
        if (documentFile.canRead()) {
            listDocumentFiles(documentFile)
            return
        }

        val persistedUri = DocumentHelper.getPersistedUri(this, filePath)
        if (persistedUri != null) {
            listDocumentFiles(DocumentFile.fromTreeUri(this, persistedUri))
        } else {
            DocumentHelper.startActivity(this, filePath, 1)
        }
    }

    private fun listDocumentFiles(documentFile: DocumentFile?) {
        documentFile?.listFiles()?.forEach {
            fileList.add(FileItem(null, it.uri, it.uri.toString(), it.name, it.isDirectory))
        }
        fileList.forEach {
            println("fileList item-是否为目录：${it.isDirectory}-路径：${it.filePath}")
        }
        fileAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            finish()
            return
        }
        val uri = data?.data ?: return

        contentResolver.takePersistableUriPermission(
            uri, data.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        ) //关键是这里，这个就是保存这个目录的访问权限
        listDocumentFiles(DocumentFile.fromTreeUri(this, uri))
    }

    override fun onBackPressed() {
        if (contentView.visibility == View.VISIBLE) {
            contentView.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }
}