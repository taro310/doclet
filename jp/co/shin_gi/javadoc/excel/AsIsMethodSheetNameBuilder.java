package jp.co.shin_gi.javadoc.excel;

import java.util.LinkedHashMap;
import java.util.Map;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;

public class AsIsMethodSheetNameBuilder implements IMethodSheetNameBuilder {
    public AsIsMethodSheetNameBuilder() {
    }

    private static class BaseName {
        private Long m_count;
        private String m_name;

        private BaseName() {
        }

        public Long getCount() {
            return this.m_count;
        }

        public String getName() {
            return this.m_name;
        }

        public void setCount(Long count) {
            this.m_count = count;
        }

        public void setName(String name) {
            this.m_name = name;
        }
    }

    private Map<String, Long> m_baseNameMap = new LinkedHashMap();

    private Map<String, BaseName> m_methodMap = new LinkedHashMap();

    @Override
    public void build(ExcelContext context, ClassDoc classDoc) {
        ConstructorDoc[] constructors = classDoc.constructors();
        for (int i = 0; i < constructors.length; i++) {
            buildExecutableMemberDoc(context, constructors[i]);
        }
        MethodDoc[] methods = classDoc.methods();
        for (int i = 0; i < methods.length; i++) {
            buildExecutableMemberDoc(context, methods[i]);
        }
    }

    protected void buildExecutableMemberDoc(ExcelContext context, ExecutableMemberDoc memberDoc) {
        String bn = computeBaseName(memberDoc);
        Long count = this.m_baseNameMap.get(bn);
        if (count == null) {
            count = new Long(1L);
        } else {
            count = new Long(count.longValue() + 1L);
        }
        this.m_baseNameMap.put(bn, count);

        String name = memberDoc.name() + memberDoc.signature();
        BaseName baseName = new BaseName();
        baseName.setName(bn);
        baseName.setCount(count);
        this.m_methodMap.put(name, baseName);
    }

    protected String computeBaseName(ExecutableMemberDoc memberDoc) {
        String name = memberDoc.name();
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        return name;
    }

    @Override
    public String get(ExcelContext context, ExecutableMemberDoc memberDoc) {
        String bn = computeBaseName(memberDoc);
        Long count = this.m_baseNameMap.get(bn);
        if (count.longValue() == 1L) {
            return bn;
        }
        String name = memberDoc.name() + memberDoc.signature();
        BaseName baseName = this.m_methodMap.get(name);
        return baseName.getName() + "(" + String.valueOf(baseName.getCount()) + ")";
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name:
 * jp.co.shin_gi.javadoc.excel.AsIsMethodSheetNameBuilder Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */