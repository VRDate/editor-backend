package org.asciidoctor.editor.realtime.messages;

import org.asciidoctor.editor.processor.Converter;

/**
 * An OuputMessage is a result of :
 * 
 * The Asciidoctor processor parses an AsciiDoc document and translates it into
 * a variety of formats.
 * 
 * @author greau.maxime@gmail.com
 * 
 */
public class OutputMessage extends AsciidocMessage {
	
	/** time to process the file into output type */
	private long timeToRender;
	
	/** rendered by asciidoctor processor */
	private String content;

	public OutputMessage(Converter format) {
		this.format = format;
	}
	
	public String toString(){
		return "[OutputMessage][format]" + format + " - Rendered doc in: ..." + timeToRender;
	}
	
	public long getTimeToRender() {
		return timeToRender;
	}

	public void setTimeToRender(long timeToRender) {
		this.timeToRender = timeToRender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
