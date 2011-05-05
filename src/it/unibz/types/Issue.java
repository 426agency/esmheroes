package it.unibz.types;



import it.unibz.connectors.BugzillaXMLHandler;

import java.util.*;


/** Represents on issue in issuezilla.
 */
public final class Issue implements Comparable {
    static final String ISSUE_ID = "bug_id";
    static final String ISSUE_STATUS = "bug_status";
    static final String PRIORITY = "priority";
    public static final String ASSIGNED_TO = "assigned_to";
    public static final String CREATED = "creation_ts";
    static final String LAST_MODIFIED = "delta_ts";

    private HashMap attributes = new HashMap (49);
	private boolean fixedByHero=false;


    public boolean isFixedByHero() {
		return fixedByHero;
	}

	/**
     * Gets the id as an Integer.
     *
     * @return the issue_id as 
     */
    public int getId() {
        Object id = getAttribute(ISSUE_ID);
        try {
            return Integer.parseInt ((String) id);
        } catch (Exception ex) {
            return -1;
        }
    }

    /** Who is assigned to this bug.
     * @return name of person assigned to this bug
     */
    public String getAssignedTo () {
        return string (ASSIGNED_TO);
    }

    /** Status of the bug, verified, etc.
     * @return textual name of the status.
     */
    public String getStatus () {
        return string (ISSUE_STATUS);
    }

    /** Priority of the issue.
     * @return integer describing priority, -1 if unknown
     */
    public int getPriority () {
        String s = string (PRIORITY);
        if (s.length () == 2 && s.charAt (0) == 'P') {
            return s.charAt (1) - '0';
        } else {
            return -1;
        }
    }
    
    /** A time when this issue has been created.
     * @return the date or begining of epoch if wrongly defined
     */
    public Date getCreated () {
        Date d = (Date)getAttribute (CREATED);
        return d == null ? new Date (0) : d;
    }
    
    /** A time when this issue has been last modified.
     * @return the date or begining of epoch if wrongly defined
     */
    public Date getLastModified () {
        Date d = BugzillaXMLHandler.toDate((String)getAttribute (LAST_MODIFIED));
        return d == null ? new Date (0) : d;
    }

    
    /** Getter to return string for given attribute.
     */
    private String string (String name) {
        Object o = getAttribute (name);
        return o instanceof String ? (String)o : "";
    }
    
    /** Getter for array of integers.
     */
    private int[] ints (String name) {
        List l = (List)getAttribute (name);
        if (l == null) {
            return new int[0];
        }
        
        int[] arr = new int[l.size ()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Integer.parseInt ((String)l.get (i));
        }
        return arr;
    }

    /** Package private getter, it is expected to add getter for useful
     * issues.
     */
    public Object getAttribute(String name) {
            return attributes.get(name);
        
    }


    /** Setter of values, package private. */
    public void setAttribute(String name, Object value) {
    	attributes.put(name, value);
    	
    }

    /**
     * Gets the name/value pairs of the bug attributes as a Map.
     *
     * @return the name/value pairs of the attributes
     */
    private Map attributes() {
        return attributes;
    }

    /** Converts the object to textual representation.
     * @return a text description of the issue
     */
    public String toString() {   
        StringBuffer buffer;
        if (attributes == null) {
            return "Empty BugBase";
        }
        Iterator it = attributes.entrySet().iterator();
        buffer = new StringBuffer();
        buffer.append(this.getClass().getName() 
                      + " containing these name/value attribute pairs:\n");
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            buffer.append("NAME  : " + entry.getKey() + "\n");
            buffer.append("VALUE : " + entry.getValue() + "\n");      
        }
        return buffer.toString();
    }

    /** Compares issues by their ID
     */
    public int compareTo (Object o) {
        Issue i = (Issue)o;
        return getId () - i.getId ();
    }
    


    /** 
     * Long description of Issues.
     */
    public final static class Description {
        public static final String WHO = "who";
        static final String ISSUE_WHEN = "bug_when";
        static final String BODY = "thetext";
        public static final String THETEXT = "thetext";

        /** Holds value of property who. */
        private String who;

        /** Holds value of property issue_when. */
        private Date when;

        /** Holds value of property thetext. */
        private String body;

        /** Name of the author of the issue.
         * @return Value of property who.
         */
        public String getWho() {
            return who;
        }

        /** Setter for property who.
         * @param who New value of property who.
         */
        void setWho(String who) {
            this.who = who;
        }

        /** When this comment has been added.
         * @return Value of property issue_when.
         */
        public java.util.Date getWhen() {
            return when;
        }

        /** Setter for property issue_when.
         * @param issue_when New value of property issue_when.
         */
        public void setIssueWhen(Date when) {
            this.when = when;
        }

        /** The actual text of the issue.
         * @return Value of property thetext.
         */
        public String getBody() {
            return body;
        }

        /** Textual description.
         * @return string representation of the description.
         */
        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append(getWho());
            buffer.append(", ");
            buffer.append(getWhen());
            buffer.append(" : \n");
            buffer.append(getBody());
            buffer.append("\n\n");
            return buffer.toString();
        }

        /** Setter for property thetext.
         * @param thetext New value of property thetext.
         */
        void setBody(String body) {
            this.body = body;
        }

        public void setAtribute(String name, String value) {
            if (name.equalsIgnoreCase(WHO)) {
                setWho(value);
            } else if (name.equalsIgnoreCase(BODY) 
                    || name.equalsIgnoreCase(THETEXT)) {
                setBody(value);
            }
        }

        private String getAttribute(String name) {
            if (name.equalsIgnoreCase(WHO)) {
                return who;
            } else if (name.equalsIgnoreCase(BODY) 
                    || name.equalsIgnoreCase(THETEXT)) {
                return body;
            } else {
                return null;
            }
        }

    }



	public long getDuration() {    	
		return getLastModified().getTime()-getCreated().getTime();
	}

	public void setFixedByHero() {
this.fixedByHero=true;		
	}
    
}
