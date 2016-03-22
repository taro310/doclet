package jp.co.shin_gi.javadoc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import jp.co.shin_gi.javadoc.excel.AsIsMethodSheetNameBuilder;
import jp.co.shin_gi.javadoc.excel.ClassPattern;
import jp.co.shin_gi.javadoc.excel.CommentPattern;
import jp.co.shin_gi.javadoc.excel.ExcelContext;
import jp.co.shin_gi.javadoc.excel.IMethodSheetNameBuilder;
import jp.co.shin_gi.javadoc.excel.LogicalPattern;
import jp.co.shin_gi.javadoc.excel.ManualPattern;
import jp.co.shin_gi.javadoc.excel.MethodPattern;
import jp.co.shin_gi.javadoc.excel.ModifiersPattern;
import jp.co.shin_gi.javadoc.excel.PackagePattern;
import jp.co.shin_gi.javadoc.excel.ParameterCommentPattern;
import jp.co.shin_gi.javadoc.excel.ParameterNamePattern;
import jp.co.shin_gi.javadoc.excel.ParameterTypePattern;
import jp.co.shin_gi.javadoc.excel.QualifiedClassPattern;
import jp.co.shin_gi.javadoc.excel.ReturnCommentPattern;
import jp.co.shin_gi.javadoc.excel.ReturnTypePattern;
import jp.co.shin_gi.javadoc.excel.SimplePattern;
import jp.co.shin_gi.javadoc.excel.SummaryPattern;
import jp.co.shin_gi.javadoc.excel.ThrowsCommentPattern;
import jp.co.shin_gi.javadoc.excel.ThrowsTypePattern;
import jp.co.shin_gi.javadoc.excel.VariablePattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;

public class ToExcel {
    public static final String OPTION_CLASSSHEET = "-classsheet";
    public static final String OPTION_OUT = "-out";
    public static final String OPTION_TAG = "-tag";
    public static final String OPTION_TEMPLATE = "-template";
    public static final String OPTION_VAR = "-var";

    public ToExcel() {
    }

    public static int optionLength(String option) {
        if (option.equalsIgnoreCase("-template")) {
            return 2;
        }
        if (option.equalsIgnoreCase("-out")) {
            return 2;
        }
        if (option.equalsIgnoreCase("-tag")) {
            return 2;
        }
        if (option.equalsIgnoreCase("-var")) {
            return 2;
        }
        if (option.equalsIgnoreCase("-classsheet")) {
            return 2;
        }

        return 0;
    }

    public static boolean start(RootDoc root) {
        ToExcel generator = new ToExcel();
        boolean retval = generator.generate(root);
        return retval;
    }

    public static boolean validOptions(String[][] options, DocErrorReporter reporter) {
        boolean foundTemplate = false;
        boolean foundOut = false;
        for (int i = 0; i < options.length; i++) {
            String[] option = options[i];
            if (option[0].equalsIgnoreCase("-template")) {
                if (foundTemplate) {
                    reporter.printError("-templateオプションはひとつだけ指定するようにしてください。");
                    return false;
                }
                foundTemplate = true;
                String name = option[1];
                File f = new File(name);
                if (!f.exists()) {
                    reporter.printError(String.format(
                        "存在しているテンプレートファイルを指定してください。指定したテンプレートファイルは{%s}です。",
                        new Object[]{name}));
                    return false;
                }
            } else if (option[0].equalsIgnoreCase("-out")) {
                if (foundOut) {
                    reporter.printError("-outオプションはひとつだけ指定するようにしてください。");
                    return false;
                }
                foundOut = true;
                String name = option[1];
                File f = new File(name);
                if (!f.exists()) {
                    reporter.printError(String.format(
                        "存在している出力先ディレクトリを指定してください。指定した出力先ディレクトリは{%s}です。",
                        new Object[]{name}));
                    return false;
                }
                if (!f.canWrite()) {
                    reporter
                        .printError(String.format("出力可能なディレクトリを指定してください。指定した出力先ディレクトリは{%s}です。", new Object[]{name}));
                    return false;
                }
            } else if (!option[0].equalsIgnoreCase("-tag")) {
                if (option[0].equalsIgnoreCase("-var")) {
                    String expression = option[1];
                    int pos = expression.indexOf("=");
                    if (pos == -1)
                        reporter.printError(String.format(
                            "-varオプションには、イコールを含む式を指定してください。指定した値は{%s}です。",
                            new Object[]{expression}));
                } else {
                    option[0].equalsIgnoreCase("-classsheet");
                }
            }
        }

        if (!foundTemplate) {
            reporter.printError("-templateオプションを指定してください。");
        }
        if (!foundOut) {
            reporter.printError("-outオプションを指定してください。");
        }
        if ((!foundTemplate) || (!foundOut)) {
            return false;
        }

        return true;
    }

    protected void actionPerClassDoc(ExcelContext context, ClassDoc classDoc) {
        String outputFileName = buildOutputFileName(context, classDoc);
        context.setOutputFileName(outputFileName);

        context.getRoot().printNotice(context.getOutputFilePath() + "の生成中...");

        prepareExistingWorkbook(context);

        prepareTargetWorkbook(context);

        IMethodSheetNameBuilder msnb = createMethodSheetNameBuilder(context);
        msnb.build(context, classDoc);
        context.setMethodSheetNameBuilder(msnb);

        Workbook book = context.getTargetWorkbook();
        Sheet sheet = book.cloneSheet(context.getClassTemplateIndex());
        int methodindex = book.getSheetIndex(sheet.getSheetName());
        book.setSheetName(methodindex, context.getClassSheetName());
        Iterator<Cell> c;
        for (Iterator<Row> r = sheet.iterator(); r.hasNext(); c.hasNext()) {
            Row row = r.next();
            c = row.cellIterator();
//            continue;
            Cell cell = c.next();
            String value = cell.getStringCellValue();
            for (Iterator<IExpressionPattern<Cell>> ep = context.expressionPatternIterator(); ep.hasNext();) {
                IExpressionPattern<Cell> pattern = ep.next();
                boolean match = pattern.match(context, classDoc, cell, value);
                if (match) {
                    break;
                }
            }
        }

        ConstructorDoc[] constructors = classDoc.constructors();
        for (int i = 0; i < constructors.length; i++) {
            actionPerExecutableMemberDoc(context, constructors[i]);
        }

        MethodDoc[] methods = classDoc.methods();
        Set<MethodDoc> set = new TreeSet();
        for (int i = 0; i < methods.length; i++) {
            set.add(methods[i]);
        }
        for (Iterator<MethodDoc> i = set.iterator(); i.hasNext();) {
            MethodDoc md = i.next();
            actionPerExecutableMemberDoc(context, md);
        }

        book.removeSheetAt(context.getClassTemplateIndex());
        book.removeSheetAt(context.getMethodTemplateIndex());
        book.setActiveSheet(book.getSheetIndex(context.getClassSheetName()));

        writeWorkbook(context, book);
    }

    protected void actionPerExecutableMemberDoc(ExcelContext context, ExecutableMemberDoc memberDoc) {
        Workbook book = context.getTargetWorkbook();
        Sheet sheet = book.cloneSheet(context.getMethodTemplateIndex());
        int methodindex = book.getSheetIndex(sheet.getSheetName());
        String sheetName = context.getMethodSheetNameBuilder().get(context, memberDoc);
        book.setSheetName(methodindex, sheetName);
        Iterator<Cell> c;
        for (Iterator<Row> r = sheet.iterator(); r.hasNext();

        c.hasNext()) {
            Row row = r.next();
            c = row.cellIterator();
//            continue;
            Cell cell = c.next();
            if (cell.getCellType() == 1) {
                String value = cell.getStringCellValue();
                for (Iterator<IExpressionPattern<Cell>> ep = context.expressionPatternIterator(); ep.hasNext();) {
                    IExpressionPattern<Cell> pattern = ep.next();
                    boolean match = pattern.match(context, memberDoc, cell, value);
                    if (match) {
                        break;
                    }
                }
            }
        }
    }

    protected ExcelContext buildContext(RootDoc root) {
        ExcelContext context = new ExcelContext();
        context.setRoot(root);
        String[][] options = root.options();
        for (int i = 0; i < options.length; i++) {
            if (options[i][0].equalsIgnoreCase("-template")) {
                context.setTemplateFilePath(options[i][1]);
            } else if (options[i][0].equalsIgnoreCase("-out")) {
                context.setOutputDirectoryPath(options[i][1]);
            } else if (options[i][0].equalsIgnoreCase("-var")) {
                String expression = options[i][1];
                String[] tokens = expression.split("=", 2);
                context.putVariable(tokens[0], tokens.length > 1 ? tokens[1] : "");
            } else if (options[i][0].equalsIgnoreCase("-classsheet")) {
                context.setClassSheetName(options[i][1]);
            }
        }
        return context;
    }

    protected String buildOutputFileName(ExcelContext context, ClassDoc classDoc) {
        try {
            String builderClassName =
                System.getProperty(IFileNameBuilder.class.getName(), QualifiedFileNameBuilder.class.getCanonicalName());
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> clazz = loader.loadClass(builderClassName);
            IFileNameBuilder fnb = (IFileNameBuilder) clazz.newInstance();
            return fnb.buildFileName(context, classDoc);
        } catch (ClassNotFoundException e) {
            throw new JavadocSystemException(IFileNameBuilder.class.getName() + "プロパティには、存在するクラスを指定してください。", e);
        } catch (InstantiationException e) {
            throw new JavadocSystemException(
                IFileNameBuilder.class.getName() + "プロパティには、デフォルトコンストラクタのあるクラスを指定してください。",
                e);
        } catch (IllegalAccessException e) {
            throw new JavadocSystemException(IFileNameBuilder.class.getName() + "プロパティには、アクセス可能なクラスを指定してください。", e);
        }
    }

    protected IMethodSheetNameBuilder createMethodSheetNameBuilder(ExcelContext context) {
        try {
            String builderClassName =
                System.getProperty(
                    IMethodSheetNameBuilder.class.getName(),
                    AsIsMethodSheetNameBuilder.class.getCanonicalName());
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> clazz = loader.loadClass(builderClassName);
            return (IMethodSheetNameBuilder) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new JavadocSystemException(IMethodSheetNameBuilder.class.getName() + "プロパティには、存在するクラスを指定してください。", e);
        } catch (InstantiationException e) {
            throw new JavadocSystemException(IMethodSheetNameBuilder.class.getName()
                + "プロパティには、デフォルトコンストラクタのあるクラスを指定してください。", e);
        } catch (IllegalAccessException e) {
            throw new JavadocSystemException(
                IMethodSheetNameBuilder.class.getName() + "プロパティには、アクセス可能なクラスを指定してください。",
                e);
        }
    }

    public boolean generate(RootDoc root) {
        ExcelContext context = buildContext(root);

        context.clearExpressionPattern();

        String[] names = context.getVariableNames();
        for (int i = 0; i < names.length; i++) {
            context.addExpressionPattern(new VariablePattern(names[i], context.getVariable(names[i])));
        }

        String[][] options = root.options();
        Set<String> customtagSet = new LinkedHashSet();
        for (int i = 0; i < options.length; i++) {
            if (options[i][0].equalsIgnoreCase("-tag"))
                customtagSet.add(options[i][1]);
        }
        for (Iterator<String> i = customtagSet.iterator(); i.hasNext();) {
            String customtag = i.next();
            context.addExpressionPattern(new SimplePattern(customtag));
        }

        context.addExpressionPattern(new SimplePattern("author"));
        context.addExpressionPattern(new SimplePattern("since"));
        context.addExpressionPattern(new SimplePattern("version"));
        context.addExpressionPattern(new SimplePattern("see"));
        context.addExpressionPattern(new SimplePattern("deprecated"));
        context.addExpressionPattern(new ClassPattern());
        context.addExpressionPattern(new QualifiedClassPattern());
        context.addExpressionPattern(new PackagePattern());
        context.addExpressionPattern(new MethodPattern());
        context.addExpressionPattern(new SummaryPattern());
        context.addExpressionPattern(new CommentPattern());
        context.addExpressionPattern(new ParameterTypePattern());
        context.addExpressionPattern(new ParameterNamePattern());
        context.addExpressionPattern(new ParameterCommentPattern());
        context.addExpressionPattern(new ModifiersPattern());
        context.addExpressionPattern(new ReturnTypePattern());
        context.addExpressionPattern(new ReturnCommentPattern());
        context.addExpressionPattern(new ThrowsTypePattern());
        context.addExpressionPattern(new ThrowsCommentPattern());
        context.addExpressionPattern(new LogicalPattern());
        context.addExpressionPattern(new ManualPattern());

        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; i++) {
            actionPerClassDoc(context, classes[i]);
        }
        return true;
    }

    protected void prepareExistingWorkbook(ExcelContext context) {
        try {
            Workbook existingWorkbook = null;
            File outfile = new File(context.getOutputFilePath());
            if ((outfile.exists()) && (outfile.isFile())) {
                existingWorkbook = WorkbookFactory.create(outfile);
            }
            context.setExistingWorkbook(existingWorkbook);
        } catch (InvalidFormatException e) {
            throw new JavadocSystemException(String.format(
                "生成するファイルが壊れている可能性があります。EXCELで開けるかご確認ください。対象ファイルは、{%s}です。",
                new Object[]{context.getOutputFilePath()}), e);
        } catch (IOException e) {
            throw new JavadocSystemException(String.format(
                "生成するファイルが壊れている可能性があります。EXCELで開けるかご確認ください。対象ファイルは、{%s}です。",
                new Object[]{context.getOutputFilePath()}), e);
        }
    }

    protected void prepareTargetWorkbook(ExcelContext context) {
        try {
            Workbook book = WorkbookFactory.create(new File(context.getTemplateFilePath()));
            context.setTargetWorkbook(book);
        } catch (InvalidFormatException e) {
            throw new JavadocSystemException(String.format(
                "テンプレートファイルが壊れている可能性があります。EXCELで開けるかご確認ください。対象ファイルは、{%s}です。",
                new Object[]{context.getOutputFilePath()}), e);
        } catch (IOException e) {
            throw new JavadocSystemException(String.format(
                "テンプレートファイルが壊れている可能性があります。EXCELで開けるかご確認ください。対象ファイルは、{%s}です。",
                new Object[]{context.getOutputFilePath()}), e);
        }
    }

    protected void writeWorkbook(ExcelContext context, Workbook book) {
        try {
            File f = new File(context.getOutputFilePath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                book.write(fos);
            } finally {
                if (fos != null)
                    try {
                        fos.close();
                    } catch (IOException localIOException1) {
                    }
            }
            try {
                fos.close();
            } catch (IOException localIOException2) {
            }

            return;
        } catch (FileNotFoundException e) {
            throw new JavadocSystemException(String.format(
                "出力先ファイルを開けませんでした。出力先のパスを確認してください。出力先のパスは{%s}です。",
                new Object[]{context.getOutputFilePath()}), e);
        } catch (IOException e) {
            throw new JavadocSystemException(String.format(
                "ファイルを出力できませんでした。出力先のパスを確認してください。出力先のパスは{%s}です。",
                new Object[]{context.getOutputFilePath()}), e);
        }
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name: jp.co.shin_gi.javadoc.ToExcel Java
 * Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */