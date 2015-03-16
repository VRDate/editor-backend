package org.asciidoctor.editor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.ast.StructuredDocument;
import org.asciidoctor.editor.core.AsciidoctorProcessor;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


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
    public void init() throws IOException{
    	logger.info("[BACKEND][START] Initializing Editor Backend...");
        logger.info("Asciidoctor version : " + asciidoctor.getDelegate().asciidoctorVersion());

        logger.info("[BACKEND][START] Backend started.");
    }
    
    @PreDestroy
    public void destroy(){
    	ref.unauth();
    }

}
