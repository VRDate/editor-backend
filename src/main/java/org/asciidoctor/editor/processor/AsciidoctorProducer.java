package org.asciidoctor.editor.processor;

import org.asciidoctor.Asciidoctor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Created by mgreau on 10/02/15.
 */
@ApplicationScoped
public class AsciidoctorProducer {

    @Produces
    public Asciidoctor produceAsciidoctor(){
        return Asciidoctor.Factory.create();
    }
}
