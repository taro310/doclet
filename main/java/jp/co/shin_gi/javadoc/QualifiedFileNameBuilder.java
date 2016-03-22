package jp.co.shin_gi.javadoc;

import com.sun.javadoc.ClassDoc;

public class QualifiedFileNameBuilder implements IFileNameBuilder {
    public QualifiedFileNameBuilder() {
    }

    @Override
    public String buildFileName(Context<?> context, ClassDoc classDoc) {
        String name = classDoc.qualifiedName();

        String path = context.getTemplateFilePath();
        int pos = path.lastIndexOf(".");
        if (pos != -1) {
            return name + path.substring(pos);
        }
        return name;
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.QualifiedFileNameBuilder Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */