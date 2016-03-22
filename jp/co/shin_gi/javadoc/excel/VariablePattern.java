package jp.co.shin_gi.javadoc.excel;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.shin_gi.javadoc.Context;
import jp.co.shin_gi.javadoc.IExpressionPattern;

import org.apache.poi.ss.usermodel.Cell;

import com.sun.javadoc.ProgramElementDoc;

public class VariablePattern implements IExpressionPattern<Cell> {
    private final String m_name;
    private final Pattern m_pattern;
    private final String m_value;

    public VariablePattern(String name, String value) {
        this.m_name = name;
        this.m_value = value;
        this.m_pattern = Pattern.compile("\\{" + Pattern.quote(this.m_name) + "\\}");
    }

    @Override
    public boolean match(Context<Cell> context, ProgramElementDoc ped, Cell target, String value) {
        Matcher m = this.m_pattern.matcher(value);
        if (!m.matches()) {
            return false;
        }

        DateFormat[] formatters = {DateFormat.getDateInstance(),
                                   DateFormat.getTimeInstance(),
                                   DateFormat.getDateTimeInstance()};

        for (int i = 0; i < formatters.length; i++) {
            ParsePosition pp = new ParsePosition(0);
            Date d = formatters[i].parse(this.m_value, pp);
            if (pp.getErrorIndex() == -1) {
                target.setCellValue(d);
                return true;
            }
        }

        try {
            double d = Double.parseDouble(this.m_value);
            target.setCellValue(d);
            return true;

        } catch (NumberFormatException localNumberFormatException) {
            target.setCellValue(this.m_value);
        }
        return true;
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.excel.VariablePattern Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */