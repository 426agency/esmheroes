package it.unibz.connectors;


import org.xml.sax.*;

import it.unibz.types.Issue;

import java.util.*;

import java.text.SimpleDateFormat;

/**
 * The handler for parsing files containing XML representations of Issuezilla
 * bugs.
 */
public final class BugzillaXMLHandler extends HandlerBase {
    
    /** The DTD version this parser is capable to work with. */
    private static final String DTD_VERSION = "$Revision: 1.1 $";
    
    /** Name of the dtd_version attribute of the issuezilla tag */
    private static final String DTD_VERSION_NAME = "dtd_version";
    
    private static final String BUG_STATUS = "bug_status";
    
    private static final String RESOLUTION = "resolution";
    
    private static final String PRIORITY = "priority";
    
    private static final String BUG = "bug";
    
    private static final String BUGZILLA = "bugzilla";
   
    
    /** 
     * Contains names of all the XML tags which can be inside the
     * <Issuezilla.Issue> tag. Should be probably fetched from the DTD.
     * A constant for now. 
     */
    private static final HashSet tagsInIssue;
        
    /** 
     * List of bugs created from the XML file. Items of the list are Maps
     * containing the name/value pairs for the bug attributes.
     */
    private List bugs;
    
    /** The name/value pairs for the currently parsed bug */
    private Map bug;
    
    /** Structure of currently opened tags */
    private Vector openedTags;
    
    /** A StringBuffer containing the values inside the tags */
    private StringBuffer buffer;
        
    /** date format converter */
    private static SimpleDateFormat dateFormat;
    /** date format converter for second format of time */
    private static SimpleDateFormat dateFormat2;
    
    
    static {
        tagsInIssue = new HashSet();
        tagsInIssue.add("bug_id");
        tagsInIssue.add("bug_status");
        tagsInIssue.add("priority");
        tagsInIssue.add(Issue.ASSIGNED_TO);
        tagsInIssue.add("delta_ts");
        tagsInIssue.add("creation_ts");
       
    }
    
    /** Creates new IssuezillaXMLHandler */
    public BugzillaXMLHandler() {
    }

    
    public void setDocumentLocator (org.xml.sax.Locator locator) {
    }

    
    public void startDocument ()
    throws org.xml.sax.SAXException {
        bugs = new ArrayList();
        openedTags = new Vector();
    }

    
    public void endDocument ()
    throws org.xml.sax.SAXException {
    }
    
    public void startElement (String name, org.xml.sax.AttributeList atts)
    throws org.xml.sax.SAXException {
        openedTags.addElement(name);
        if (name.equals(BUGZILLA)) {
            checkDTDVersion(atts);
        } else if (name.equals(BUG)) {
            bug = new HashMap();
            bugs.add(bug);
            bug.put(Issue.CREATED, new java.util.Date (0));
            dealIssueAttributes(atts);
        } else if (tagsInIssue.contains(name)) {
            buffer = new StringBuffer();
        }
    }

    
    public void endElement (String name)
    throws org.xml.sax.SAXException {
        if (!currentTag().equals(name)) {
            throw new SAXException(
                "An error while parsing the XML file near the closing " + name
                + " tag");
        }
        openedTags.remove(openedTags.size() - 1);
        if (name.equals(BUGZILLA)) {
            
        } else if (tagsInIssue.contains(name)) {
            Object prev = bug.get (name);
            if (prev instanceof List) {
                // expecting multivalue
                ((List)prev).add (buffer.toString ());
            } else if (prev instanceof Date) {
                // convert to date
                bug.put (name, toDate (buffer.toString()));
            } else {
                bug.put(name, buffer.toString());
            }
            buffer = null;
        }
        
    }

    
    public void characters (char ch[], int start, int length)
    throws org.xml.sax.SAXException {
        String s = new String(ch, start, length);
        if (!s.equals("")) {
            if (buffer != null) {
                buffer.append(s);
            }
        }
        
    }

    
    public void ignorableWhitespace (char ch[], int start, int length)
    throws org.xml.sax.SAXException {
    }

    
    public void processingInstruction (String target, String data)
    throws org.xml.sax.SAXException {
    }
    
    /** Gets the current tag */
    private String currentTag() {
        if (openedTags.size() == 0) {
            return null;
        }
        return (String) openedTags.lastElement();
    }
    
    /**
     * Gets the List of the bugs which is created during the parsing process.
     * Must be called after the parsing (othervise returns null)
     *
     * @return a List containing the Maps of name/value pairs for the bugs
     */
    public List getBugList() {
        return bugs;
    }
    
    /**
     * Sets the priority and resolution attributes and checks the DTD version */
    private void dealIssueAttributes(AttributeList atts)
    throws SAXException {
        String priority = atts.getValue(PRIORITY);
        String issue_status = atts.getValue(BUG_STATUS);
        String resolution = atts.getValue(RESOLUTION);
        if (priority == null) {
            priority = "P0";
        }
        bug.put(PRIORITY, priority);
        bug.put(BUG_STATUS, issue_status);
        if (resolution != null) {
            bug.put(RESOLUTION, resolution);
        }
    }
    
    /** 
     * Checks whether the right DTD version is used. If not a SAXException
     * to that effect is thrown.
     */
    private void checkDTDVersion(AttributeList atts) throws SAXException {
        String dtdVersion = atts.getValue(DTD_VERSION_NAME);
        if ( (dtdVersion == null) || (!dtdVersion.equals(DTD_VERSION))) {
            
        }
    }
    
    /** Converts a string to date
     */
    public static java.util.Date toDate (String date) {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        }
        if (dateFormat2 == null) {
            dateFormat2 = new SimpleDateFormat ("yyyy-MM-dd hh:mm");
        }
        try {
            return dateFormat.parse (date);
        } catch (java.text.ParseException ex1) {
            try {
                return dateFormat2.parse (date);
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
}
