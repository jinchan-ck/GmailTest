package com.google.android.gm.template;

import java.io.IOException;

public class Ternary extends Expression
{
  private final Expression mCondition;
  private final Expression mFalseExpression;
  private final Expression mTrueExpression;

  public Ternary(Expression paramExpression1, Expression paramExpression2, Expression paramExpression3)
  {
    this.mCondition = paramExpression1;
    this.mTrueExpression = paramExpression2;
    this.mFalseExpression = paramExpression3;
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    paramJavaCodeGenerator.append("(Expression.isTruthy(").append(this.mCondition).append(")").append("?").append("(").append(this.mTrueExpression).append(")").append(":").append("(").append(this.mFalseExpression).append(")").appendLine(")");
  }

  public Object evaluate(EvalContext paramEvalContext)
  {
    if (this.mCondition.booleanValue(paramEvalContext))
      return this.mTrueExpression.evaluate(paramEvalContext);
    return this.mFalseExpression.evaluate(paramEvalContext);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Ternary
 * JD-Core Version:    0.6.2
 */