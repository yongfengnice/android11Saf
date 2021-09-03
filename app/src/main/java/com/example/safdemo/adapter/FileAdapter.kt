package com.example.safdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safdemo.R
import com.example.safdemo.utils.DocumentHelper

/**
 * @author Created by suyongfeng on 2021/8/23
 */
class FileAdapter(private val fileList: List<FileItem>) : RecyclerView.Adapter<FileHolder>() {
    var itemClick: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        val fileHolder = FileHolder(inflate)
        fileHolder.root.setOnClickListener {
            itemClick?.onClick(it)
        }
        return fileHolder
    }

    override fun onBindViewHolder(holder: FileHolder, position: Int) {
        val fileItem = fileList[position]
        holder.textView.text = "文件路径：${DocumentHelper.treeToPath(fileItem.filePath)}"
        if (fileItem.isDirectory) {
            holder.nameView.text = "目录名称：${fileItem.fileName}"
        } else {
            holder.nameView.text = "文件名称：${fileItem.fileName}"
        }
        holder.root.tag = fileItem
    }

    override fun getItemCount(): Int {
        return fileList.size
    }
}

class FileHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val root: View = itemView.findViewById(R.id.root)
    val textView: TextView = itemView.findViewById(R.id.text)
    val nameView: TextView = itemView.findViewById(R.id.name)
}