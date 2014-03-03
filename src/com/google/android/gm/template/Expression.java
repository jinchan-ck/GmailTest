package com.google.android.gm.template;

import java.io.IOException;

public abstract class Expression extends Node
{
  public static boolean isTruthy(Object paramObject)
  {
    if ((paramObject == null) || (Boolean.FALSE.equals(paramObject)) || ("".equals(paramObject)) || (((paramObject instanceof Number)) && (((Number)paramObject).doubleValue() == 0.0D)));
    for (int i = 1; i == 0; i = 0)
      return true;
    return false;
  }

  public final boolean booleanValue(EvalContext paramEvalContext)
  {
    return isTruthy(evaluate(paramEvalContext));
  }

  public abstract Object evaluate(EvalContext paramEvalContext);

  public void writeValue(Appendable paramAppendable, EvalContext paramEvalContext)
    throws IOException
  {
    Object localObject = evaluate(paramEvalContext);
    if (localObject == null);
    for (String str = ""; ; str = localObject.toString())
    {
      paramAppendable.append(str);
      return;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Expression
 * JD-Core Version:    0.6.2
 */