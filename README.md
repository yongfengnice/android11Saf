# android11SafDemo
android 11 分区存储 兼容旧版本 迁移工具类方法


~~~
val persistedUri = DocumentHelper.getPersistedUri(this, filePath)
if (persistedUri != null) {
  listDocumentFiles(DocumentFile.fromTreeUri(this, persistedUri))
} else {
  DocumentHelper.startActivity(this, filePath, 1)
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
  super.onActivityResult(requestCode, resultCode, data)
  if (resultCode != Activity.RESULT_OK) {
    finish()
    return
  }
  val uri = data?.data ?: return
  //关键是这里，这个就是保存这个目录的访问权限
  contentResolver.takePersistableUriPermission(uri, data.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)) 
  listDocumentFiles(DocumentFile.fromTreeUri(this, uri))
}
~~~
