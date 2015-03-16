package org.asciidoctor.editor.realtime.messages.events;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.ManagedBean;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.asciidoctor.editor.core.extension.slides.DZSlidesUtils;
import org.asciidoctor.ast.DocumentHeader;

import org.asciidoctor.editor.core.AsciidoctorProcessor;
import org.asciidoctor.editor.core.qualifier.Backend;
import org.asciidoctor.editor.core.qualifier.ComputeDiff;
import org.asciidoctor.editor.core.qualifier.Patch;
import org.asciidoctor.editor.core.diff.DiffAdoc;
import org.asciidoctor.editor.core.diff.DiffProvider;
import org.asciidoctor.editor.realtime.BackendServerEndpoint;
import org.asciidoctor.editor.realtime.messages.OutputMessage;
import org.asciidoctor.editor.realtime.messages.TypeFormat;
import org.asciidoctor.editor.realtime.messages.TypeMessage;

/**
 * Consumer for all asciidoc messages.
 * 
 * @author mgreau
 *
 */
@ManagedBean
public class AsciidocMessageConsumer {
	
	@Inject
	private Logger logger;
	
	@Inject
	AsciidoctorProcessor processor; 
	
	//@Inject @DiffProvider("Google")
	//DiffAdoc diffGoogle;

	public void diffEvent(@Observes @ComputeDiff AsciidocMessageEvent event) {
		logger.info("::event:: received computeDiff event message");
		
	//	event.msg.setAdocSourceToMerge(diffGoogle.rawDiff(event.msg.getAdocSource(), event.msg.getAdocSourceToMerge()));
		event.msg.setType(TypeMessage.diff);
		event.msg.setAdocId(event.id);
		event.msg.setFormat(TypeFormat.asciidoc);
		
		BackendServerEndpoint.sendMessage(event.session, event.msg, event.id);
	}
	
	public void patchEvent(@Observes @Patch AsciidocMessageEvent event) {
		logger.info("::event:: received patch event message");
		
	//	event.msg.setAdocSourceToMerge(diffGoogle.applyPatch(event.msg.getAdocSource(), event.msg.getPatchToApply()));
		event.msg.setType(TypeMessage.patch);
		event.msg.setAdocId(event.id);
		event.msg.setFormat(TypeFormat.asciidoc);
		
		BackendServerEndpoint.sendMessage(event.session, event.msg, event.id);

	}
	
	public void html5RenderedEvent(@Observes @Backend("html5") AsciidocMessageEvent event){
		logger.info("::event:: received html5 event message");
		OutputMessage html = buildOutputMessage(TypeFormat.html5, event);
		
		long start = System.currentTimeMillis();
		try {
			html.setContent(processor.convertToDocument(event.msg.getAdocSource()));
			html.setTimeToRender(System.currentTimeMillis() - start);
		} catch (RuntimeException rEx) {
			logger.severe("processing error." + rEx.getCause().toString());
		}

		// send the new HTML version to all connected peers
		BackendServerEndpoint.sendMessage(html, event.id);
	}
	
	/**
	 * 
	 * @param event
	 */
	public void dzslidesRenderedEvent(@Observes @Backend("dzslides") AsciidocMessageEvent event){
		logger.info("::event:: received dzslides event message");
		OutputMessage html = buildOutputMessage(TypeFormat.html5, event);
		
		final String templateDir = DZSlidesUtils.getTemplateDir();
		
		long start = System.currentTimeMillis();
		try {
			html.setContent(processor.convertToDocument(event.msg.getAdocSource(), "dzslides",
                    new java.io.File(templateDir), event.msg.getPart(), null));
			html.setTimeToRender(System.currentTimeMillis() - start);
		} catch (RuntimeException rEx) {
			logger.severe("processing error." + rEx.toString());
		}
		
		// send the new HTML version to all connected peers
		BackendServerEndpoint.sendMessage(html, event.id);
	}
	
	public void pdfRenderedEvent(@Observes @Backend("pdf") AsciidocMessageEvent event){
		logger.info("::event:: received pdf event message");
		OutputMessage pdf = buildOutputMessage(TypeFormat.pdf, event);

		long start = System.currentTimeMillis();
		try {
			pdf.setContent(processor.convertToDocument(event.msg.getAdocSource(), TypeFormat.pdf.toString()));
			pdf.setTimeToRender(System.currentTimeMillis() - start);
		} catch (RuntimeException rEx) {
			logger.severe("processing error." + rEx.getCause().toString());
		}

		// send the PDF version to all connected peers
		BackendServerEndpoint.sendMessage(pdf, event.id);
	}
	
	private OutputMessage buildOutputMessage(TypeFormat type, AsciidocMessageEvent event){
		final OutputMessage html = new OutputMessage(type);
		html.setAdocId(event.id);
		html.setType(TypeMessage.output);
		html.setCurrentWriter(event.msg.getCurrentWriter());
		html.setAdocSource(event.msg.getAdocSource());
		html.setTimeToRender(-1);
		html.setDocHeader(checkHeader(event.msg
				.getAdocSource()));
		
		return html;
	}
	
	/**
	 * Check if all required headers are present.
	 * 
	 * @param source the AsciiDoc source sent by the client
	 * @return the DocumentHeader 
	 */
	private DocumentHeader checkHeader (String source){
		//Check if document header is present
		DocumentHeader docHeader = null;
		try {
			logger.info("[RENDER] processing DocumentHeader");

			docHeader = processor.getDelegate().readDocumentHeader(source);
			for (Map.Entry<String, Object> h : docHeader.getAttributes().entrySet()){
				logger.log(Level.FINER, h.getKey() + " : " + h.getValue());
			}
			//FIXME : check which headers are mandatory
			Map<String, Object> headers = docHeader.getAttributes();
			if (docHeader.getAuthors().size() == 0) {
				logger.info("DocHeader add author");
				headers.put("author", "the Author");
				headers.put("email", "test@test.fr");
			}
			//docHeader = DocumentHeader.createDocumentHeader("Doc title", "page title", headers);
			
		} catch (RuntimeException rEx) {
			logger.severe("DocHeader processing error, add custom header" + rEx.getCause().toString());
		}
		
		return docHeader;
				
	}


}
