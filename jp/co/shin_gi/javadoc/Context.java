package jp.co.shin_gi.javadoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sun.javadoc.RootDoc;

public class Context<T> {
    private String m_outputDirectoryPath;
    private String m_outputFileName;
    private List<IExpressionPattern<T>> m_patternList = new ArrayList();

    private RootDoc m_root;

    private String m_templateFilePath;

    private Map<String, String> m_varmap = new LinkedHashMap();

    public Context() {
    }

    public void addExpressionPattern(IExpressionPattern<T> pattern) {
        this.m_patternList.add(pattern);
    }

    public void clearExpressionPattern() {
        this.m_patternList.clear();
    }

    public Iterator<IExpressionPattern<T>> expressionPatternIterator() {
        return this.m_patternList.iterator();
    }

    public String getOutputDirectoryPath() {
        return this.m_outputDirectoryPath;
    }

    public String getOutputFileName() {
        return this.m_outputFileName;
    }

    public String getOutputFilePath() {
        File parent = new File(getOutputDirectoryPath());
        File file = new File(parent, getOutputFileName());
        return file.getAbsolutePath();
    }

    public RootDoc getRoot() {
        return this.m_root;
    }

    public String getTemplateFilePath() {
        return this.m_templateFilePath;
    }

    public String getVariable(String name) {
        String value = this.m_varmap.get(name);
        if (value == null)
            value = "";
        return value;
    }

    public String[] getVariableNames() {
        String[] names = new String[this.m_varmap.size()];
        this.m_varmap.keySet().toArray(names);
        return names;
    }

    public void putVariable(String name, String value) {
        this.m_varmap.put(name, value);
    }

    public void setOutputDirectoryPath(String outputDirectoryPath) {
        this.m_outputDirectoryPath = outputDirectoryPath;
    }

    public void setOutputFileName(String outputFileName) {
        this.m_outputFileName = outputFileName;
    }

    public void setRoot(RootDoc root) {
        this.m_root = root;
    }

    public void setTemplateFilePath(String templateFilePath) {
        this.m_templateFilePath = templateFilePath;
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name: jp.co.shin_gi.javadoc.Context Java
 * Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */