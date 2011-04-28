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

//http://www.bugzilla.org/docs/3.2/en/html/api/
public class Bugzilla {

	private static XmlRpcClientConfigImpl  config=null;
	private static XmlRpcClient client=null;
	private static Map map = null;
	
	private static void init(){
		if(config==null){
			config = new XmlRpcClientConfigImpl();
			try {
				config.setServerURL(new URL("https://bugs.kde.org/xmlrpc.cgi"));
				client = new XmlRpcClient();
				client.setConfig(config);
				map = new HashMap();
				map.put("login","info@dhdev.com");
				map.put("password","thehons88");
				client.execute("User.login",  new Object[]{map});
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static long getDelay(boolean exclusive, List<String> heroes){
		long ret=0;
		init();
	//bug_status=ASSIGNED&bug_status=REOPENED&bug_severity=critical&bug_severity=grave&bug_severity=major&bug_severity=crash&bug_severity=normal&bug_severity=minor&votes=21&order=bugs.votes
		map = new HashMap();
		map.put("ids", new String[]{"*"});

		Map result;
		try {
			result = (Map) client.execute(new String("Bug.get"),  new Object[]{map});
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
return 0;
}

		List l = Arrays.asList((Object[])result.get("bugs"));
		//Now we have our list of bugs, lets sum delaytimes = completation time
		int s =l.size();
		for(int i=0;i<s;i++){
			HashMap hm = (HashMap)l.get(i);
			if(exclusive&&heroes.contains((String)hm.get("assgined_to"))||
					(!exclusive&&!heroes.contains((String)hm.get("assgined_to"))))
				ret+=((Date)hm.get("last_change_time")).getTime()-((Date)hm.get("creation_time")).getTime();
		}
//		String res= String.format("%d hours, %d min, %d sec", 
//				TimeUnit.MILLISECONDS.toHours(delay),
//				TimeUnit.MILLISECONDS.toMinutes(delay)-
//				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(delay)),
//				TimeUnit.MILLISECONDS.toSeconds(delay) - 
//				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(delay))
//		);
		return ret;
	}
	
	public static void main(String[] args) throws MalformedURLException, XmlRpcException {
		init();

		

		System.out.println("Total delay time = "+getDelay(true,new ArrayList<String>()));

	}
}