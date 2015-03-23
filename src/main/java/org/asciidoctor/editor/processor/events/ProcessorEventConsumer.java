package org.asciidoctor.editor.processor.events;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.asciidoctor.editor.configuration.BackendConfiguration;
import org.asciidoctor.editor.processor.AsciidoctorProcessor;
import org.asciidoctor.editor.processor.Converter;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.File;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class ProcessorEventConsumer {

    @Inject
    private AsciidoctorProcessor asciidoctor;

    @Inject
    private Logger logger;

    @Inject
    @ConfigProperty(name = BackendConfiguration.ENV_ASCIIDOCTOR_BACKENDS)
    private String asciidoctorBackendsDir;

    @Inject
    @ConfigProperty(name = BackendConfiguration.KEY_BACKENDS_DZSLIDES)
    private String templateDZSlides;

    /**
     * for unique filename
     */
    private Random r;

    @PostConstruct
    public void init() {
        r = new Random(200L);
    }

    @Asynchronous
    @Lock(LockType.READ)
    public void consumeEvent(@Observes ProcessorEvent processorEvent) throws InterruptedException {
        final String filename = getRandomFilename(processorEvent.filename);
        logger.info("Receiving event : " + asciidoctorBackendsDir + templateDZSlides + filename + processorEvent.converter.getFilenameExtension());

        try {
            File templateDir = null;
            if (processorEvent.converter.name().equals(Converter.dzslides.name())) {
                templateDir = new File(asciidoctorBackendsDir + templateDZSlides);
            }

            asciidoctor.convertToBinaryDocument(processorEvent.asciidoc, processorEvent.converter, templateDir, "all",
                    filename);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Processing error for filename : " + filename, e);
        }
    }

    public String getRandomFilename(String filename) {
        return filename + "-" + r.nextLong();
    }
}