package com.google.android.gm.template;

import java.io.IOException;
import java.util.ArrayList;

public class If extends Command
{
  private final ArrayList<Expression> mConditions;
  private final ArrayList<ArrayList<Node>> mNodeLists;

  private If(ArrayList<Expression> paramArrayList, ArrayList<ArrayList<Node>> paramArrayList1)
  {
    this.mConditions = paramArrayList;
    this.mNodeLists = paramArrayList1;
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    int i = 0;
    int j = this.mConditions.size();
    while (i < j)
    {
      Expression localExpression = (Expression)this.mConditions.get(i);
      if (i == 0)
        paramJavaCodeGenerator.append("if (Expression.isTruthy(").append(localExpression).appendLine(")) {");
      while (true)
      {
        paramJavaCodeGenerator.increaseIndent();
        ArrayList localArrayList = (ArrayList)this.mNodeLists.get(i);
        int k = 0;
        int m = localArrayList.size();
        while (k < m)
        {
          ((Node)localArrayList.get(k)).emitCode(paramJavaCodeGenerator);
          k++;
        }
        if (i == j - 1)
          paramJavaCodeGenerator.appendLine("else {");
        else
          paramJavaCodeGenerator.append("else if (Expression.isTruthy(").append(localExpression).appendLine(")) {");
      }
      paramJavaCodeGenerator.decreaseIndent();
      paramJavaCodeGenerator.appendLine("}");
      i++;
    }
  }

  public void writeValue(Appendable paramAppendable, EvalContext paramEvalContext)
    throws IOException
  {
    int i = 0;
    int j = this.mConditions.size();
    while (i < j)
    {
      if (((Expression)this.mConditions.get(i)).booleanValue(paramEvalContext))
      {
        ArrayList localArrayList = (ArrayList)this.mNodeLists.get(i);
        int k = 0;
        int m = localArrayList.size();
        while (k < m)
        {
          ((Node)localArrayList.get(k)).writeValue(paramAppendable, paramEvalContext);
          k++;
        }
      }
      i++;
    }
  }

  public static class Builder
  {
    private final ArrayList<Expression> mConditions = new ArrayList();
    private boolean mHasDefault = false;
    private final ArrayList<ArrayList<Node>> mNodeLists = new ArrayList();

    public Builder addCondition(Expression paramExpression)
    {
      if (this.mHasDefault)
        throw new IllegalStateException("Can't add more conditions after default");
      this.mConditions.add(paramExpression);
      this.mNodeLists.add(new ArrayList());
      return this;
    }

    public Builder addDefault()
    {
      addCondition(Constant.TRUE);
      this.mHasDefault = true;
      return this;
    }

    public Builder addNode(Node paramNode)
    {
      int i = this.mNodeLists.size() - 1;
      ((ArrayList)this.mNodeLists.get(i)).add(paramNode);
      return this;
    }

    public If build()
    {
      return new If(this.mConditions, this.mNodeLists, null);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.If
 * JD-Core Version:    0.6.2
 */