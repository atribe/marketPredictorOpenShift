package ibd.web.IBD50;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ParseURL {
	public static void main(String[] args){
		new ParseURL().exchangeToTrade("AWAY");
		new ParseURL().indexMembership("AWAY");
		new ParseURL().marketCap("AWAY");
		new ParseURL().earningAnnouncement("MRK");
	}
	
	public String exchangeToTrade(String symbol){
		String exchangeToTrade = "";
		int counter = 1;
		while(counter<=5){
			try{
				Document doc = Jsoup.connect("http://finance.yahoo.com/q/pr?s="+symbol+"+Profile").get();
				Element content = doc.getElementById("yfi_rt_quote_summary");
				exchangeToTrade =  content.getElementsByClass("rtq_exch").first().text();
				if(exchangeToTrade.contains("-")){
					exchangeToTrade = exchangeToTrade.replaceAll("-", "");
				}
				//System.out.println(exchangeToTrade);
				break;
			}catch(Exception e){
				//e.printStackTrace();
				counter++;
			}
		}
		return exchangeToTrade;
	}
	
	public List<String> indexMembership(String symbol){
		List<String> urlText = null;
		int counter = 1;
		while(counter<=5){
			try{
				Document doc = Jsoup.connect("http://finance.yahoo.com/q/pr?s="+symbol+"+Profile").get();
				Element content = doc.getElementsByClass("yfnc_tabledata1").first();
				Elements links = content.getElementsByTag("a");
				List<String> urls = new ArrayList<String>();
				urlText = new ArrayList<String>();
				for (Element link : links) {					
				  String linkHref = link.attr("href");
				  String linkText = link.text();
				  //System.out.println(linkHref);
				  //System.out.println(linkText);
				  urls.add(linkHref);
				  urlText.add(linkText);
				}
				break;
			}catch(Exception e){
				//e.printStackTrace();
				counter++;
			}
		}
		return urlText;
	}
	
	public Long marketCap(String symbol){
		Long amountMarketCap = null;
		int counter = 1;
		while(counter<=5){
			try{
				Document doc = Jsoup.connect("http://finance.yahoo.com/q/ks?s="+symbol+"+Key+Statistics").get();
				String marketCap = doc.getElementsByClass("yfnc_tabledata1").first().text();
				//System.out.println(marketCap);
				Character [] worth = {'K','M','B'};
				Long [] amount = {1000L,1000000L,1000000000L};
				int index = 0;
				for(char ch : worth){
					if(marketCap.substring(marketCap.length()-1).equalsIgnoreCase(Character.toString(ch))){
						amountMarketCap = (long) (Double.parseDouble(marketCap.substring(0,marketCap.length()-1))*amount[index]);
						//System.out.println(amountMarketCap);
					}
					index++;
				}
				break;
			}catch(Exception e){
				//e.printStackTrace();
				counter++;
			}
		}
		return amountMarketCap;
	}
	
	public List<String> earningAnnouncement(String symbol){
		List<String> dates = null;
		int counter = 1;
		while(counter<=5){
			try{
				Document doc = Jsoup.connect("http://finance.yahoo.com/q/ce?s="+symbol+"+Company+Events").get();
				Element earningAnnouncements = doc.getElementsByClass("yfnc_datamodoutline1").first();
				Elements content = earningAnnouncements.getElementsByClass("yfnc_tabledata1");
				int index = 0;
				dates = new ArrayList<String>();
				for(Element ele:content){
					if(index%3==0){
						dates.add(ele.text());
						//System.out.println(ele.text());
					}
					index++;
				}
				break;
			}catch(Exception e){
				//e.printStackTrace();
				counter++;
			}
		}
		return dates;
	}
}
