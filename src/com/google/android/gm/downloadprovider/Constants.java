package com.google.android.gm.downloadprovider;

public class Constants
{
  public static final String ACTION_HIDE = "android.intent.action.DOWNLOAD_HIDE";
  public static final String ACTION_LIST = "android.intent.action.DOWNLOAD_LIST";
  public static final String ACTION_OPEN = "android.intent.action.DOWNLOAD_OPEN";
  public static final String ACTION_RETRY = "android.intent.action.DOWNLOAD_WAKEUP";
  public static final int BUFFER_SIZE = 4096;
  public static final String DEFAULT_DL_BINARY_EXTENSION = ".bin";
  public static final String DEFAULT_DL_FILENAME = "downloadfile";
  public static final String DEFAULT_DL_HTML_EXTENSION = ".html";
  public static final String DEFAULT_DL_SUBDIR = "/download";
  public static final String DEFAULT_DL_TEXT_EXTENSION = ".txt";
  public static final String DEFAULT_USER_AGENT = "AndroidDownloadManager";
  public static final String ETAG = "etag";
  public static final String FAILED_CONNECTIONS = "numfailed";
  public static final String FILENAME_SEQUENCE_SEPARATOR = "-";
  public static final String KNOWN_SPURIOUS_FILENAME = "lost+found";
  private static final boolean LOCAL_LOGV = false;
  private static final boolean LOCAL_LOGVV = false;
  public static final boolean LOGV = false;
  public static final boolean LOGVV = false;
  static final boolean LOGX = false;
  public static final int MAX_DOWNLOADS = 1000;
  public static final int MAX_REDIRECTS = 5;
  public static final int MAX_RETRIES = 5;
  public static final int MAX_RETRY_AFTER = 86400;
  public static final String MEDIA_SCANNED = "scanned";
  public static final String MIMETYPE_APK = "application/vnd.android.package";
  public static final int MIN_PROGRESS_STEP = 4096;
  public static final long MIN_PROGRESS_TIME = 1500L;
  public static final int MIN_RETRY_AFTER = 30;
  public static final String NO_SYSTEM_FILES = "no_system";
  public static final String OTA_UPDATE = "otaupdate";
  public static final String RECOVERY_DIRECTORY = "recovery";
  public static final String RETRY_AFTER_X_REDIRECT_COUNT = "method";
  public static final int RETRY_FIRST_DELAY = 30;
  public static final String TAG = "DownloadManager";
  public static final String UID = "uid";
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.downloadprovider.Constants
 * JD-Core Version:    0.6.2
 */