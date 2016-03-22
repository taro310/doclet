package jp.co.shin_gi.javadoc.excel;

import jp.co.shin_gi.javadoc.Context;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelContext extends Context<Cell> {
    public static final String DEFAULT_CLASS_SHEET_NAME = "class summary";
    public static final String SHEET_CLASS = "class";
    public static final String SHEET_METHOD = "method";
    private String m_classSheetName = "class summary";

    private Workbook m_existingWorkbook;

    private IMethodSheetNameBuilder m_methodSheetNameBuilder = null;

    private Workbook m_targetWorkbook;

    public ExcelContext() {
    }

    public String getClassSheetName() {
        return this.m_classSheetName;
    }

    public int getClassTemplateIndex() {
        Workbook book = getTargetWorkbook();
        int index = book.getSheetIndex("class");
        return index;
    }

    public Workbook getExistingWorkbook() {
        return this.m_existingWorkbook;
    }

    public IMethodSheetNameBuilder getMethodSheetNameBuilder() {
        return this.m_methodSheetNameBuilder;
    }

    public int getMethodTemplateIndex() {
        Workbook book = getTargetWorkbook();
        int index = book.getSheetIndex("method");
        return index;
    }

    public Workbook getTargetWorkbook() {
        return this.m_targetWorkbook;
    }

    public void setClassSheetName(String classSheetName) {
        if ((classSheetName == null) || (classSheetName.trim().length() == 0)) {
            classSheetName = "class summary";
        }
        this.m_classSheetName = classSheetName;
    }

    public void setExistingWorkbook(Workbook existingWorkbook) {
        this.m_existingWorkbook = existingWorkbook;
    }

    public void setMethodSheetNameBuilder(IMethodSheetNameBuilder methodSheetNameBuilder) {
        this.m_methodSheetNameBuilder = methodSheetNameBuilder;
    }

    public void setTargetWorkbook(Workbook targetWorkbook) {
        this.m_targetWorkbook = targetWorkbook;
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.excel.ExcelContext Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */