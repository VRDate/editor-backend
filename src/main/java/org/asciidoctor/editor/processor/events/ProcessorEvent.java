package org.asciidoctor.editor.processor.events;

import org.asciidoctor.editor.processor.Converter;

import javax.validation.constraints.NotNull;

public class ProcessorEvent {

    String asciidoc;

    String filename;

    Converter converter;

    public ProcessorEvent(@NotNull String adoc, @NotNull Converter c, @NotNull String filename) {
        this.asciidoc = adoc;
        this.converter = c;
        this.filename = filename;
    }


}
