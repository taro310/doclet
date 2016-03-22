package jp.co.shin_gi.javadoc.excel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.shin_gi.javadoc.Context;
import jp.co.shin_gi.javadoc.IExpressionPattern;

import org.apache.poi.ss.usermodel.Cell;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Type;

public class ReturnTypePattern implements IExpressionPattern<Cell> {
    private Pattern m_pattern = Pattern.compile("\\{return\\.type\\}");

    public ReturnTypePattern() {
    }

    @Override
    public boolean match(Context<Cell> context, ProgramElementDoc ped, Cell target, String value) {
        Matcher m = this.m_pattern.matcher(value);
        if (!m.matches()) {
            return false;
        }
        if ((ped instanceof MethodDoc)) {
            MethodDoc md = (MethodDoc) ped;
            Type rt = md.returnType();
            if (rt != null) {
                target.setCellValue(rt.qualifiedTypeName() + rt.dimension());
                return true;
            }
        }

        target.setCellValue(value);
        return true;
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.excel.ReturnTypePattern Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */