package org.asciidoctor.editor.datas;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.SafeMode;
import org.asciidoctor.editor.StarterService;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import org.asciidoctor.editor.core.AsciidoctorProcessor;
import org.asciidoctor.editor.core.IOUtils;

import static org.asciidoctor.OptionsBuilder.options;

@Stateless
public class ProjectDatas {

	@Inject
	private Logger logger;

	@Inject
	private Firebase ref;

    @Inject
    private AsciidoctorProcessor asciidoctor;


	public void listenToAsciiDocContent(String projectID, String fileID){

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put(Asciidoctor.STRUCTURE_MAX_LEVEL, 2);

        ref.child("projects").child(projectID)
                .child("files").child(fileID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot arg0) {
                logger.info("data changed :" + arg0);

                try {
                    asciidoctor.convertToBinaryDocument(arg0.child("asciidoc").getValue().toString(), "pdf", null, "all",
                            arg0.child("name").getValue().toString());
                    arg0.getRef().child("pdf").setValue("ok");
                } catch (IOException e) {
                    arg0.getRef().child("pdf").setValue("ko");
                }
            }

            @Override
            public void onCancelled(FirebaseError arg0) {
                logger.info("cancel changed :" + arg0);

            }
        });

	}



}
