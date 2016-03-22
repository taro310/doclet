package jp.co.shin_gi.javadoc;

import java.io.IOException;
import java.io.StringReader;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class SimpleHtmlParser {
    public SimpleHtmlParser() {
    }

    private static class ExCallback extends HTMLEditorKit.ParserCallback {
        private StringBuilder m_buffer = new StringBuilder();

        private ExCallback() {
        }

        @Override
        public void handleEndTag(HTML.Tag t, int pos) {
        }

        @Override
        public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
            if (t.equals(HTML.Tag.BR)) {
                this.m_buffer.append("\r\n");
            }
        }

        @Override
        public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        }

        @Override
        public void handleText(char[] data, int pos) {
            String s = new String(data);
            this.m_buffer.append(s);
        }

        @Override
        public String toString() {
            return this.m_buffer.toString();
        }
    }

    private static class ExKit extends HTMLEditorKit {
        private static final long serialVersionUID = 1L;

        private ExKit() {
        }

        @Override
        public HTMLEditorKit.Parser getParser() {
            return super.getParser();
        }
    }

    public String parse(String html) {
        try {
            ExKit kit = new ExKit();
            HTMLEditorKit.Parser parser = kit.getParser();
            ExCallback callback = new ExCallback();
            parser.parse(new StringReader(html), callback, true);
            return callback.toString();
        } catch (IOException e) {
            throw new JavadocSystemException("HTML構文解析に失敗しました。", e);
        }
    }
}

/*
 * Location: C:\pleiades\workspace\DOCLET\src\javadocto-1.00.jar Qualified Name: jp.co.shin_gi.javadoc.SimpleHtmlParser
 * Java Class Version: 7 (51.0) JD-Core Version: 0.7.0.1
 */