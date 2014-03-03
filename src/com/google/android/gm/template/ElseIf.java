package com.google.android.gm.template;

public class ElseIf extends Command
{
  private final Expression mCondition;

  public ElseIf(Expression paramExpression)
  {
    this.mCondition = paramExpression;
  }

  public Expression getCondition()
  {
    return this.mCondition;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.ElseIf
 * JD-Core Version:    0.6.2
 */