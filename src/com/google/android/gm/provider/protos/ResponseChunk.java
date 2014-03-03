package com.google.android.gm.provider.protos;

public abstract interface ResponseChunk
{
  public static final int BEGIN_CONVERSATION = 9;
  public static final int BEGIN_MESSAGE = 10;
  public static final int CHECK_CONVERSATION = 8;
  public static final int CONFIG_ACCEPTED = 3;
  public static final int CONFIG_INFO = 2;
  public static final int END_CONVERSATION = 12;
  public static final int END_MESSAGE = 13;
  public static final int FORWARD_SYNC_OPERATION = 7;
  public static final int MESSAGE = 11;
  public static final int MESSAGE_BATCH = 18;
  public static final int NO_CONVERSATION = 16;
  public static final int NO_MESSAGE = 17;
  public static final int PREAMBLE = 1;
  public static final int QUERY = 15;
  public static final int START_SYNC = 4;
  public static final int SYNC_POSTAMBLE = 14;
  public static final int UPHILL_SYNC = 5;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.protos.ResponseChunk
 * JD-Core Version:    0.6.2
 */