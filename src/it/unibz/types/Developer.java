package it.unibz.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents a Developer with its affected files 
 *
 */
public class Developer {

	private String name=null;
	private List<FileItem> files = null;
	
	public List<FileItem> getFiles(){
		return files;
	}
	
	public int getNrfile() {
		return files.size();
	}

	public String getName() {
		return name;
	}
	
	public void addFile(FileItem i){
		if(files==null)
			files= new ArrayList<FileItem>();
		files.add(i);
	}

	public Developer(String name) {
		super();
		this.name = name;
	}
	
	public FileItem getFile(int i){
		return files.get(i);
	}
	
	
}
