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
import org.asciidoctor.editor.datas.FirebaseConfig;

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

        initFirebase();

		logger.info("[BACKEND][START] Backend started.");
    }
    
    @PreDestroy
    public void destroy(){
    	ref.unauth();
    }
    
    public void initFirebase() {
		logger.info("[FIREBASE] Init listener");

    	final Map<String, Object> parameters = new HashMap<>();
		parameters.put(Asciidoctor.STRUCTURE_MAX_LEVEL, 2);
		


		ref.child("projects").child("project:748de5e0-2650-959e-886e-dc6daa5e2508")
		.child("files").child("c7340fef-6980-5962-5122-41330c3cbbb8").addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot arg0) {
				logger.info("data changed :" + arg0);
				Map<String, Object> props = new HashMap<String, Object>();
				props.put("ast", asciidoctor.getDelegate().load(arg0.child("asciidoc").getValue().toString(), parameters));
					//props.put("html5", asciidoctor.convertToDocument(arg0.child("asciidoc").getValue().toString()));
				arg0.getRef().child("converter").setValue(props);
			}

			@Override
			public void onCancelled(FirebaseError arg0) {
				logger.info("cancel changed :" + arg0);	

			}
		});
	}

}
