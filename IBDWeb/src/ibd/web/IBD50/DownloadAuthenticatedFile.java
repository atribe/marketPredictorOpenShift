package ibd.web.IBD50;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;


public class DownloadAuthenticatedFile {

	public static void main(String args){

		try{
			// Creating a normal HttpClient object.
			DefaultHttpClient httpclient = new DefaultHttpClient();
			// As it's just a get call so no need for overhead of calling post.
			HttpGet httpget = new HttpGet("http://research.investors.com");
			// A response object
			HttpResponse response = httpclient.execute(httpget);
			// Entity in Client 4 and above holds all the response information.
			HttpEntity entity = response.getEntity();

			System.out.println("Login form get: " + response.getStatusLine());
			if (entity != null) {
				entity.consumeContent();            
			}
			System.out.println("Initial set of cookies:");
			// Getting all the cookies information available for www.investors.com
			List<Cookie> cookies = httpclient.getCookieStore().getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
			}

			// Calling the service to get the Login information.
			HttpPost httpost = new HttpPost("http://www.investors.com/Services/SiteAjaxService.asmx/MemberSingIn");

			// Creating Name Value pairs to pass information.
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("strEmail", "teedit@gmail.com"));
			nvps.add(new BasicNameValuePair("strPassword", "aaronnhugh"));
			nvps.add(new BasicNameValuePair("blnRemember", "true"));

			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

			response = httpclient.execute(httpost);
			entity = response.getEntity();

			System.out.println("Login form get: " + response.getStatusLine());
			if (entity != null) {
				entity.consumeContent();
			}

			System.out.println("Post logon cookies:");
			cookies = httpclient.getCookieStore().getCookies();
			String mySessionId = null;
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
			}

			// Setting the protocol version to handle the dynamic File Name Stuff
			httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			httpget = new HttpGet("http://research.investors.com/etables/IBD50XLS.aspx?tabView=IBD50&from=etables&columnsort1=ibd50rank&columnsorttype1=DESC&columnsort2=&columnsorttype2=DESC/eTable.txt");
			response = httpclient.execute(httpget);
			entity = response.getEntity();
			Header[] headers = response.getAllHeaders();
			for(int i=0;i<headers.length;i++){
				System.out.println("- " + "Header: "+headers[i].toString());
			}
			System.out.println(response.toString());

			System.out.println("File get: " + response.getStatusLine());

			InputStream in = entity.getContent();

			File path = new File(args);
			path.mkdirs();
			String fileName = new Date().toString();
			String[] arr = fileName.split(" ");
			File file = new File(path, "Data50"+arr[1]+arr[2]+arr[5]+".xls");
			ibd.web.Constants.Constants.fileName = "Data50"+arr[1]+arr[2]+arr[5]+".xls";
			ibd.web.Constants.Constants.logger.info("Inside DownloadAuthenticatedFile: Downloading: "+ibd.web.Constants.Constants.fileName);
			FileOutputStream fos = new FileOutputStream(file);

			byte[] buffer = new byte[1024];
			int len1 = 0;
			while ((len1 = in.read(buffer)) != -1) {
				fos.write(buffer, 0, len1);
			}

			fos.close();
			in.close();

			httpclient.getConnectionManager().shutdown();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}