/*
 * ====================================================================
 * Copyright (c) 2004-2009 TMate Software Ltd.  All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.  The terms
 * are also available at http://svnkit.com/license.html
 * If newer versions of this license are posted there, you may use a
 * newer version instead, at your option.
 * ====================================================================
 */
package it.unibz.connectors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;



public class SvnHandler {
	
	
    public static Map getAuthors () {
        /*
         * default values:
         */
        String url = "svn://anonsvn.kde.org/home/kde/trunk/KDE/kdeadmin/kcron";
        String name = "anonymous";
        String password = "anonymous";

        setupLibrary();
        
        SVNRepository repository = null;
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException svne) {
            /*
             * Perhaps a malformed URL is the cause of this exception
             */
            System.err
                    .println("error while creating an SVNRepository for location '"
                            + url + "': " + svne.getMessage());
            System.exit(1);
        }

        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);

        Collection logEntries = null;
        long startRevision = 0;
        long endRevision = -1; //HEAD (the latest) revision     
        try {
			logEntries = repository.log( new String[] { "" } , null , startRevision , endRevision , true , true );
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map authors = new HashMap<String, Integer>();
		Map fileitems = new HashMap<String,FileItem>();
		
		for ( Iterator entries = logEntries.iterator( ); entries.hasNext( ); ) {
			   SVNLogEntry logEntry = ( SVNLogEntry ) entries.next( );
			   Set changedPathsSet = logEntry.getChangedPaths( ).keySet( );
			   for ( Iterator changedPaths = changedPathsSet.iterator( ); changedPaths.hasNext( ); ) {
			      SVNLogEntryPath entryPath = ( SVNLogEntryPath ) logEntry.getChangedPaths( ).get( changedPaths.next( ) );
			      if(fileitems.containsKey(entryPath.getPath())){
			      	FileItem cu = ((FileItem)fileitems.get(entryPath.getPath()));
			      	cu.incNumbermodified();
			      	//If author not same as fileauthor remove hero status
			      	if(!logEntry.getAuthor().equalsIgnoreCase(cu.getAuthor()))
			      		authors.put(cu.getAuthor(),1+(authors.containsKey(cu.getAuthor())?((Integer)authors.get(cu.getAuthor())):0));			      
			      }
			      else{
			      	if(logEntry.getAuthor().length()>0){
			      FileItem cu = new FileItem(entryPath.getPath(), logEntry.getAuthor());
			      fileitems.put(entryPath.getPath( ),cu);
			      authors.put(logEntry.getAuthor(),1);}
			      }
    
		}
		}
		
		//Now print only heroes
		Set keys = authors.keySet();         // The set of keys in the map.
	      Iterator keyIter = keys.iterator();
	      System.out.println("The map contains the following associations:");
	      while (keyIter.hasNext()) {
	         Object key = keyIter.next();  // Get the next key.
	         Object value = authors.get(key);  // Get the value for that key.
	         System.out.println( "   (" + key + "," + value + ")" );
	      }
	      
	      return authors;
		}

    /*
     * Initializes the library to work with a repository via 
     * different protocols.
     */
    private static void setupLibrary() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }

}