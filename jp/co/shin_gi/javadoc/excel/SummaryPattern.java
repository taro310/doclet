package jp.co.shin_gi.javadoc.excel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.shin_gi.javadoc.Context;
import jp.co.shin_gi.javadoc.IExpressionPattern;

import org.apache.poi.ss.usermodel.Cell;

import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;

public class SummaryPattern implements IExpressionPattern<Cell> {
    private Pattern m_pattern = Pattern.compile("\\{summary\\}");

    public SummaryPattern() {
    }

    @Override
    public boolean match(Context<Cell> context, ProgramElementDoc ped, Cell target, String value) {
        Matcher m = this.m_pattern.matcher(value);
        if (!m.matches()) {
            return false;
        }
        Tag[] inlines = ped.firstSentenceTags();
        new InlineTagsSimpleConverter().convert(context, ped, target, inlines);
        return true;
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.excel.SummaryPattern Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */