package com.google.android.gm;

import android.content.SearchRecentSuggestionsProvider;

public class SuggestionsProvider extends SearchRecentSuggestionsProvider
{
  static final String AUTHORITY = "com.google.android.gmail.SuggestionProvider";
  static final int MODE = 1;

  public SuggestionsProvider()
  {
    setupSuggestions("com.google.android.gmail.SuggestionProvider", 1);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.SuggestionsProvider
 * JD-Core Version:    0.6.2
 */