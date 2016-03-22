package jp.co.shin_gi.javadoc.excel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.shin_gi.javadoc.Context;
import jp.co.shin_gi.javadoc.IExpressionPattern;

import org.apache.poi.ss.usermodel.Cell;

import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;

public class SimplePattern implements IExpressionPattern<Cell> {
    private final String m_keyword;
    private final Pattern m_pattern;

    public SimplePattern(String keyword) {
        this.m_keyword = keyword;
        this.m_pattern = Pattern.compile("\\{" + Pattern.quote(keyword) + "(\\[([0-9]+)\\])?\\}");
    }

    @Override
    public boolean match(Context<Cell> context, ProgramElementDoc ped, Cell target, String value) {
        Matcher m = this.m_pattern.matcher(value);
        if (!m.matches()) {
            return false;
        }

        Tag[] tags = ped.tags(this.m_keyword);
        if ((tags == null) || (tags.length == 0)) {
            target.setCellValue("");
            return true;
        }

        String index_s = m.group(2);
        Tag tag;
        if (index_s == null) {
            tag = tags[0];
        } else {
            int index = Integer.parseInt(index_s);
            if (index < tags.length) {
                tag = tags[index];
            } else {
                target.setCellValue("");
                return true;
            }
        }
        Tag[] inlines = tag.inlineTags();
        new InlineTagsSimpleConverter().convert(context, ped, target, inlines);

        return true;
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.excel.SimplePattern Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */