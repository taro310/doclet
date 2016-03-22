package jp.co.shin_gi.javadoc.excel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.shin_gi.javadoc.Context;
import jp.co.shin_gi.javadoc.IExpressionPattern;

import org.apache.poi.ss.usermodel.Cell;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Type;

public class ThrowsTypePattern implements IExpressionPattern<Cell> {
    private Pattern m_pattern = Pattern.compile("\\{throws(\\[([0-9]+)\\])?\\.type\\}");

    public ThrowsTypePattern() {
    }

    @Override
    public boolean match(Context<Cell> context, ProgramElementDoc ped, Cell target, String value) {
        Matcher m = this.m_pattern.matcher(value);
        if (!m.matches()) {
            return false;
        }
        int index = 0;
        if (2 <= m.groupCount()) {
            String index_s = m.group(2);
            if (index_s != null) {
                index = Integer.parseInt(index_s);
            }
        }

        if ((ped instanceof ExecutableMemberDoc)) {
            ExecutableMemberDoc emd = (ExecutableMemberDoc) ped;
            Type[] types = emd.thrownExceptionTypes();
            if (index < types.length) {
                Type type = types[index];
                target.setCellValue(type.qualifiedTypeName() + type.dimension());
                return true;
            }
        }

        target.setCellValue(value);
        return true;
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.excel.ThrowsTypePattern Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */