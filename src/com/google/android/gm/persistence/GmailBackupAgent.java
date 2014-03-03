package com.google.android.gm.persistence;

import android.app.backup.BackupAgent;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupManager;
import android.app.backup.FullBackupDataOutput;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import com.google.android.gm.SharedPreference;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.Settings;
import com.google.android.gm.provider.LogUtils;
import com.google.common.collect.Maps;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.Adler32;
import org.json.JSONException;
import org.json.JSONObject;

public class GmailBackupAgent extends BackupAgent
{
  public static void dataChanged(String paramString)
  {
    LogUtils.v("GmailBackupAgent", "%s may have changed", new Object[] { paramString });
    BackupManager.dataChanged("com.google.android.gm");
  }

  private static long getChecksum(Map<String, Gmail.Settings> paramMap, List<SharedPreference> paramList)
  {
    Adler32 localAdler32 = new Adler32();
    Iterator localIterator1 = paramMap.entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      updateChecksum(localAdler32, localEntry.getKey());
      Gmail.Settings localSettings = (Gmail.Settings)localEntry.getValue();
      updateChecksum(localAdler32, localSettings.getConversationAgeDays());
      updateChecksum(localAdler32, localSettings.getMaxAttachmentSizeMb());
      updateChecksum(localAdler32, localSettings.getLabelsIncluded());
      updateChecksum(localAdler32, localSettings.getLabelsPartial());
    }
    Iterator localIterator2 = paramList.iterator();
    while (localIterator2.hasNext())
    {
      SharedPreference localSharedPreference = (SharedPreference)localIterator2.next();
      updateChecksum(localAdler32, localSharedPreference.getKey());
      updateChecksum(localAdler32, localSharedPreference.getValue());
    }
    return localAdler32.getValue();
  }

  private static Object getDataObject(BackupDataInput paramBackupDataInput)
    throws IOException
  {
    int i = paramBackupDataInput.getDataSize();
    byte[] arrayOfByte = new byte[i];
    paramBackupDataInput.readEntityData(arrayOfByte, 0, i);
    ObjectInputStream localObjectInputStream = new ObjectInputStream(new ByteArrayInputStream(arrayOfByte));
    try
    {
      Object localObject = localObjectInputStream.readObject();
      return localObject;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      LogUtils.e("GmailBackupAgent", localClassNotFoundException, "Invalid restore data", new Object[0]);
    }
    return null;
  }

  private List<String> getDatabaseAccounts(Context paramContext)
  {
    ArrayList localArrayList = new ArrayList();
    String[] arrayOfString = paramContext.databaseList();
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = new File(arrayOfString[j]).getName();
      if ((str.endsWith(".db")) && (str.startsWith("internal")))
        localArrayList.add(str.substring(1 + "internal".length(), str.length() - ".db".length()));
    }
    return localArrayList;
  }

  private List<SharedPreference> getSharedPreferenceList(BackupDataInput paramBackupDataInput)
    throws IOException
  {
    return (List)getDataObject(paramBackupDataInput);
  }

  private List<SharedPreference> getSharedPreferences(Context paramContext)
  {
    List localList = Persistence.getInstance().getBackupPreferences(paramContext);
    if (LogUtils.isLoggable("GmailBackupAgent", 2))
    {
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
        LogUtils.v("GmailBackupAgent", "Backup %s", new Object[] { (SharedPreference)localIterator.next() });
    }
    return localList;
  }

  private Map<String, Gmail.Settings> getSyncSettings(Context paramContext)
  {
    List localList = getDatabaseAccounts(paramContext);
    Gmail localGmail = LongShadowUtils.getContentProviderMailAccess(getContentResolver());
    LinkedHashMap localLinkedHashMap = Maps.newLinkedHashMap();
    Iterator localIterator1 = localList.iterator();
    while (localIterator1.hasNext())
    {
      String str = (String)localIterator1.next();
      localLinkedHashMap.put(str, localGmail.getBackupSettings(paramContext, str));
    }
    if (LogUtils.isLoggable("GmailBackupAgent", 2))
    {
      Iterator localIterator2 = localLinkedHashMap.entrySet().iterator();
      while (localIterator2.hasNext())
        LogUtils.v("GmailBackupAgent", "Backup %s", new Object[] { (Map.Entry)localIterator2.next() });
    }
    return localLinkedHashMap;
  }

  private LinkedHashMap<String, Gmail.Settings> getSyncSettingsMap(BackupDataInput paramBackupDataInput)
    throws IOException
  {
    return (LinkedHashMap)getDataObject(paramBackupDataInput);
  }

  private GmailBackupData parseJsonBackupData(BackupDataInput paramBackupDataInput)
  {
    try
    {
      int i = paramBackupDataInput.getDataSize();
      byte[] arrayOfByte = new byte[i];
      paramBackupDataInput.readEntityData(arrayOfByte, 0, i);
      GmailBackupData localGmailBackupData = parseJsonBackupData(arrayOfByte);
      return localGmailBackupData;
    }
    catch (IOException localIOException)
    {
      LogUtils.e("GmailBackupAgent", localIOException, "Invalid restore data", new Object[0]);
    }
    return new GmailBackupData(null, null);
  }

  private GmailBackupData parseJsonBackupData(byte[] paramArrayOfByte)
  {
    try
    {
      String str = new String(paramArrayOfByte, "UTF-8");
      LogUtils.v("GmailBackupAgent", "Reading restore data: %s", new Object[] { str });
      GmailBackupData localGmailBackupData = GmailBackupData.fromJson(new JSONObject(str));
      return localGmailBackupData;
    }
    catch (JSONException localJSONException)
    {
      LogUtils.e("GmailBackupAgent", localJSONException, "Invalid restore data", new Object[0]);
      return new GmailBackupData(null, null);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new RuntimeException("UTF-8 should never throw a UnsupportedEncodingException", localUnsupportedEncodingException);
    }
    catch (Exception localException)
    {
      LogUtils.e("GmailBackupAgent", localException, "Invalid restore data", new Object[0]);
    }
    return new GmailBackupData(null, null);
  }

  private byte[] serializeBackupData(GmailBackupData paramGmailBackupData)
    throws UnsupportedEncodingException, JSONException
  {
    String str = paramGmailBackupData.toJson().toString();
    LogUtils.v("GmailBackupAgent", "Writing backup data: %s", new Object[] { str });
    return str.getBytes("UTF-8");
  }

  private static void updateChecksum(Adler32 paramAdler32, long paramLong)
  {
    paramAdler32.update((int)(0xFFFF & paramLong));
    paramAdler32.update((int)(paramLong >>> 32));
  }

  private static void updateChecksum(Adler32 paramAdler32, Object paramObject)
  {
    try
    {
      paramAdler32.update(paramObject.toString().getBytes("UTF-8"));
      return;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
  }

  private static void updateChecksum(Adler32 paramAdler32, List<String> paramList)
  {
    updateChecksum(paramAdler32, (String[])paramList.toArray(new String[paramList.size()]));
  }

  private static void updateChecksum(Adler32 paramAdler32, String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      updateChecksum(paramAdler32, paramArrayOfString[j]);
      updateChecksum(paramAdler32, 124L);
    }
  }

  private static void writeBackupData(BackupDataOutput paramBackupDataOutput, String paramString, byte[] paramArrayOfByte)
    throws IOException
  {
    if (paramArrayOfByte != null)
    {
      int i = paramArrayOfByte.length;
      paramBackupDataOutput.writeEntityHeader(paramString, i);
      paramBackupDataOutput.writeEntityData(paramArrayOfByte, i);
    }
  }

  public void onBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2)
    throws IOException
  {
    Context localContext = getApplicationContext();
    Map localMap = getSyncSettings(localContext);
    List localList = getSharedPreferences(localContext);
    long l = getChecksum(localMap, localList);
    new DataOutputStream(new FileOutputStream(paramParcelFileDescriptor2.getFileDescriptor())).writeLong(l);
    DataInputStream localDataInputStream = new DataInputStream(new FileInputStream(paramParcelFileDescriptor1.getFileDescriptor()));
    try
    {
      if (localDataInputStream.readLong() == l)
      {
        LogUtils.v("GmailBackupAgent", "No changes to backup", new Object[0]);
        return;
      }
    }
    catch (IOException localIOException)
    {
      LogUtils.w("GmailBackupAgent", "Failed to read old backup state", new Object[0]);
      GmailBackupData localGmailBackupData = new GmailBackupData(localMap, localList);
      try
      {
        writeBackupData(paramBackupDataOutput, "backup_data_v2", serializeBackupData(localGmailBackupData));
        return;
      }
      catch (JSONException localJSONException)
      {
        throw new IOException("Failed to serialize settings", localJSONException);
      }
    }
  }

  public void onFullBackup(FullBackupDataOutput paramFullBackupDataOutput)
    throws IOException
  {
    Context localContext = getApplicationContext();
    GmailBackupData localGmailBackupData = new GmailBackupData(getSyncSettings(localContext), getSharedPreferences(localContext));
    File localFile = new File(getFilesDir().getAbsolutePath(), "backup_data_file");
    try
    {
      DataOutputStream localDataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(localFile)));
      localDataOutputStream.writeInt(2);
      try
      {
        byte[] arrayOfByte = serializeBackupData(localGmailBackupData);
        localDataOutputStream.writeInt(arrayOfByte.length);
        localDataOutputStream.write(arrayOfByte);
        localDataOutputStream.flush();
        fullBackupFile(localFile, paramFullBackupDataOutput);
        return;
      }
      catch (JSONException localJSONException)
      {
        throw new IOException("Failed serialize object", localJSONException);
      }
    }
    finally
    {
      localFile.delete();
    }
  }

  public void onRestore(BackupDataInput paramBackupDataInput, int paramInt, ParcelFileDescriptor paramParcelFileDescriptor)
    throws IOException
  {
    Context localContext = getApplicationContext();
    Persistence localPersistence = Persistence.getInstance();
    Object localObject = null;
    List localList1 = null;
    Gmail localGmail;
    while (true)
      if (paramBackupDataInput.readNextHeader())
      {
        String str2 = paramBackupDataInput.getKey();
        if ("gmail_shared_preferences_v1".equals(str2))
        {
          localList1 = getSharedPreferenceList(paramBackupDataInput);
        }
        else if ("gmail_sync_settings_v1".equals(str2))
        {
          localObject = getSyncSettingsMap(paramBackupDataInput);
        }
        else if ("backup_data_v2".equals(str2))
        {
          GmailBackupData localGmailBackupData = parseJsonBackupData(paramBackupDataInput);
          localObject = localGmailBackupData.getSyncSettings();
          localList1 = localGmailBackupData.getSharedPreferences();
        }
      }
      else
      {
        if (localList1 != null)
          localPersistence.restoreSharedPreferences(localContext, localList1, "GmailBackupAgent");
        localGmail = LongShadowUtils.getContentProviderMailAccess(getContentResolver());
        if (localObject == null)
          break;
        Iterator localIterator2 = ((Map)localObject).entrySet().iterator();
        while (localIterator2.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator2.next();
          Object[] arrayOfObject1 = new Object[2];
          arrayOfObject1[0] = localEntry.getKey();
          arrayOfObject1[1] = localEntry.getValue();
          LogUtils.v("GmailBackupAgent", "Restore: %s: %s", arrayOfObject1);
          localGmail.restoreSettings(localContext, (String)localEntry.getKey(), (Gmail.Settings)localEntry.getValue());
        }
        paramBackupDataInput.skipEntityData();
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = paramBackupDataInput.getKey();
        LogUtils.w("GmailBackupAgent", "Unknown restore key: %s", arrayOfObject2);
      }
    LinkedHashMap localLinkedHashMap = Maps.newLinkedHashMap();
    Iterator localIterator1 = getDatabaseAccounts(localContext).iterator();
    while (localIterator1.hasNext())
    {
      String str1 = (String)localIterator1.next();
      localLinkedHashMap.put(str1, localGmail.getBackupSettings(localContext, str1));
    }
    List localList2 = localPersistence.getBackupPreferences(localContext);
    new DataOutputStream(new FileOutputStream(paramParcelFileDescriptor.getFileDescriptor())).writeLong(getChecksum(localLinkedHashMap, localList2));
  }

  public void onRestoreFile(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong1, File paramFile, int paramInt, long paramLong2, long paramLong3)
    throws IOException
  {
    LogUtils.v("GmailBackupAgent", "onRestoreFile() invoked", new Object[0]);
    DataInputStream localDataInputStream = new DataInputStream(new FileInputStream(paramParcelFileDescriptor.getFileDescriptor()));
    int i = localDataInputStream.readInt();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(i);
    LogUtils.v("GmailBackupAgent", "Flattened data version %d", arrayOfObject);
    if (i == 2)
    {
      byte[] arrayOfByte = new byte[localDataInputStream.readInt()];
      localDataInputStream.readFully(arrayOfByte);
      GmailBackupData localGmailBackupData = parseJsonBackupData(arrayOfByte);
      Map localMap = localGmailBackupData.getSyncSettings();
      Context localContext = getApplicationContext();
      if (localMap != null)
      {
        Gmail localGmail = LongShadowUtils.getContentProviderMailAccess(getContentResolver());
        Iterator localIterator = localMap.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          Gmail.Settings localSettings = (Gmail.Settings)localEntry.getValue();
          String str = (String)localEntry.getKey();
          LogUtils.v("GmailBackupAgent", "Restore: %s: %s", new Object[] { str, localSettings });
          localGmail.restoreSettings(localContext, str, localSettings);
        }
      }
      List localList = localGmailBackupData.getSharedPreferences();
      if (localList != null)
        Persistence.getInstance().restoreSharedPreferences(localContext, localList, "GmailBackupAgent");
      return;
    }
    paramParcelFileDescriptor.close();
    throw new IOException("Invalid file schema");
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.persistence.GmailBackupAgent
 * JD-Core Version:    0.6.2
 */