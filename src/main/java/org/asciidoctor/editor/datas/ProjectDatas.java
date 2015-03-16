package org.asciidoctor.editor.datas;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.asciidoctor.editor.StarterService;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class ProjectDatas {

	@Inject
	private Logger logger;

	@Inject
	private Firebase ref;

	private String project;

	private String files;


	public void listenToProject(String project, String files){

	}



	
	

}
