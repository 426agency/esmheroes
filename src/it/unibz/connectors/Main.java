package it.unibz.connectors;

import it.unibz.types.Developer;
import it.unibz.types.Issue;
import it.unibz.util.CSVExporter;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Main
{
	

	
	public static void main(String[] args) {
    
    int[] res;
 // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            }
        }
    };

    // Install the all-trusting trust manager
    try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
    }
		try {
			res = BugzillaComponent.query ("https://bugs.kde.org/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=&product=kuser&long_desc_type=substring&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&keywords_type=allwords&keywords=&bug_status=RESOLVED&bug_status=NEEDSINFO&bug_status=VERIFIED&bug_status=CLOSED&emailtype1=substring&email1=&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&chfieldfrom=&chfieldto=Now&chfieldvalue=&cmdtype=doit&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=");
			List<Developer> heros = SvnHandler.getHeros();
			
			Issue[] bugs = BugzillaComponent.getBugs(res,heros);
			CSVExporter.generateCsvFile("C:\\Users\\Fbihack\\Documents\\University\\esmheroes\\data.csv",bugs);
		} catch (Exception e) {
			e.printStackTrace();
		}
    
   
}

}
