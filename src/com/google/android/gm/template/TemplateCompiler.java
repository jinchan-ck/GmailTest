package com.google.android.gm.template;

public class TemplateCompiler
{
  private static String getFileNameNoSuffix(String paramString)
  {
    int i = paramString.lastIndexOf(".");
    if (i == -1)
      i = paramString.length();
    return paramString.substring(1 + paramString.lastIndexOf("/"), i);
  }

  // ERROR //
  public static void main(String[] paramArrayOfString)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 38	com/google/android/gm/template/TemplateCompiler:validateFlags	([Ljava/lang/String;)Z
    //   4: ifne +49 -> 53
    //   7: getstatic 44	java/lang/System:err	Ljava/io/PrintStream;
    //   10: ldc 46
    //   12: invokevirtual 52	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   15: getstatic 44	java/lang/System:err	Ljava/io/PrintStream;
    //   18: new 54	java/lang/StringBuilder
    //   21: dup
    //   22: invokespecial 55	java/lang/StringBuilder:<init>	()V
    //   25: ldc 57
    //   27: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   30: ldc 2
    //   32: invokevirtual 67	java/lang/Class:getName	()Ljava/lang/String;
    //   35: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   38: ldc 69
    //   40: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   46: invokevirtual 52	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   49: iconst_m1
    //   50: invokestatic 76	java/lang/System:exit	(I)V
    //   53: aload_0
    //   54: iconst_0
    //   55: aaload
    //   56: astore_1
    //   57: aload_1
    //   58: invokestatic 78	com/google/android/gm/template/TemplateCompiler:getFileNameNoSuffix	(Ljava/lang/String;)Ljava/lang/String;
    //   61: astore_2
    //   62: new 80	java/io/PrintWriter
    //   65: dup
    //   66: new 82	java/io/FileWriter
    //   69: dup
    //   70: aload_1
    //   71: invokespecial 84	java/io/FileWriter:<init>	(Ljava/lang/String;)V
    //   74: invokespecial 87	java/io/PrintWriter:<init>	(Ljava/io/Writer;)V
    //   77: astore_3
    //   78: aload_3
    //   79: astore 4
    //   81: aload 4
    //   83: ldc 89
    //   85: invokevirtual 90	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   88: aload 4
    //   90: new 54	java/lang/StringBuilder
    //   93: dup
    //   94: invokespecial 55	java/lang/StringBuilder:<init>	()V
    //   97: ldc 92
    //   99: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: aload_2
    //   103: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: ldc 94
    //   108: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   111: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   114: invokevirtual 90	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   117: iconst_1
    //   118: istore 7
    //   120: iload 7
    //   122: aload_0
    //   123: arraylength
    //   124: if_icmpge +173 -> 297
    //   127: aload_0
    //   128: iload 7
    //   130: aaload
    //   131: astore 8
    //   133: new 96	java/io/BufferedReader
    //   136: dup
    //   137: new 98	java/io/FileReader
    //   140: dup
    //   141: aload 8
    //   143: invokespecial 99	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   146: invokespecial 102	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   149: astore 9
    //   151: new 104	com/google/android/gm/template/Parser
    //   154: dup
    //   155: aload 9
    //   157: invokespecial 105	com/google/android/gm/template/Parser:<init>	(Ljava/io/Reader;)V
    //   160: invokevirtual 109	com/google/android/gm/template/Parser:parse	()Lcom/google/android/gm/template/Template;
    //   163: astore 12
    //   165: aload 12
    //   167: astore 11
    //   169: aload 11
    //   171: aload 8
    //   173: invokestatic 78	com/google/android/gm/template/TemplateCompiler:getFileNameNoSuffix	(Ljava/lang/String;)Ljava/lang/String;
    //   176: invokevirtual 114	com/google/android/gm/template/Template:setName	(Ljava/lang/String;)V
    //   179: aload 9
    //   181: invokevirtual 117	java/io/BufferedReader:close	()V
    //   184: aload 11
    //   186: new 119	com/google/android/gm/template/JavaCodeGenerator
    //   189: dup
    //   190: aload 4
    //   192: invokespecial 122	com/google/android/gm/template/JavaCodeGenerator:<init>	(Ljava/lang/Appendable;)V
    //   195: invokevirtual 126	com/google/android/gm/template/Template:emitCode	(Lcom/google/android/gm/template/JavaCodeGenerator;)V
    //   198: iinc 7 1
    //   201: goto -81 -> 120
    //   204: astore 13
    //   206: getstatic 44	java/lang/System:err	Ljava/io/PrintStream;
    //   209: ldc 128
    //   211: invokevirtual 52	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   214: aload 13
    //   216: invokevirtual 131	java/io/IOException:printStackTrace	()V
    //   219: iconst_m1
    //   220: invokestatic 76	java/lang/System:exit	(I)V
    //   223: aconst_null
    //   224: astore 4
    //   226: goto -145 -> 81
    //   229: astore 10
    //   231: getstatic 44	java/lang/System:err	Ljava/io/PrintStream;
    //   234: new 54	java/lang/StringBuilder
    //   237: dup
    //   238: invokespecial 55	java/lang/StringBuilder:<init>	()V
    //   241: ldc 133
    //   243: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   246: aload 8
    //   248: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   251: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   254: invokevirtual 52	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   257: aload 10
    //   259: invokevirtual 134	com/google/android/gm/template/SyntaxError:printStackTrace	()V
    //   262: iconst_m1
    //   263: invokestatic 76	java/lang/System:exit	(I)V
    //   266: aconst_null
    //   267: astore 11
    //   269: goto -100 -> 169
    //   272: astore 6
    //   274: getstatic 44	java/lang/System:err	Ljava/io/PrintStream;
    //   277: ldc 136
    //   279: invokevirtual 52	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   282: aload 6
    //   284: invokevirtual 131	java/io/IOException:printStackTrace	()V
    //   287: iconst_m1
    //   288: invokestatic 76	java/lang/System:exit	(I)V
    //   291: aload 4
    //   293: invokevirtual 137	java/io/PrintWriter:close	()V
    //   296: return
    //   297: aload 4
    //   299: ldc 139
    //   301: invokevirtual 90	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   304: goto -13 -> 291
    //   307: astore 5
    //   309: aload 4
    //   311: invokevirtual 137	java/io/PrintWriter:close	()V
    //   314: aload 5
    //   316: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   62	78	204	java/io/IOException
    //   151	165	229	com/google/android/gm/template/SyntaxError
    //   81	117	272	java/io/IOException
    //   120	151	272	java/io/IOException
    //   151	165	272	java/io/IOException
    //   169	198	272	java/io/IOException
    //   231	266	272	java/io/IOException
    //   297	304	272	java/io/IOException
    //   81	117	307	finally
    //   120	151	307	finally
    //   151	165	307	finally
    //   169	198	307	finally
    //   231	266	307	finally
    //   274	291	307	finally
    //   297	304	307	finally
  }

  private static boolean validateFlags(String[] paramArrayOfString)
  {
    return paramArrayOfString.length >= 2;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.TemplateCompiler
 * JD-Core Version:    0.6.2
 */