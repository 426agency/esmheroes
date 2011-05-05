package it.unibz.connectors;


import it.unibz.types.Developer;
import it.unibz.types.Issue;

import java.io.*;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

/**
 * A connection to Bugzilla. Connects to the database and provides
 * descriptions of bugs. 
 */
public final class BugzillaComponent extends java.lang.Object {

    private static java.net.URL urlBase;
    /** sax parser to use */
    private static SAXParser saxParser;
        
    
    /** Getter of more issues at once.
     * @param numbers array of integers with numbers of bugs to retrieve
     * @return the issue array
     * @exception IOException if connection fails
     * @exception SAXException if parsing fails
     */
    public static Issue[] getBugs (int[] numbers,List<Developer> heros) throws SAXException, IOException {
        int maxIssuesAtOnce = 10;
        urlBase= new URL("https://bugs.kde.org");
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance(); 
            factory.setValidating (false);
            saxParser = factory.newSAXParser();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException ("Cannot initialize parser");
        }
        
        Issue[] result = new Issue[numbers.length];
        
        for (int issueToProcess = 0; issueToProcess < numbers.length; ) {
            int lastIssueRightNow = Math.min (numbers.length, issueToProcess + maxIssuesAtOnce);
        
            StringBuffer sb = new StringBuffer (numbers.length * 8);
            String sep = "xml.cgi?id=";
            for (int i = issueToProcess; i < lastIssueRightNow; i++) {
                sb.append (sep);
                sb.append (numbers[i]);
                sep = ",";
            }
            sb.append ("&show_attachments=false");
                URL u = null;
                try {
                    u = new URL("https://bugs.kde.org/"+sb.toString());
                    InputStream is = u.openStream();
                    
                    Issue[] arr;
                    try {
                        arr = getBugs(is, urlBase,heros);
                    } finally {
                        is.close();
                    }
                    
                    // copy the results and go on
                    for (int i = 0; i < arr.length; ) {
                        result[issueToProcess++] = arr[i++];
                    }
                    continue;
                }
                catch (IOException ex) {
                    
                }
            
        } // end of GLOBAL
        
        return result;
    }
    
    /** Executes a query and returns array of issue numbers that fullfils the query.
     * @param query the query string that should be appended to the URL after question mark part
     * @return array of integers
     */
    public static int[] query (String query) throws SAXException, IOException {
        URL u = new URL ("https://bugs.kde.org/buglist.cgi?" + query);
        BufferedReader reader = null;

            try {
                reader = new BufferedReader (
                    new InputStreamReader (u.openStream (), "UTF-8")
                );
            }
            catch (IOException ex) {
                
            }
        
        if (reader == null) {
            throw new IOException("Can't get connection to " + u.toString());
        }
        
        ArrayList result = new ArrayList ();
        
        String magic = "show_bug.cgi?id=";
        for (;;) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            
            int index = line.indexOf (magic);
            if (index == -1) {
                continue;
            }
            
            index += magic.length ();
            
            int end = line.indexOf ('"', index);
            if (end == -1) {
                throw new IOException ("No ending \" from index " + index + " in " + line);
            }
        
            String number = line.substring (index, end);
            
            result.add (Integer.valueOf (number));
        }
        
        int[] arr = new int[result.size ()];
        
        Iterator it = result.iterator ();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = ((Integer)it.next ()).intValue();
        }
        
        return arr;
    }
        
        
    
    /**
     * Gets the bugs from the InputStream.
     *
     * @return Issue[] objects from the InputStream containing
     * their XML representation.
     */
    private static Issue[] getBugs(InputStream in, URL source, List<Developer> heros)
    throws SAXException, IOException  {
        BugzillaXMLHandler handler = new BugzillaXMLHandler();
        InputSource input = new InputSource(in);
        input.setSystemId(source.toExternalForm());
        saxParser.parse(input, handler);
        return getBugsFromHandler(handler,heros);
    }
    
    /**
     * Gets the bugs form the handler. This must be called once the handler
     * finished its work.
     */
    private static Issue[] getBugsFromHandler(BugzillaXMLHandler handler, List<Developer> heros) {
        List bugList = handler.getBugList();
        if (bugList == null) {
            return null;
        }
        Issue[] bugs = new Issue[bugList.size()];
        for (int i = 0; i < bugList.size(); i++) {
            Issue bug = new Issue();
            Map atts = (Map) bugList.get(i);
            Iterator it = atts.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next(); 
                bug.setAttribute((String) entry.getKey(), entry.getValue());
            }
            Iterator<Developer> keyIter = heros.iterator();
    	      while (keyIter.hasNext()) {
    	         Developer key = keyIter.next();
    	         //SET IF bug fixed by hero
            	if(bug.getAttribute(Issue.ASSIGNED_TO).toString().contains(key.getName()))
            		bug.setFixedByHero();
            }
				
            bugs[i] = bug;
        }
        return bugs;
    }
    
}
