package it.unibz.connectors;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

 Map result = (Map) client.execute("User.login",  new Object[]{map});
 System.out.println("Result = "+result);
}
}