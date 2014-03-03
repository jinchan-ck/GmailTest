package com.google.android.gm.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class LabelQueryBuilder
{
  private List<String> mArgs;
  private SQLiteQueryBuilder mBuilder = new SQLiteQueryBuilder();
  private SQLiteDatabase mDb;
  private int mLimit;
  private String[] mProjection;
  private boolean mRecent;
  private boolean whereAppended;

  public LabelQueryBuilder(Context paramContext, SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString)
  {
    this.mDb = paramSQLiteDatabase;
    this.mProjection = paramArrayOfString;
    this.mBuilder.setTables("labels");
    this.mBuilder.setProjectionMap(MailEngine.LABEL_PROJECTION_MAP);
    this.mArgs = Lists.newArrayList(QueryUtils.getQueryBindArgs(paramContext, paramArrayOfString, "name", new String[0]));
  }

  private void appendWhereClause(CharSequence paramCharSequence)
  {
    if (this.whereAppended)
      this.mBuilder.appendWhere(" AND ");
    while (true)
    {
      this.mBuilder.appendWhere(paramCharSequence);
      return;
      this.whereAppended = true;
    }
  }

  private String getLimit()
  {
    if (this.mLimit != 0)
      return Integer.toString(this.mLimit);
    return null;
  }

  private String getOrder()
  {
    if (this.mRecent)
      return "lastTouched DESC";
    return "systemLabel DESC, sortOrder ASC";
  }

  public LabelQueryBuilder filterCanonicalName(List<String> paramList)
  {
    if ((paramList != null) && (!paramList.isEmpty()))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int i = 1;
      Iterator localIterator = paramList.iterator();
      if (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if (i != 0)
        {
          localStringBuilder.append("canonicalName IN (");
          i = 0;
        }
        while (true)
        {
          localStringBuilder.append("?");
          this.mArgs.add(str);
          break;
          localStringBuilder.append(", ");
        }
      }
      localStringBuilder.append(")");
      appendWhereClause(localStringBuilder.toString());
    }
    return this;
  }

  List<String> getQueryArgs()
  {
    return this.mArgs;
  }

  String getQueryString()
  {
    return this.mBuilder.buildQuery(this.mProjection, null, null, null, getOrder(), getLimit());
  }

  public LabelQueryBuilder labelId(long paramLong)
  {
    if (paramLong != 0L)
    {
      appendWhereClause("_id = ?");
      this.mArgs.add(Long.toString(paramLong));
    }
    return this;
  }

  public Cursor query()
  {
    String[] arrayOfString = (String[])this.mArgs.toArray(new String[this.mArgs.size()]);
    return this.mBuilder.query(this.mDb, this.mProjection, null, arrayOfString, null, null, getOrder(), getLimit());
  }

  public LabelQueryBuilder setRecent(long paramLong, int paramInt)
  {
    if (paramLong != 0L)
    {
      StringBuilder localStringBuilder = new StringBuilder("lastTouched");
      localStringBuilder.append(" != 0 AND ");
      localStringBuilder.append("lastTouched");
      localStringBuilder.append(" < ?");
      this.mArgs.add(Long.toString(paramLong));
      this.mLimit = paramInt;
      appendWhereClause(localStringBuilder.toString());
      this.mRecent = true;
    }
    return this;
  }

  public LabelQueryBuilder showHidden(boolean paramBoolean)
  {
    if (!paramBoolean)
      appendWhereClause("hidden = 0");
    return this;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.LabelQueryBuilder
 * JD-Core Version:    0.6.2
 */