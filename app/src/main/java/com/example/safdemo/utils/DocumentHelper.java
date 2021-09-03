package com.example.safdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.text.TextUtils;

import androidx.documentfile.provider.DocumentFile;

import com.example.safdemo.adapter.FileItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * @author Created by suyongfeng on 2021/8/23
 */
public class DocumentHelper {
    public static final String ZIP_FILE_SEPARATOR = "/";
    public static String root = (Environment.getExternalStorageDirectory().getPath() + ZIP_FILE_SEPARATOR);
    public static final String ANDROID_PRIMARY_3A = "content://com.android.externalstorage.documents/tree/primary%3A";
    public static final String ANDROID_PRIMARY_3A_DATA = ANDROID_PRIMARY_3A + "Android%2Fdata";
    public static final String ANDROID_PRIMARY_3A_DOWNLOAD = ANDROID_PRIMARY_3A + "Download";
    public static final String ANDROID_DATA_DOCUMENT_PATH = "/tree/primary:Android/data/document/primary:Android/data";
    public static final String ANDROID_DATA_PATH = root + "Android/data";
    public static final String ANDROID_DATA_APP_PATH = ANDROID_DATA_PATH + "/com.example.safdemo/";
    public static final String FILE_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "safdemo" + File.separator + "cache";
    public static final String FILE_TEMPORARY_CACHE_PATH = (FILE_CACHE_PATH + File.separator + "androidData");

    public static String changeToUri(String str) {
        if (TextUtils.isEmpty(str)) return "";
        if (str.endsWith(ZIP_FILE_SEPARATOR)) {
            str = str.substring(0, str.length() - 1);
        }
        String replace = str.replace(root, "").replace(ZIP_FILE_SEPARATOR, "%2F");
        return ANDROID_PRIMARY_3A_DATA + "/document/primary%3A" + replace;
    }

    public static String changeToUri2(String str) {
        if (TextUtils.isEmpty(str)) return "";
        String replace = str.replace(root, "").replace(ZIP_FILE_SEPARATOR, "%2F");
        return ANDROID_PRIMARY_3A + replace;
    }

    public static String treeToPath(String str) {
        if (str.contains(ANDROID_PRIMARY_3A_DATA) || str.contains(ANDROID_PRIMARY_3A_DOWNLOAD)) {
            int lastIndexOf = str.lastIndexOf("%3A");
            return (root + str.substring(lastIndexOf + 3)).replace("%2F", ZIP_FILE_SEPARATOR);
        } else {
            return str;
        }
    }

    public static boolean checkIfAndroidData(String str) {
        if (TextUtils.isEmpty(str)) return false;
        return !str.startsWith(ANDROID_DATA_APP_PATH) && isOverAndroidR()
                && (str.startsWith(ANDROID_DATA_PATH) || str.startsWith(ANDROID_DATA_DOCUMENT_PATH) || str.startsWith(ANDROID_PRIMARY_3A_DATA));
    }

    public static boolean isOverAndroidR() {
        return Build.VERSION.SDK_INT > 29;
    }

    public static boolean copyDocumentFileToCache(Context context, String str) {
        return copyFileData(context, DocumentFile.fromSingleUri(context, Uri.parse(str)), FILE_TEMPORARY_CACHE_PATH);
    }

    public static boolean copyFileData(Context context, DocumentFile documentFile, String str) {
        try {
            File file = new File(str);
            String name = documentFile.getName();
            Objects.requireNonNull(name);
            File file2 = new File(str, name);
            if (!file.exists()) {
                file.mkdirs();
            }
            InputStream inputStream = getInputStream(context, documentFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    inputStream.close();
                    fileOutputStream.close();
                    return true;
                }
            }
        } catch (Exception unused) {
            return false;
        }
    }

    public static String getTestContent(Context context, FileItem item) throws Exception {
        InputStream inputStream;
        if (item.getFile() != null) {
            inputStream = new FileInputStream(item.getFile());
        } else {
            inputStream = getInputStream(context, DocumentFile.fromSingleUri(context, item.getUri()));
        }
        byte[] bArr = new byte[1024];
        int available = inputStream.available();
        inputStream.read(bArr);
        String content = new String(bArr, "UTF-8");
        if (content.length() > available) {
            content = content.substring(0, available);
        }
        inputStream.close();
        return content;
    }

    public static DocumentFile getDocumentFile(Context context, String str) {
        if (str.endsWith(ZIP_FILE_SEPARATOR)) {
            str = str.substring(0, str.length() - 1);
        }
        String replace = str.replace(root, "").replace(ZIP_FILE_SEPARATOR, "%2F");
        return DocumentFile.fromSingleUri(context, Uri.parse(ANDROID_PRIMARY_3A_DATA + "/document/primary%3A" + replace));
    }

    public static InputStream getInputStream(Context context, DocumentFile documentFile) {
        if (documentFile == null) {
            return null;
        }
        try {
            if (documentFile.canWrite()) {
                return context.getContentResolver().openInputStream(documentFile.getUri());
            }
            return null;
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static void startActivity(Activity activity, String str, int reqCode) {
        Uri parse = Uri.parse(changeToUri(str));
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= 26) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, parse);
        }
        activity.startActivityForResult(intent, reqCode);
    }

    public static Uri getPersistedUri(Context context, String filePath) {
        String uriStr = changeToUri2(filePath);
        List<UriPermission> uriPermissions = context.getContentResolver().getPersistedUriPermissions();
        if (uriPermissions != null && uriPermissions.size() > 0) {
            for (UriPermission uri : uriPermissions) {
                if (uri != null && uri.getUri() != null && uriStr.equals(uri.getUri().toString())) {
                    return uri.getUri();
                }
            }
        }
        return null;
    }
}
