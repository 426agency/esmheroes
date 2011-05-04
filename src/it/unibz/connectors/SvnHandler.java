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

import it.unibz.types.Developer;
import it.unibz.types.FileItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;



public class SvnHandler {
	static Map<String,Developer> authors = new HashMap<String, Developer>();
	static Map<String,FileItem> fileitems = new HashMap<String,FileItem>();
	
    public static List<Developer> getHeros () {
        /*
         * default values:
         */
        String url = "svn://anonsvn.kde.org/home/kde/trunk/KDE/kdeadmin/kuser";
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

        
        
        long startRevision = 0;
        long endRevision = -1; //HEAD (the latest) revision     
//        try {
//			logEntries = repository.log( new String[] { "" } , null , startRevision , endRevision , true , true );
//		} catch (SVNException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		try {
			listEntries(repository, "");
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		for ( Iterator entries = logEntries.iterator( ); entries.hasNext( ); ) {
//			   SVNLogEntry logEntry = ( SVNLogEntry ) entries.next( );
//			   Set changedPathsSet = logEntry.getChangedPaths( ).keySet( );
//			   for ( Iterator changedPaths = changedPathsSet.iterator( ); changedPaths.hasNext( ); ) {
//			      SVNLogEntryPath entryPath = ( SVNLogEntryPath ) logEntry.getChangedPaths( ).get( changedPaths.next( ) );
//			      if(logEntry.getAuthor().length()==0)
//			    	  continue;
//			      
//			      //first check author
//			      Developer d =null;
//			      boolean isnew=true;
//			      if(!authors.containsKey(logEntry.getAuthor()))
//		      		{
//		      			d = new Developer(logEntry.getAuthor(), 1);
//		      			authors.put(d.getName(),d);
//		      		}
//			      else{
//			      d=authors.get(logEntry.getAuthor());
//			      isnew=false;}
//			      
//			      //case 1 file already exists			      
//			      if(fileitems.containsKey(entryPath.getPath())){
//			      	FileItem cu = fileitems.get(entryPath.getPath());
//			      	
//			      	//If author not same as fileauthor remove hero status
//			      	if(!logEntry.getAuthor().equalsIgnoreCase(cu.getAuthor().getName()))
//			      		{cu.incNumbermodified();
//			      		//increase number of authors modified files
//			      		if(!isnew){
//			      			d.incNrfile();
//			      		}
//			      			}
//			      }
//			      //case 2 we have a new file
//			      else{
//			      		FileItem cu = new FileItem(entryPath.getPath(), d);
//			      		if(!isnew){
//			      			d.incNrfile();
//			      		}
//			      		fileitems.put(entryPath.getPath( ),cu);
//			      }
//			      
//			      authors.put(logEntry.getAuthor(), d);
//    
//		}
//		}
		List<Developer> ret= new ArrayList<Developer>();
		//Now print only heroes
		Set<String> keys = authors.keySet();         // The set of keys in the map.
	      Iterator<String> keyIter = keys.iterator();
	      System.out.println("The map contains the following associations:");
	      while (keyIter.hasNext()) {
	         Object key = keyIter.next();  // Get the next key.
	         Developer value = authors.get(key);  // Get the value for that key.
	         System.out.println( "   (" + key + "," + value.getNrfile() + ")" );
	         for(int i=0;i<value.getNrfile();i++){
	        	 if(value.getFile(i).getNumbermodified()==1){
	        		 System.out.println("HERO!!");
	        		 ret.add(value);
	        		 break;
	        	 }
	         }
	      }
	      //Now filter out authors
	    
	      
	      return ret;
		}

    public static void listEntries( SVNRepository repository, String path ) throws SVNException {
        Collection fileentries = repository.getDir( path, -1 , null , (Collection) null );
        Iterator iterator = fileentries.iterator( );
        while ( iterator.hasNext( ) ) {
            SVNDirEntry entry = ( SVNDirEntry ) iterator.next( );
            long startRevision = 0;
            long endRevision = -1; //HEAD (the latest) revision   
            Collection logEntries = null;

            try {
    			logEntries = repository.log( new String[] { entry.getRelativePath() } , null , startRevision , endRevision , false , true );
    		} catch (SVNException e) {
    			return;
    		}
    		for ( Iterator entries = logEntries.iterator( ); entries.hasNext( ); ) {

    		SVNLogEntry logEntry = ( SVNLogEntry ) entries.next( );
//			   Set changedPathsSet = logEntry.getChangedPaths( ).keySet( );
//			   for ( Iterator changedPaths = changedPathsSet.iterator( ); changedPaths.hasNext( ); ) {
//			      SVNLogEntryPath entryPath = ( SVNLogEntryPath ) logEntry.getChangedPaths( ).get( changedPaths.next( ) );
			      if(logEntry.getAuthor().length()==0)
			    	  continue;
			      
			      //first check author
			      Developer d =null;
			      boolean isnew=true;
			      if(!authors.containsKey(logEntry.getAuthor()))
		      		{
		      			d = new Developer(logEntry.getAuthor());
		      			authors.put(d.getName(),d);
		      		}
			      else{
			      d=authors.get(logEntry.getAuthor());
			      isnew=false;}
			      
			      //case 1 file already exists			      
			      if(fileitems.containsKey(entry.getRelativePath())){
			      	FileItem cu = fileitems.get(entry.getRelativePath());
			      	
			      	//If author not same as fileauthor remove hero status
			      	if(!logEntry.getAuthor().equalsIgnoreCase(cu.getAuthor().getName()))
			      		{cu.incNumbermodified();
			      		//increase number of authors modified files
			      		d.addFile(cu);
			      			}
			      }
			      //case 2 we have a new file
			      else{
			      		FileItem cu = new FileItem(entry.getRelativePath(), d);
			      		d.addFile(cu);
			      		fileitems.put(entry.getRelativePath(),cu);
			      }
			      
			      authors.put(logEntry.getAuthor(), d);
 
		
    		}
            System.out.println( "/" + (path.equals( "" ) ? "" : path + "/" ) + entry.getName( ) + 
                               " ( author: '" + entry.getAuthor( ) + "'; revision: " + entry.getRevision( ) + 
                               "; date: " + entry.getDate( ) + ")" );
            if ( entry.getKind() == SVNNodeKind.DIR ) {
                listEntries( repository, ( path.equals( "" ) ) ? entry.getName( ) : path + "/" + entry.getName( ) );
            }
        }
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