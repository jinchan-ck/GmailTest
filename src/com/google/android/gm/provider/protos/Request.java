package com.google.android.gm.provider.protos;

public abstract interface Request
{
  public static final int CLIENT_ID = 1;
  public static final int COMPRESSION_TYPE_DICTIONARY = 1;
  public static final int COMPRESSION_TYPE_DICTIONARY_BATCH = 3;
  public static final int COMPRESSION_TYPE_DICTIONARY_BATCH_DEBUG = 4;
  public static final int COMPRESSION_TYPE_DICTIONARY_BATCH_UNLIMITED = 5;
  public static final int COMPRESSION_TYPE_DICTIONARY_DEBUG = 2;
  public static final int COMPRESSION_TYPE_NONE = 0;
  public static final int CONFIG = 2;
  public static final int CONFIG_CONVERSATION_AGE_DAYS = 1;
  public static final int CONFIG_DURATION_LABEL_CANONICAL_NAME = 3;
  public static final int CONFIG_INCLUDED_LABEL_CANONICAL_NAME = 2;
  public static final int CONVERSATION_SYNC = 3;
  public static final int CONVERSATION_SYNC_CONVERSATION = 1;
  public static final int CONVERSATION_SYNC_CONVERSATION_CONVERSATION_ID = 1;
  public static final int CONVERSATION_SYNC_CONVERSATION_HIGHEST_MESSAGE_ID_ON_CLIENT = 2;
  public static final int CONVERSATION_SYNC_CONVERSATION_MESSAGES_TO_SKIP = 3;
  public static final int CONVERSATION_SYNC_DIRTY_CONVERSATION_ID = 4;
  public static final int CONVERSATION_SYNC_FULL_CHECK = 3;
  public static final int CONVERSATION_SYNC_MESSAGE_ID = 2;
  public static final int GET_CONFIG_INFO = 8;
  public static final int MAIN_SYNC = 4;
  public static final int MAIN_SYNC_COMPRESSION = 7;
  public static final int MAIN_SYNC_FETCH_CUSTOM_COLOR_PREFS = 6;
  public static final int MAIN_SYNC_FETCH_CUSTOM_FROM_PREFS = 8;
  public static final int MAIN_SYNC_FETCH_INFO_OVERLOAD_ENABLED_PREF = 9;
  public static final int MAIN_SYNC_HANDLED_SERVER_OPERATION_ID = 2;
  public static final int MAIN_SYNC_LOWEST_CONVERSATION_ID = 1;
  public static final int MAIN_SYNC_MAX_BACKWARD_SYNC_MESSAGES = 3;
  public static final int MAIN_SYNC_MAX_BACKWARD_SYNC_MESSAGES_LIMIT = 4;
  public static final int MAIN_SYNC_MAX_FORWARD_SYNC_ITEMS_LIMIT = 5;
  public static final int QUERY = 5;
  public static final int QUERY_HIGHEST_MESSAGE_ID = 2;
  public static final int QUERY_MAX_CONVERSATIONS = 3;
  public static final int QUERY_MAX_SENDERS = 4;
  public static final int QUERY_QUERY = 1;
  public static final int START_SYNC = 6;
  public static final int START_SYNC_ACKED_CLIENT_OPERATION_ID = 4;
  public static final int START_SYNC_FETCH_CUSTOM_COLOR_PREFS = 5;
  public static final int START_SYNC_FETCH_CUSTOM_FROM_PREFS = 6;
  public static final int START_SYNC_FETCH_INFO_OVERLOAD_ENABLED_PREF = 7;
  public static final int START_SYNC_FETCH_SIGNATURE_PREFS = 8;
  public static final int START_SYNC_HANDLED_SERVER_OPERATION_ID = 1;
  public static final int START_SYNC_HIGHEST_MESSAGE_ID_FETCHED = 2;
  public static final int START_SYNC_LOWEST_MESSAGE_ID_FETCHED = 3;
  public static final int UPHILL_SYNC = 7;
  public static final int UPHILL_SYNC_ACKED_CLIENT_OPERATION_ID = 2;
  public static final int UPHILL_SYNC_OPERATION = 1;
  public static final int UPHILL_SYNC_OPERATION_CONVERSATION_LABEL_ADDED_OR_REMOVED = 3;
  public static final int UPHILL_SYNC_OPERATION_CONVERSATION_LABEL_ADDED_OR_REMOVED_ADDED = 3;
  public static final int UPHILL_SYNC_OPERATION_CONVERSATION_LABEL_ADDED_OR_REMOVED_CANONICAL_NAME = 4;
  public static final int UPHILL_SYNC_OPERATION_CONVERSATION_LABEL_ADDED_OR_REMOVED_LABEL_ID = 2;
  public static final int UPHILL_SYNC_OPERATION_CONVERSATION_LABEL_ADDED_OR_REMOVED_MAX_MESSAGE_ID = 1;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_EXPUNGED = 5;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_LABEL_ADDED_OR_REMOVED = 2;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_LABEL_ADDED_OR_REMOVED_ADDED = 3;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_LABEL_ADDED_OR_REMOVED_CANONICAL_NAME = 4;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_LABEL_ADDED_OR_REMOVED_LABEL_ID = 2;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_LABEL_ADDED_OR_REMOVED_MESSAGE_ID = 1;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT = 4;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_BCC = 5;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_BODY = 7;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_CC = 4;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_CUSTOM_FROM_ADDRESS = 15;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_FORWARD = 12;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_FORWARDED_ATTACHMENT_IDS = 8;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_INCLUDE_QUOTED_TEXT = 11;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_PREVIOUS_DRAFT_ID = 1;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_QUOTE_START_POS = 13;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_REF_MESSAGE_ID = 2;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_SAVED = 10;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_SUBJECT = 6;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_TO = 3;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_UPLOADED_ATTACHMENT = 9;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_UPLOADED_ATTACHMENT_ATTACHMENT_ID = 1;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_UPLOADED_ATTACHMENT_CONTENT_TYPE = 3;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_UPLOADED_ATTACHMENT_DATA = 4;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_UPLOADED_ATTACHMENT_FILENAME = 2;
  public static final int UPHILL_SYNC_OPERATION_MESSAGE_SAVED_OR_SENT_USE_CUSTOM_FROM_ADDRESS = 14;
  public static final int UPHILL_SYNC_OPERATION_OPERATION_ID = 1;
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.protos.Request
 * JD-Core Version:    0.6.2
 */