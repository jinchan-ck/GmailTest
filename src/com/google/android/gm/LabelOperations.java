package com.google.android.gm;

import android.text.TextUtils;
import com.google.android.gm.provider.Label;
import com.google.android.gm.provider.LogUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

public class LabelOperations
{
  private static final Pattern OPERATION_LABEL_ADD_SEPERATOR_PATTERN = Pattern.compile("\\^\\*\\*\\^");
  private final Map<String, Operation> mOperations = Maps.newHashMap();

  public LabelOperations()
  {
  }

  public LabelOperations(Label paramLabel, boolean paramBoolean)
  {
    this();
    if (paramLabel != null)
    {
      add(paramLabel, paramBoolean);
      return;
    }
    LogUtils.e("Gmail", "LabelOperation created with null Label object", new Object[0]);
  }

  public static LabelOperations deserialize(String paramString)
  {
    LabelOperations localLabelOperations = new LabelOperations();
    if (paramString != null)
    {
      String[] arrayOfString1 = TextUtils.split(paramString, " ");
      int i = arrayOfString1.length;
      for (int j = 0; j < i; j++)
      {
        String[] arrayOfString2 = TextUtils.split(arrayOfString1[j], OPERATION_LABEL_ADD_SEPERATOR_PATTERN);
        if (arrayOfString2.length == 2)
          localLabelOperations.add(Label.parseJoinedString(arrayOfString2[0]), Boolean.valueOf(arrayOfString2[1]).booleanValue());
      }
    }
    return localLabelOperations;
  }

  public static String serialize(LabelOperations paramLabelOperations)
  {
    if (paramLabelOperations == null)
      return null;
    StringBuffer localStringBuffer = new StringBuffer();
    Iterator localIterator = paramLabelOperations.mOperations.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      localStringBuffer.append(((Operation)localEntry.getValue()).mLabel.serialize() + "^**^" + Boolean.toString(((Operation)localEntry.getValue()).mAdd) + " ");
    }
    return localStringBuffer.toString();
  }

  public void add(Label paramLabel, boolean paramBoolean)
  {
    Operation localOperation = new Operation(paramLabel, paramBoolean, null);
    this.mOperations.put(paramLabel.getCanonicalName(), localOperation);
  }

  public void clear()
  {
    this.mOperations.clear();
  }

  public int count()
  {
    return this.mOperations.size();
  }

  protected LabelOperations createNewInstance()
  {
    return new LabelOperations();
  }

  public List<Operation> getOperationList()
  {
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator = this.mOperations.entrySet().iterator();
    while (localIterator.hasNext())
      localArrayList.add(((Map.Entry)localIterator.next()).getValue());
    return localArrayList;
  }

  public boolean hasApplyArchive()
  {
    return hasRemoveOperation("^i");
  }

  public boolean hasApplyMute()
  {
    return hasApplyOperation("^g");
  }

  public boolean hasApplyOperation(Label paramLabel)
  {
    return hasApplyOperation(paramLabel.getCanonicalName());
  }

  public boolean hasApplyOperation(String paramString)
  {
    if (hasOperation(paramString))
      return ((Operation)this.mOperations.get(paramString)).mAdd;
    return false;
  }

  public boolean hasApplyRead()
  {
    return hasRemoveOperation("^u");
  }

  public boolean hasApplySpam()
  {
    return hasApplyOperation("^s");
  }

  public boolean hasApplyStar()
  {
    return hasOperation("^t");
  }

  public boolean hasApplyTrash()
  {
    return hasApplyOperation("^k");
  }

  public boolean hasApplyUnread()
  {
    return hasApplyOperation("^u");
  }

  public boolean hasMarkImportant()
  {
    return (hasApplyOperation("^^important")) || (hasRemoveOperation("^^unimportant"));
  }

  public boolean hasMarkNotImportant()
  {
    return (hasApplyOperation("^^unimportant")) || (hasRemoveOperation("^^important"));
  }

  public boolean hasMoveToInbox()
  {
    return hasApplyOperation("^i");
  }

  public boolean hasOperation(Label paramLabel)
  {
    return hasOperation(paramLabel.getCanonicalName());
  }

  public boolean hasOperation(String paramString)
  {
    return this.mOperations.containsKey(paramString);
  }

  public boolean hasRemoveOperation(Label paramLabel)
  {
    return hasRemoveOperation(paramLabel.getCanonicalName());
  }

  public boolean hasRemoveOperation(String paramString)
  {
    boolean bool1 = hasOperation(paramString);
    boolean bool2 = false;
    if (bool1)
    {
      boolean bool3 = ((Operation)this.mOperations.get(paramString)).mAdd;
      bool2 = false;
      if (!bool3)
        bool2 = true;
    }
    return bool2;
  }

  public LabelOperations undoOperation()
  {
    LabelOperations localLabelOperations = createNewInstance();
    Iterator localIterator = this.mOperations.entrySet().iterator();
    if (localIterator.hasNext())
    {
      Operation localOperation = (Operation)((Map.Entry)localIterator.next()).getValue();
      Label localLabel = localOperation.mLabel;
      if (!localOperation.mAdd);
      for (boolean bool = true; ; bool = false)
      {
        localLabelOperations.add(localLabel, bool);
        break;
      }
    }
    return localLabelOperations;
  }

  public class Operation
  {
    public final boolean mAdd;
    public final Label mLabel;

    private Operation(Label paramBoolean, boolean arg3)
    {
      this.mLabel = paramBoolean;
      boolean bool;
      this.mAdd = bool;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.LabelOperations
 * JD-Core Version:    0.6.2
 */