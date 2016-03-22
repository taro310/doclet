package jp.co.shin_gi.javadoc.excel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.shin_gi.javadoc.Context;
import jp.co.shin_gi.javadoc.IExpressionPattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sun.javadoc.ProgramElementDoc;

public class ManualPattern implements IExpressionPattern<Cell> {
    private Pattern m_pattern = Pattern.compile("\\{manual\\}");

    public ManualPattern() {
    }

    @Override
    public boolean match(Context<Cell> context, ProgramElementDoc ped, Cell target, String value) {
        Matcher m = this.m_pattern.matcher(value);
        if (!m.matches()) {
            return false;
        }
        target.setCellValue(value);
        ExcelContext ec = (ExcelContext) context;
        Workbook ebook = ec.getExistingWorkbook();
        if (ebook == null) {
            return false;
        }

        Sheet esheet = ebook.getSheet(target.getSheet().getSheetName());
        if (esheet == null)
            return false;
        int r = target.getRowIndex();
        Row erow = esheet.getRow(r);
        if (erow == null)
            return false;
        int c = target.getColumnIndex();
        Cell ecell = erow.getCell(c);
        if (ecell == null) {
            return false;
        }

        switch (ecell.getCellType()) {
        case 3:
            target.setCellType(3);
            break;
        case 4:
            target.setCellValue(ecell.getBooleanCellValue());
            break;
        case 5:
            target.setCellErrorValue(ecell.getErrorCellValue());
            break;
        case 2:
            target.setCellFormula(ecell.getCellFormula());
            break;
        case 0:
            target.setCellValue(ecell.getNumericCellValue());
            break;
        case 1:
            target.setCellValue(ecell.getStringCellValue());
        }

        CellStyle estyle = ecell.getCellStyle();
        CellStyle tstyle = ec.getTargetWorkbook().createCellStyle();
        tstyle.cloneStyleFrom(estyle);
        target.setCellStyle(tstyle);

        return true;
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.excel.ManualPattern Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */