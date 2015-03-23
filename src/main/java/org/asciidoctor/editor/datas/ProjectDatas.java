package org.asciidoctor.editor.datas;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import org.asciidoctor.editor.processor.Converter;
import org.asciidoctor.editor.processor.events.ProcessorEvent;

import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class ProjectDatas {

	@Inject
	private Logger logger;

	@Inject
	private Firebase ref;

    @Inject
    Event<ProcessorEvent> processor;

    /**
     * Add a listener to a file
     *
     * @param projectID
     * @param fileID    ID of the file to li
     * @param converter
     */
    @Asynchronous
    @Lock(LockType.READ)
    public void listenToAsciiDocContent(final String projectID, final String fileID, final Converter converter) {

        ref.child("projects").child(projectID)
                .child("files").child(fileID).child("asciidoc").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot arg0) {
                logger.info("Datas changed (fire event)...");

                try {
                    final ProcessorEvent pEvent = new ProcessorEvent(arg0.getValue().toString(), converter,
                            fileID);
                    processor.fire(pEvent);

                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Processing error..", e);
                }
            }

            @Override
            public void onCancelled(FirebaseError arg0) {
                logger.info("Stop the listener :" + arg0);

            }
        });

	}
}
