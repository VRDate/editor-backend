package org.asciidoctor.editor.processor.events;

import org.asciidoctor.editor.processor.AsciidoctorProcessor;

import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class ProcessorEventConsumer {

    @Inject
    private AsciidoctorProcessor asciidoctor;

    @Inject
    private Logger logger;

    @Asynchronous
    @Lock(LockType.READ)
    public void consumeEvent(@Observes ProcessorEvent processorEvent) throws InterruptedException {

        logger.info("Receiving event : " + processorEvent.filename);
        try {
            asciidoctor.convertToBinaryDocument(processorEvent.asciidoc, processorEvent.converter, null, "all",
                    processorEvent.filename);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Processing error..", e);
        }


    }
}