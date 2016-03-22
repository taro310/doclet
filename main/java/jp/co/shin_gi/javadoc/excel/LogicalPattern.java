package jp.co.shin_gi.javadoc.excel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.shin_gi.javadoc.Context;
import jp.co.shin_gi.javadoc.IExpressionPattern;

import org.apache.poi.ss.usermodel.Cell;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ProgramElementDoc;

public class LogicalPattern implements IExpressionPattern<Cell> {
    private Pattern m_pattern =
        Pattern
            .compile("\\{(abstract|externalizable|final|interface|native|packageprivate|private|protected|public|serializable|static|synchronized)(\\?(.*?)(\\:(.*?))?)?\\}");

    public LogicalPattern() {
    }

    @Override
    public boolean match(Context<Cell> context, ProgramElementDoc ped, Cell target, String value) {
        Matcher m = this.m_pattern.matcher(value);
        if (!m.matches()) {
            return false;
        }
        String keyword = m.group(1);
        String t = m.group(3);
        String f = m.group(5);

        if (keyword.equals("public")) {
            setCellValue(target, ped.isPublic(), t, f);
            return true;
        }
        if (keyword.equals("protected")) {
            setCellValue(target, ped.isProtected(), t, f);
            return true;
        }
        if (keyword.equals("packageprivate")) {
            setCellValue(target, ped.isPackagePrivate(), t, f);
            return true;
        }
        if (keyword.equals("private")) {
            setCellValue(target, ped.isPrivate(), t, f);
            return true;
        }
        if (keyword.equals("final")) {
            setCellValue(target, ped.isFinal(), t, f);
            return true;
        }
        if (keyword.equals("static")) {
            setCellValue(target, ped.isStatic(), t, f);
            return true;
        }
        if (keyword.equals("interface")) {
            setCellValue(target, ped.isInterface(), t, f);
            return true;
        }

        if ((ped instanceof ExecutableMemberDoc)) {
            ExecutableMemberDoc emd = (ExecutableMemberDoc) ped;

            if (keyword.equals("synchronized")) {
                setCellValue(target, emd.isSynchronized(), t, f);
                return true;
            }
            if (keyword.equals("native")) {
                setCellValue(target, emd.isNative(), t, f);
                return true;
            }
        }

        if ((ped instanceof MethodDoc)) {
            MethodDoc md = (MethodDoc) ped;

            if (keyword.equals("abstract")) {
                setCellValue(target, md.isAbstract(), t, f);
                return true;
            }
        }

        if ((ped instanceof ClassDoc)) {
            ClassDoc cd = (ClassDoc) ped;

            if (keyword.equals("abstract")) {
                setCellValue(target, cd.isAbstract(), t, f);
                return true;
            }
            if (keyword.equals("externalizable")) {
                setCellValue(target, cd.isExternalizable(), t, f);
                return true;
            }
            if (keyword.equals("serializable")) {
                setCellValue(target, cd.isSerializable(), t, f);
                return true;
            }
        }

        target.setCellValue(value);
        return true;
    }

    protected void setCellValue(Cell target, boolean b, String t, String f) {
        if (b) {
            target.setCellValue(t);
        } else {
            target.setCellValue(f);
        }
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.excel.LogicalPattern Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */