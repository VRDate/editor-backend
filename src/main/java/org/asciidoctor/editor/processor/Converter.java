package org.asciidoctor.editor.processor;


public enum Converter {

    asciidoc(".adoc"), html5(".html"), pdf(".pdf"), slide(".html");

    private String filenameExtension;
    private Converter(String extension){
        filenameExtension = extension;
    }

    public final String getFilenameExtension(){
        return filenameExtension;
    }

}
