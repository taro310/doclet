package jp.co.shin_gi.javadoc.excel;

import jp.co.shin_gi.javadoc.Context;
import jp.co.shin_gi.javadoc.SimpleHtmlParser;

import org.apache.poi.ss.usermodel.Cell;

import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Tag;

public class InlineTagsSimpleConverter {
    public InlineTagsSimpleConverter() {
    }

    public void convert(Context<Cell> context, ProgramElementDoc ped, Cell target, Tag[] inlines) {
        SimpleHtmlParser htmlParser = new SimpleHtmlParser();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inlines.length; i++) {
            if ((inlines[i] instanceof SeeTag)) {
                SeeTag see = (SeeTag) inlines[i];
                String className = see.referencedClassName();
                String memberName = see.referencedMemberName();

                if (className != null) {
                    sb.append(className);
                }
                if (memberName != null) {
                    if (className != null) {
                        sb.append("#");
                    }
                    sb.append(memberName);
                }
            } else {
                String text = htmlParser.parse(inlines[i].text());
                sb.append(text);
            }
        }
        target.setCellValue(sb.toString());
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.excel.InlineTagsSimpleConverter Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */