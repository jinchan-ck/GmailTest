package com.google.android.gm.template;

import java.util.ArrayList;
import java.util.Map;

public class EvalContext
{
  private final ArrayList<Map<String, ? extends Object>> mScopes = new ArrayList();

  public EvalContext(Map<String, ? extends Object> paramMap)
  {
    push(paramMap);
  }

  public Object get(String paramString)
  {
    for (int i = this.mScopes.size() - 1; i >= 0; i--)
    {
      Map localMap = (Map)this.mScopes.get(i);
      if (localMap.containsKey(paramString))
        return localMap.get(paramString);
    }
    return null;
  }

  public Map<String, ? extends Object> pop()
  {
    return (Map)this.mScopes.remove(this.mScopes.size() - 1);
  }

  public void push(Map<String, ? extends Object> paramMap)
  {
    this.mScopes.add(paramMap);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.EvalContext
 * JD-Core Version:    0.6.2
 */