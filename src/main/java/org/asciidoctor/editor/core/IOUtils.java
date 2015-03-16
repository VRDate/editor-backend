package org.asciidoctor.editor.core;

import java.io.*;
import java.net.URL;

/**
 * Created by greaum on 16/03/2015.
 */
public class IOUtils {

    public static final String getResourceAsString(String path) {
        try {
            URL resource = Thread.currentThread().getContextClassLoader()
                    .getResource(path);
            if (resource != null) {
                return readFromStream(Thread.currentThread()
                        .getContextClassLoader().getResourceAsStream(path));
            } else {
                throw new RuntimeException(new FileNotFoundException(path));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    public static final String readFromStream(final InputStream is) {
        if (is == null) {
            return "";
        }
        final char[] buffer = new char[1024];
        final StringBuilder out = new StringBuilder();
        try {
            final Reader in = new InputStreamReader(is, "UTF-8");
            try {
                for (;;) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
            } finally {
                in.close();
            }
        } catch (UnsupportedEncodingException ex) {
/* ... */
        } catch (IOException ex) {
/* ... */
        }
        return out.toString();
    }
}
