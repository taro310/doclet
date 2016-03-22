package jp.co.shin_gi.javadoc.excel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.shin_gi.javadoc.Context;
import jp.co.shin_gi.javadoc.IExpressionPattern;

import org.apache.poi.ss.usermodel.Cell;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MemberDoc;
import com.sun.javadoc.ProgramElementDoc;

public class QualifiedClassPattern implements IExpressionPattern<Cell> {
    private Pattern m_pattern = Pattern.compile("\\{qualifiedclass\\}");

    public QualifiedClassPattern() {
    }

    @Override
    public boolean match(Context<Cell> context, ProgramElementDoc ped, Cell target, String value) {
        Matcher m = this.m_pattern.matcher(value);
        if (!m.matches())
            return false;
        if ((ped instanceof ClassDoc)) {
            ClassDoc classDoc = (ClassDoc) ped;
            target.setCellValue(classDoc.qualifiedName());
        } else if ((ped instanceof MemberDoc)) {
            MemberDoc memberDoc = (MemberDoc) ped;
            target.setCellValue(memberDoc.containingClass().qualifiedName());
        }
        return true;
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.excel.QualifiedClassPattern Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */