package org.asciidoctor.editor.core.diff;

public interface DiffAdoc {
	
	public String rawDiff(String adocSource,
			String adocSourceToMerge) ;
	
	public String applyPatch(String adocSource, String patch);

}
