package com.google.android.gm.template;

import java.io.IOException;
import java.util.ArrayList;

public class Template extends Command
{
  private String mName = "Anonymous";
  private final ArrayList<Node> mNodes;

  public Template(ArrayList<Node> paramArrayList)
  {
    this.mNodes = paramArrayList;
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    paramJavaCodeGenerator.beginTemplateOutput(this.mName);
    int i = 0;
    int j = this.mNodes.size();
    while (i < j)
    {
      ((Node)this.mNodes.get(i)).emitCode(paramJavaCodeGenerator);
      i++;
    }
    paramJavaCodeGenerator.endTemplateOutput();
  }

  public void setName(String paramString)
  {
    this.mName = paramString;
  }

  public void writeValue(Appendable paramAppendable, EvalContext paramEvalContext)
    throws IOException
  {
    int i = 0;
    int j = this.mNodes.size();
    while (i < j)
    {
      ((Node)this.mNodes.get(i)).writeValue(paramAppendable, paramEvalContext);
      i++;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Template
 * JD-Core Version:    0.6.2
 */