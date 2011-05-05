package it.unibz.connectors;

import it.unibz.types.Developer;
import it.unibz.types.Issue;
import it.unibz.util.CSVExporter;
import java.util.List;

public class Main
{
	public static void main(String[] args) {
    
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
    
   
}

}
