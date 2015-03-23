package org.asciidoctor.editor.processor;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.asciidoctor.ast.ContentPart;
import org.asciidoctor.ast.StructuredDocument;
import org.asciidoctor.editor.configuration.BackendConfiguration;
import org.asciidoctor.editor.core.IOUtils;
import org.asciidoctor.editor.processor.extension.iframe.IFrameAnchorPostProcessor;
import org.asciidoctor.editor.processor.extension.slides.DZSlidesPostProcessor;
import org.asciidoctor.editor.processor.extension.slides.DZSlidesUtils;
import org.asciidoctor.extension.JavaExtensionRegistry;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Logger;

@Stateless
public class AsciidoctorProcessor {

    @Inject
	private Logger logger;

    @Inject
	private Asciidoctor asciidoctor ;

    @Inject
    @ConfigProperty(name = BackendConfiguration.KEY_OUTPUT_DIR)
    private String outputDir;

	@PostConstruct
	public void init() {
		logger.info("Asciidoctor processor initiated.");
	}

    /**
     * Register a post processor depends on the backend.
     *
     * @param converter
     */
	private void addPostProcessor(Converter converter) {
		if (converter.equals(Converter.html5)) {
			JavaExtensionRegistry javaExtensionRegistry = this.asciidoctor
                    .javaExtensionRegistry();
            javaExtensionRegistry
                    .postprocessor(IFrameAnchorPostProcessor.class);
		} else if (converter.equals(Converter.dzslides)) {
			JavaExtensionRegistry javaExtensionRegistry = this.asciidoctor
                    .javaExtensionRegistry();
            javaExtensionRegistry
                    .postprocessor(DZSlidesPostProcessor.class);
        }
    }

    /**
     * Create the Map in order to configure the converter.
     * @param optsBuilder
	 * @param converter
	 * @param outputFilename
     * @return
     */
	private Map<String, Object> parameters(OptionsBuilder optsBuilder, Converter converter,
										   String outputFilename){
        OptionsBuilder opts = optsBuilder
				.backend(converter.toString())
				.safe(SafeMode.UNSAFE).headerFooter(true);
                //.inPlace(true);
        if (outputFilename != null) {
			opts.toFile(new File(outputDir + outputFilename + converter.getFilenameExtension()));
		}
		//For now the PDF converter can only write to a file
		else if (outputFilename == null && converter == Converter.pdf) {
			opts.toFile(new File(outputDir + "temp.pdf"));
		} else {
			opts.toFile(false);
        }

        return  opts.asMap();
    }

	public String convertToDocument(final String source){
		return convertToDocument(source, Converter.html5, null, null, null);
	}


	public String convertToDocument(final String source, final Converter converter) {
		if (converter == Converter.pdf)
			return convertToDocument(source, converter, null, "all", "test.pdf");
		return convertToDocument(source, converter, null, null, null);
	}

	public String convertToDocument(final String source, Converter converter, final File templateDir, String part, String fileName) {
		String output = null;

		if (converter == null)
			converter = Converter.html5;

		addPostProcessor(converter);
		
		OptionsBuilder optsBuilder = OptionsBuilder
				.options();
		if (templateDir != null && templateDir.exists()){
			optsBuilder = optsBuilder.templateDir(templateDir);
		}
		
		try {
			Map<String, Object> parameters = parameters(optsBuilder, converter, fileName);
			
			if (part != null && !"all".equals(part)){
				StructuredDocument document = asciidoctor.readDocumentStructure(
						source, parameters);
				if (converter.equals(Converter.dzslides) && document != null && document.getPartById(part) != null) {
					parameters.put(Asciidoctor.STRUCTURE_MAX_LEVEL, 2);
					ContentPart p = document.getPartById(part);
					output = DZSlidesUtils.getHeaderForSlides() + "<body><"+ p.getContext() +" class=\""+ p.getRole() + "\">"+ "<h2>" + p.getTitle() + "</h2>" + p.getContent() + "</"+ p.getContext() +">" + DZSlidesUtils.getBodyFooterForSlides() +"</body>";
				}
			}

			if (output == null) {
				logger.info("[RENDER]::START rendering adoc");
				output = asciidoctor.convert(source, parameters);
			}

			//if the content was written to the filesystem
			if (output == null) {
				Path p = FileSystems.getDefault().getPath(outputDir + fileName + converter.getFilenameExtension());
				output = p.toUri().toASCIIString();
			}
			logger.info("output dir : " + outputDir);
			logger.info("[RENDER]::END rendering adoc");

		} catch(RuntimeException rex){
			logger.severe("[RENDER]::ERROR rendering adoc" + rex.toString());
			output = "Error during render process";
		}
		return output;
	}


	public byte[] convertToBinaryDocument(InputStream source, Converter converter, final File templateDir, String part, String fileName) throws IOException {
		return convertToBinaryDocument(IOUtils.readFromStream(source), converter, templateDir, part, fileName);
	}

	public byte[] convertToBinaryDocument(String source, Converter converter, final File templateDir, String part, String fileName) throws IOException {
		convertToDocument(source, converter, templateDir, part, fileName);
		Path p = null;
        if (fileName != null){
			p = FileSystems.getDefault().getPath(outputDir + fileName + converter.getFilenameExtension());
		}
        return Files.readAllBytes(p);
    }

	public Asciidoctor getDelegate() {
		return asciidoctor;
	}
	

}
