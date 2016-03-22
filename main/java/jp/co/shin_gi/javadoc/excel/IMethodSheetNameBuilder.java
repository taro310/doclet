package jp.co.shin_gi.javadoc.excel;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;

public abstract interface IMethodSheetNameBuilder
{
  public abstract void build(ExcelContext paramExcelContext, ClassDoc paramClassDoc);
  
  public abstract String get(ExcelContext paramExcelContext, ExecutableMemberDoc paramExecutableMemberDoc);
}

/* Location:           C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar
 * Qualified Name:     jp.co.shin_gi.javadoc.excel.IMethodSheetNameBuilder
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.0.1
 */