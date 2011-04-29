package it.unibz.connectors;

public class FileItem
{
private String filename=null;
private String author=null;
private int numbermodified=0;
public String getFilename() {
	return filename;
}
public FileItem(String filename, String author) {
	super();
	this.filename = filename;
	this.author = author;
	this.numbermodified = 1;
}
public void setFilename(String filename) {
	this.filename = filename;
}
public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public int getNumbermodified() {
	return numbermodified;
}
public void incNumbermodified() {
	this.numbermodified++;
}
}
