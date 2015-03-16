package org.asciidoctor.editor.datas;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.firebase.client.AuthData;
import com.firebase.client.FirebaseError;
import org.asciidoctor.editor.StarterService;

import com.firebase.client.Firebase;

import java.util.logging.Logger;

@ApplicationScoped
public class FirebaseProducer {

    @Inject
    private Logger logger;

    @Produces
    public Firebase produceReference(){
        final Firebase ref = new Firebase(FirebaseConfig.FIREBASE_URL);

        ref.authWithCustomToken(FirebaseConfig.FIREBASE_SECRET, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticationError(FirebaseError error) {
                logger.info("[FIREBASE][ERROR] Login Failed! " + error.getMessage());
            }

            @Override
            public void onAuthenticated(AuthData authData) {
                logger.info("[FIREBASE] Login Succeeded!");
            }
        });
        return ref;
    }
}
