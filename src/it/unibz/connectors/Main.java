package it.unibz.connectors;

import it.unibz.types.Developer;
import it.unibz.types.Issue;
import it.unibz.util.CSVExporter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.SAXException;

public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//BugzillaComponent iz = new BugzillaComponent (new URL ("https://bugs.kde.org/"));
    
    int[] res;
		try {
			res = BugzillaComponent.query ("https://bugs.kde.org/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=&product=kuser&long_desc_type=substring&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&keywords_type=allwords&keywords=&bug_status=RESOLVED&bug_status=NEEDSINFO&bug_status=VERIFIED&bug_status=CLOSED&emailtype1=substring&email1=&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&chfieldfrom=&chfieldto=Now&chfieldvalue=&cmdtype=doit&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=");
			List<Developer> heros = SvnHandler.getHeros();
			
			Issue[] bugs = BugzillaComponent.getBugs(res,heros);
			CSVExporter.generateCsvFile("C:\\Users\\Fbihack\\Documents\\University\\data.csv",bugs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
   
//		String res= String.format("%d hours, %d min, %d sec", 
//				TimeUnit.MILLISECONDS.toHours(delay),
//				TimeUnit.MILLISECONDS.toMinutes(delay)-
//				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(delay)),
//				TimeUnit.MILLISECONDS.toSeconds(delay) - 
//				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(delay))
//		);

    //System.out.println("Total time heroes: "+BugzillaComponent.getTotalDelayHeroes()+" non Heroes: "+BugzillaComponent.getTotalDelayNonHeroes());
	}

}
