package com.example.safdemo.utils;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.net.URI;

/**
 * @author Created by suyongfeng on 2021/9/3
 * 兼容旧版本的File操作接口，便于旧项目接口兼容的迁移工作
 * 比如之前的
 * File[] files = fileDir.listFiles()
 * 替换现在的
 * File[] files = DocumentHelper.isAndroidR() ? DocumentUtil.listFiles(fileDir.getAbsolutePath()) : fileDir.listFiles();
 * 即可
 */
public class SafFile extends File {
    private static final long serialVersionUID = 0L;

    private DocumentFile documentFile;

    public SafFile(@NonNull String pathname) {
        super(pathname);
    }

    public SafFile(@NonNull URI uri) {
        super(uri);
    }

    public static SafFile fromDocumentFile(DocumentFile documentFile) {
        SafFile file = new SafFile(documentFile.getName());
        file.documentFile = documentFile;
        return file;
    }

    @Override
    public String getPath() {
        return documentFile.getUri().toString();
    }

    @Override
    public boolean isDirectory() {
        return documentFile.isDirectory();
    }

    @NonNull
    @Override
    public String getAbsolutePath() {
        return documentFile.getUri().toString();
    }

    @NonNull
    @Override
    public String getName() {
        return documentFile.getName();
    }

    @Override
    public long length() {
        return documentFile.length();
    }

    @Override
    public boolean equals(Object o) {
        return documentFile.equals(o);
    }

    @Override
    public int hashCode() {
        return documentFile.hashCode();
    }

    @Override
    public long lastModified() {
        return documentFile.lastModified();
    }

    @Override
    public boolean exists() {
        return documentFile.exists();
    }

    public Uri getUri(){
        return documentFile.getUri();
    }

    public DocumentFile getDocumentFile() {
        return documentFile;
    }
}
