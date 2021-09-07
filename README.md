# android11SafDemo
android 11 分区存储 兼容旧版本 迁移工具类方法


~~~
val persistedUri = DocumentHelper.getPersistedUri(this, filePath)
if (persistedUri != null) {
  listDocumentFiles(DocumentFile.fromTreeUri(this, persistedUri))
} else {
  DocumentHelper.startActivity(this, filePath, 1)
}
~~~
