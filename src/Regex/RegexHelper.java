package Regex;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class RegexHelper {

	private String url = null;
	private String content = null;
	
	public RegexHelper(String url){
		this.url = url;
		this.parse();
	}
	
	private void parse(){
		try {
			Document document = Jsoup.parse(new URL(url), 5000);
			this.content = document.toString();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * 
	 * @param url
	 * @return 
	 */
	public String getTitle(){
//		Pattern pattern = Pattern.compile("<title>(.*?)<\\/title>([\\s\\S]*?)<body(.*?)>([\\s\\S]*?)<\\/body>");
		Pattern pattern = Pattern.compile("<title>(.*?)<\\/title>");

		Matcher matcher = pattern.matcher(content);
		String title = null;
		while(matcher.find()){
//			System.out.println(matcher.group(1));
//			System.out.println(matcher.group(4));
			title = matcher.group(1);
//			System.out.println(title);
		}
		return title;
	}
	
	public ArrayList<String> getHref(){
		Pattern pattern = Pattern.compile("<a(.*?)href=\"(\\S*?)\"(.*?)>([\\s\\S]*?)<\\/a>");
		Matcher matcher = pattern.matcher(content);
		ArrayList<String> result = new ArrayList<String>();
		while(matcher.find()){
//			System.out.println(matcher.group(2));
//			System.out.println(matcher.group(4));
			String url = matcher.group(2);
			String title = matcher.group(4);
			result.add(title + "-" + url);
		}
		return result;
	}
	
	
	public static void main(String[] args){
		RegexHelper helper = new RegexHelper("http://www.baidu.com");
		helper.getTitle();
//		helper.getHref();
		
	}
}
