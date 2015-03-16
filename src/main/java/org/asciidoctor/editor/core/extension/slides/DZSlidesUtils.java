package org.asciidoctor.editor.core.extension.slides;

/**
 * Created by mgreau on 04/01/15.
 */
public final class DZSlidesUtils {


    public static final String getTemplateDir(){
        return System.getProperty("jboss.server.data.dir")+"/asciidoctor-backends/slim/dzslides";
    }


    //TODO : Create a docinfo processor

    public static final String getHeaderForSlides(){
        return "<head> " +
                "<meta charset=\"UTF-8\"> "+
                "<title>Real-time collaborative editor for AsciiDoc</title> " +
                "<link rel=\"stylesheet\" href=\"http://fonts.googleapis.com/css?family=Open+Sans:400,700,200,300\"> " +
                "<link rel=\"stylesheet\" href=\"./dzslides/themes/highlight/asciidoctor.css\"> " +
                "<link rel=\"stylesheet\" href=\"./dzslides/themes/style/devnation.css\"> " +
                "<link rel=\"stylesheet\" href=\"./dzslides/core/dzslides.css\"> " +
                "<link rel=\"stylesheet\" href=\"./dzslides/themes/transition/fade.css\"> " +
                "</head>";
    }

    public static final String getBodyFooterForSlides(){
        return "<script src=\"./dzslides/core/dzslides.js\"></script> " +
                "<script src=\"./dzslides/highlight/highlight.pack.js\"></script>"
                + "<script>hljs.initHighlightingOnLoad()</script>";
    }

}
