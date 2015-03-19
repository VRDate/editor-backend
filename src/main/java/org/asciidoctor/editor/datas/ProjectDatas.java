package org.asciidoctor.editor.datas;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.editor.processor.AsciidoctorProcessor;
import org.asciidoctor.editor.processor.Converter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class ProjectDatas {

	@Inject
	private Logger logger;

	@Inject
	private Firebase ref;

    @Inject
    private AsciidoctorProcessor asciidoctor;


    /**
     * Add a listener to a file
     *
     * @param projectID
     * @param fileID    ID of the file to li
     * @param converter
     */
    public void listenToAsciiDocContent(final String projectID, final String fileID, final Converter converter) {

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put(Asciidoctor.STRUCTURE_MAX_LEVEL, 2);

        ref.child("projects").child(projectID)
                .child("files").child(fileID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot arg0) {
                logger.info("datas changed..." );

                try {
                    asciidoctor.convertToBinaryDocument(arg0.child("asciidoc").getValue().toString(), converter, null, "all",
                            arg0.child("name").getValue().toString());

                } catch (IOException e) {
                    logger.log(Level.SEVERE,"Processing error.." , e);
                }
            }

            @Override
            public void onCancelled(FirebaseError arg0) {
                logger.info("cancel changed :" + arg0);

            }
        });

	}



}
