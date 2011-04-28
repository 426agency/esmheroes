package it.unibz.connectors;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


public class Bugzilla {
	public static void main(String[] args) throws MalformedURLException, XmlRpcException {
		XmlRpcClientConfigImpl  config = new XmlRpcClientConfigImpl();

		config.setServerURL(new URL("https://bugs.kde.org/xmlrpc.cgi"));

		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);

		Map map = new HashMap();
		map.put("login","info@dhdev.com");
		map.put("password","thehons88");
		client.execute("User.login",  new Object[]{map});
		map = new HashMap();

		//bug_status=ASSIGNED&bug_status=REOPENED&bug_severity=critical&bug_severity=grave&bug_severity=major&bug_severity=crash&bug_severity=normal&bug_severity=minor&votes=21&order=bugs.votes
		map.put("status", "CLOSED");
		map.put("status", "VERIFIED");
		map.put("severity", "critical");
		map.put("resolution", "FIXED");
		map.put("op_sys", "LINUX");
		Map result = (Map) client.execute("Bug.search",  new Object[]{map});

		//creation_time
		// dateTime The time the attachment was created.
		//last_change_time
		//dateTime The last time the attachment was modified.
		List l = Arrays.asList((Object[])result.get("bugs"));
		long delay=0;
		//Now we have our list of bugs, lets sum delaytimes = completation time
		int s =l.size();
		for(int i=0;i<s;i++){
			HashMap hm = (HashMap)l.get(i);
			delay+=((Date)hm.get("last_change_time")).getTime()-((Date)hm.get("creation_time")).getTime();
		}
		String res= String.format("%d hours, %d min, %d sec", 
				TimeUnit.MILLISECONDS.toHours(delay),
				TimeUnit.MILLISECONDS.toMinutes(delay)-
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(delay)),
				TimeUnit.MILLISECONDS.toSeconds(delay) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(delay))
		);

		System.out.println("Total delay time = "+res);

	}
}