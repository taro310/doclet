package doclet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import jp.co.shin_gi.javadoc.SimpleHtmlParser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Tag;

/**
 * カスタムドックレットCSV出力.<br/>
 * ICM_COREのメソッド一覧を出力する.
 */
public class MethodListCsvDoclet {

    // entry point
    public static boolean start(RootDoc rootDoc) {
        System.out.println("doclet開始");
        File file = new File("C:\\pleiades\\workspace\\ICM_CORE\\ICM_CORE_メソッド一覧.csv");
        try {
            PrintWriter out = new PrintWriter(file);
            try {
                writeTo(out, rootDoc);
                if (out.checkError())
                    return false;
            } finally {
                out.close();
                System.out.println("doclet終了");
            }
        } catch (IOException ex) {
            System.out.println("writeできなかった");
            throw new RuntimeException(ex);
        }
        return true;
    }

    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }

    private static void writeTo(PrintWriter out, RootDoc rootDoc) {
        SimpleHtmlParser htmlParser = new SimpleHtmlParser();

        // header
        out.println(join(new String[]{"パッケージ",
                                      "クラス名（物理）",
                                      "クラス名（論理）",
                                      "クラスの説明",
                                      "メソッド名（物理）",
                                      "メソッド名（論理）",
                                      "メソッドの説明",
                                      "引数",
                                      "引数の説明",
                                      "戻り値",
                                      "戻り値の説明",
                                      "修飾子",
                                      "完全修飾クラス名",
                                      "版(since)"}));
        // body
        for (ClassDoc classDoc : rootDoc.classes()) {
            for (MethodDoc methodDoc : classDoc.methods(true)) {
                String[] a = new String[14];
                Arrays.fill(a, "");
                int i = -1;

                // class
                a[++i] = classDoc.containingPackage().name();
                a[++i] = classDoc.name();
                a[++i] = quote(convert(classDoc.firstSentenceTags()));
                Tag[] classTags = classDoc.inlineTags();
                a[++i] =
                    quote(((classTags != null) && (classTags.length > 0)) ? htmlParser.parse(classTags[0].text()) : "");

                // method
                a[++i] = methodDoc.name();
                a[++i] = quote(convert(methodDoc.firstSentenceTags()));

                StringBuilder sb = new StringBuilder();
                Tag[] inlines = methodDoc.inlineTags();
                if (inlines.length < 1) {
                    a[++i] = ("");
                } else {
                    for (int idx = 0; idx < inlines.length; idx++) {
                        String text = (htmlParser.parse(inlines[idx].text())).replace("\"", "\"\"");
                        sb.append(text);
                    }
                    a[++i] = quote(sb.toString());
                }
                // method param
                a[++i] = quote(joinLine(methodDoc.parameters()));
                a[++i] = quote(joinPComment(methodDoc));
                // method return type
                a[++i] = quote(methodDoc.returnType());
                Tag[] rts = methodDoc.tags("return");
                a[++i] = quote(((rts != null) && (rts.length > 0)) ? convert(rts[0].inlineTags()) : "");

                // other
                a[++i] = methodDoc.modifiers();
                a[++i] = classDoc.qualifiedName();
                a[++i] = parseSince(methodDoc, classDoc);
                out.println(join(a));
            }
        }
    }

    /**
     * @param inlines
     * @return
     */
    private static String convert(Tag[] inlines) {
        SimpleHtmlParser htmlParser = new SimpleHtmlParser();
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < inlines.length; i2++) {
            if ((inlines[i2] instanceof SeeTag)) {
                SeeTag see = (SeeTag) inlines[i2];
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
                String text = htmlParser.parse(inlines[i2].text());
                sb.append(text);
            }
        }
        return sb.toString();
    }

    private static String parseSince(Doc... docs) {
        for (Doc doc : docs) {
            String s = join(doc.tags("since")).replace(",", "");
            if (s.contains("@since:"))
                s = s.replaceAll("@since:", "");
            s = s.trim();
            if (!s.isEmpty())
                return s;
        }
        return "";
    }

    private static String quote(Object o) {
        return String.format("\"%s\"", o);
    }

    private static <T> String join(T[] a) {
        if (a.length == 0)
            return "";
        StringBuilder s = new StringBuilder(String.valueOf(a[0]));
        for (int i = 1; i < a.length; i++)
            s.append(",").append(a[i]);
        return s.toString();
    }

    /**
     * 改行コードLF
     */
    private static <T> String joinLine(T[] a) {
        if (a.length == 0)
            return "";
        StringBuilder s = new StringBuilder(String.valueOf(a[0]));
        for (int i = 1; i < a.length; i++)
            s.append(",\n").append(a[i]);
        return s.toString();
    }

    private static String joinPComment(MethodDoc methodDoc) {
        ExecutableMemberDoc emd = (ExecutableMemberDoc) methodDoc;
        Parameter[] parameters = emd.parameters();
        if (parameters.length == 0)
            return "";

        ParamTag[] tags = emd.paramTags();
        StringBuilder strB = new StringBuilder();
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].parameterName().equals(parameters[i].name())) {
                Tag[] inlines = tags[i].inlineTags();
                if (i == 0) {
                    strB = new StringBuilder(String.valueOf(convert(inlines)));
                } else {
                    strB.append(",\n").append(convert(inlines));
                }
            }
        }
        return strB.toString();

    }

}
