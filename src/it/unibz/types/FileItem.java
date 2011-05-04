package it.unibz.types;

public class FileItem
{
private String filename=null;
private Developer author=null;
private int numbermodified=0;
public String getFilename() {
	return filename;
}
public FileItem(String filename, Developer author) {
	super();
	this.filename = filename;
	this.author = author;
	this.numbermodified = 1;
}
public void setFilename(String filename) {
	this.filename = filename;
}
public Developer getAuthor() {
	return author;
}
public void setAuthor(Developer author) {
	this.author = author;
}
public int getNumbermodified() {
	return numbermodified;
}
public void incNumbermodified() {
	this.numbermodified++;
}
}
