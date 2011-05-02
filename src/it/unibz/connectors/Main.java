package it.unibz.connectors;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
			res = BugzillaComponent.query ("https://bugs.kde.org/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=&product=kcron&long_desc_type=substring&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&keywords_type=allwords&keywords=&bug_status=RESOLVED&bug_status=NEEDSINFO&bug_status=VERIFIED&bug_status=CLOSED&emailtype1=substring&email1=&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&chfieldfrom=&chfieldto=Now&chfieldvalue=&cmdtype=doit&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=");
			Map authors = SvnHandler.getAuthors();
			Set<String> keys = authors.keySet(); 
			
			BugzillaComponent.getBugs(res,keys);
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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

    System.out.println("Total time heroes: "+BugzillaComponent.getTotalDelayHeroes()+" non Heroes: "+BugzillaComponent.getTotalDelayNonHeroes());
	}

}
