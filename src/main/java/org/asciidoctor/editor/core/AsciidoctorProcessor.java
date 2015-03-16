package org.asciidoctor.editor.core;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.asciidoctor.editor.core.extension.iframe.IFrameAnchorPostProcessor;
import org.asciidoctor.editor.core.extension.slides.DZSlidesPostProcessor;
import org.asciidoctor.editor.core.extension.slides.DZSlidesUtils;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.asciidoctor.ast.ContentPart;
import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.ast.StructuredDocument;
import org.asciidoctor.extension.JavaExtensionRegistry;

import static org.asciidoctor.OptionsBuilder.options;

/**
 * 
 * @author <a href="mailto:greau.maxime@gmail.com">Maxime Gréau</a>
 *
 */
@Stateless
public class AsciidoctorProcessor {

    @Inject
	private Logger logger;

    @Inject
	private Asciidoctor asciidoctor ;

    private String outputDir;

	@PostConstruct
	public void init() {

		//asciidoctor = Asciidoctor.Factory.create();
        outputDir = "/opt/jboss/wildfly/standalone/data/";//System.getenv("JBOSS_HOME");
	}

    /**
     * Register a post processor depends on the backend.
     *
     * @param backend
     */
    private void addPostProcessor(String backend){
        if (backend.equals("html5")){
            JavaExtensionRegistry javaExtensionRegistry = this.asciidoctor
                    .javaExtensionRegistry();
            javaExtensionRegistry
                    .postprocessor(IFrameAnchorPostProcessor.class);
        } else if (backend.equals("dzslides")){
            JavaExtensionRegistry javaExtensionRegistry = this.asciidoctor
                    .javaExtensionRegistry();
            javaExtensionRegistry
                    .postprocessor(DZSlidesPostProcessor.class);
        }
    }

    /**
     * Create the Map in order to configure the converter.
     * @param optsBuilder
     * @param backend
     * @param outputFilename
     * @return
     */
    private Map<String, Object> parameters(OptionsBuilder optsBuilder, String backend,
                                           String outputFilename){
        OptionsBuilder opts = optsBuilder
                .backend(backend)
                .safe(SafeMode.UNSAFE).headerFooter(true);
                //.inPlace(true);
        if (outputFilename != null) {
            opts.toFile(new File(outputDir  + outputFilename));
        }else{
            opts.toFile(false);
        }

        return  opts.asMap();
    }

	public String convertToDocument(final String source){
		return convertToDocument(source, "html5", null, null, null);
	}


	public String convertToDocument(final String source, final String backend){
		return convertToDocument(source, backend, null, null, null);
	}
	
    // tag::render[]
	public String convertToDocument(final String source, String backend, final File templateDir, String part, String fileName) {
		String output = null;
		
		if (backend == null || "".equals(backend))
			backend = "html5";
		
		addPostProcessor(backend);
		
		OptionsBuilder optsBuilder = OptionsBuilder
				.options();
		if (templateDir != null && templateDir.exists()){
			optsBuilder = optsBuilder.templateDir(templateDir);
		}
		
		try {
			logger.info("[RENDER]::START rendering adoc");
			logger.info("output dir : "+ outputDir);
			Map<String, Object> parameters = parameters(optsBuilder, backend, fileName);
			
			if (part != null && !"all".equals(part)){
				StructuredDocument document = asciidoctor.readDocumentStructure(
						source, parameters);
				if (backend.equals("dzslides") && document != null  && document.getPartById(part) != null) {
					parameters.put(Asciidoctor.STRUCTURE_MAX_LEVEL, 2);
					ContentPart p = document.getPartById(part);
					output = DZSlidesUtils.getHeaderForSlides() + "<body><"+ p.getContext() +" class=\""+ p.getRole() + "\">"+ "<h2>" + p.getTitle() + "</h2>" + p.getContent() + "</"+ p.getContext() +">" + DZSlidesUtils.getBodyFooterForSlides() +"</body>";
				}
			}
			
			if (output == null)
				output = asciidoctor.convert(source, parameters);
			logger.info("[RENDER]::END rendering adoc");

		} catch(RuntimeException rex){
			logger.severe("[RENDER]::ERROR rendering adoc" + rex.toString());
			output = "Error during render process";
		}
		logger.info(output);
		return output;
	}
    // end::render[]

	
	public byte[] convertToBinaryDocument(InputStream source, String backend, final File templateDir, String part, String fileName) throws IOException {
		return convertToBinaryDocument(readFromStream(source), backend, templateDir, part, fileName);
    }
    public byte[] convertToBinaryDocument(String source, String backend, final File templateDir, String part, String fileName) throws IOException {
        String result = convertToDocument(source, backend, templateDir, part, fileName);
        Path p = null;
        if (fileName != null){
            p =  FileSystems.getDefault().getPath(outputDir + fileName);
        }
        return Files.readAllBytes(p);
    }

	public Asciidoctor getDelegate() {
		return asciidoctor;
	}
	

	public String readFromStream(final InputStream is) {
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
