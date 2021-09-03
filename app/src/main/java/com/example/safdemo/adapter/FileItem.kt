package com.example.safdemo.adapter

import android.net.Uri
import java.io.File

/**
 * @author Created by suyongfeng on 2021/8/20
 */
data class FileItem(
    val file: File?,
    val uri: Uri?,
    val filePath: String?,
    val fileName: String?,
    val isDirectory: Boolean,
)