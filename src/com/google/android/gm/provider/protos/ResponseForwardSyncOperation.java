package com.google.android.gm.provider.protos;

public abstract interface ResponseForwardSyncOperation
{
  public static final int CHANGE_LABEL_ADDED = 0;
  public static final int CHANGE_LABEL_REMOVED = 1;
  public static final int CHANGE_MESSAGES_EXPUNGED = 2;
  public static final int CHECK_CONVERSATION = 8;
  public static final int LABEL_CREATED = 3;
  public static final int LABEL_CREATED_CANONICAL_NAME = 2;
  public static final int LABEL_CREATED_DISPLAY_NAME = 3;
  public static final int LABEL_CREATED_LABEL_ID = 1;
  public static final int LABEL_DELETED = 5;
  public static final int LABEL_DELETED_LABEL_ID = 1;
  public static final int LABEL_RENAMED = 4;
  public static final int LABEL_RENAMED_CANONICAL_NAME = 2;
  public static final int LABEL_RENAMED_DISPLAY_NAME = 3;
  public static final int LABEL_RENAMED_LABEL_ID = 1;
  public static final int OPERATION_ID = 1;
  public static final int PREFERENCE_CHANGE = 9;
  public static final int PREFERENCE_CHANGE_PREFERENCE_DELETED = 3;
  public static final int PREFERENCE_CHANGE_PREFERENCE_NAME = 1;
  public static final int PREFERENCE_CHANGE_PREFERENCE_VALUE = 2;
  public static final int THREAD_LABELED_OR_UNLABELED = 2;
  public static final int THREAD_LABELED_OR_UNLABELED_CHANGE = 1;
  public static final int THREAD_LABELED_OR_UNLABELED_CONVERSATION_ID = 2;
  public static final int THREAD_LABELED_OR_UNLABELED_LABEL_ID = 3;
  public static final int THREAD_LABELED_OR_UNLABELED_MESSAGE_ID = 5;
  public static final int THREAD_LABELED_OR_UNLABELED_SYNC_RATIONALE = 4;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.protos.ResponseForwardSyncOperation
 * JD-Core Version:    0.6.2
 */