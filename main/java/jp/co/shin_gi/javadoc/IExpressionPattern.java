package jp.co.shin_gi.javadoc;

import com.sun.javadoc.ProgramElementDoc;

public abstract interface IExpressionPattern<T>
{
  public abstract boolean match(Context<T> paramContext, ProgramElementDoc paramProgramElementDoc, T paramT, String paramString);
}

/* Location:           C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar
 * Qualified Name:     jp.co.shin_gi.javadoc.IExpressionPattern
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.0.1
 */