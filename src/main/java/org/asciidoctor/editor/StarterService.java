package org.asciidoctor.editor;

import com.firebase.client.Firebase;
import org.asciidoctor.editor.processor.AsciidoctorProcessor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Logger;


@Startup
@Singleton
public class StarterService {
    
	@Inject
    private Logger logger;

    @Inject
    private AsciidoctorProcessor asciidoctor;

    @Inject
	private Firebase ref;
    
    @PostConstruct
    public void init() {
        logger.info("[BACKEND][START] Initializing Editor Backend...");
        logger.info("Asciidoctor version : " + asciidoctor.getDelegate().asciidoctorVersion());
        logger.info("[BACKEND][START] Backend started.");
    }
    
    @PreDestroy
    public void destroy(){
    	ref.unauth();
    }

}
