package it.unibz.util;

import it.unibz.types.Issue;

import java.io.FileWriter;
import java.io.IOException;

public class CSVExporter {
	 
	   public static void generateCsvFile(String sFileName, Issue[] bugs)
	   {
		try
		{
		    FileWriter writer = new FileWriter(sFileName);
		    writer.append("Bug");
		    writer.append(',');
		    writer.append("Developer");
		    writer.append(',');
		    writer.append("Fixtime");
		    writer.append(',');
		    writer.append("ByHero");
		    writer.append('\n');
		    for(int i=0;i<bugs.length;i++){
		    	writer.append(String.valueOf(bugs[i].getId()));
			    writer.append(',');
		    	writer.append(bugs[i].getAssignedTo());
			    writer.append(',');
			    writer.append(String.valueOf(bugs[i].getDuration()));
			    writer.append(',');
			    writer.append(bugs[i].isFixedByHero()?"1":"0");
		            writer.append('\n');
		    }
	 
		    //generate whatever data you want
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	    }
	}
