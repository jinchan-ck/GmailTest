package com.google.android.gm.provider;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;

public final class SpamReasonType
{
  public static final Set<Integer> HIGH_WARNING_LEVEL_SPAM_TYPES = ImmutableSet.of(Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(8), Integer.valueOf(15), Integer.valueOf(108));
  public static final Map<Integer, Integer> SPAM_REASON_TO_STRING_MAP = new ImmutableMap.Builder().put(Integer.valueOf(-1), Integer.valueOf(0)).put(Integer.valueOf(0), Integer.valueOf(2131427626)).put(Integer.valueOf(1), Integer.valueOf(2131427614)).put(Integer.valueOf(2), Integer.valueOf(2131427606)).put(Integer.valueOf(4), Integer.valueOf(2131427613)).put(Integer.valueOf(5), Integer.valueOf(2131427607)).put(Integer.valueOf(6), Integer.valueOf(2131427619)).put(Integer.valueOf(7), Integer.valueOf(2131427620)).put(Integer.valueOf(8), Integer.valueOf(2131427608)).put(Integer.valueOf(9), Integer.valueOf(2131427621)).put(Integer.valueOf(10), Integer.valueOf(2131427622)).put(Integer.valueOf(11), Integer.valueOf(2131427623)).put(Integer.valueOf(12), Integer.valueOf(2131427624)).put(Integer.valueOf(13), Integer.valueOf(2131427625)).put(Integer.valueOf(14), Integer.valueOf(0)).put(Integer.valueOf(15), Integer.valueOf(2131427609)).put(Integer.valueOf(101), Integer.valueOf(2131427615)).put(Integer.valueOf(102), Integer.valueOf(2131427616)).put(Integer.valueOf(103), Integer.valueOf(2131427620)).put(Integer.valueOf(104), Integer.valueOf(2131427606)).put(Integer.valueOf(105), Integer.valueOf(2131427617)).put(Integer.valueOf(106), Integer.valueOf(2131427618)).put(Integer.valueOf(107), Integer.valueOf(2131427610)).put(Integer.valueOf(108), Integer.valueOf(2131427611)).put(Integer.valueOf(109), Integer.valueOf(2131427612)).build();
  public static final Map<Integer, Integer> WARNING_LINK_TYPE_MAP = ImmutableMap.of(Integer.valueOf(107), Integer.valueOf(2), Integer.valueOf(15), Integer.valueOf(1));
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.SpamReasonType
 * JD-Core Version:    0.6.2
 */