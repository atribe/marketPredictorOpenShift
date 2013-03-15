import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class Test {
	/**
	 * @description Test class to use different tests to implement them finally in the website.
	 * @param args
	 */
	public static void main(String[] args){
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss"); 
			dateFormat.setTimeZone(TimeZone.getTimeZone("EST5EDT")); 
			Calendar calendar = Calendar.getInstance();
			System.out.println(dateFormat.format(new Date()));
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, 1);
			String time = "16:30:10";
			String [] date = dateFormat.format(calendar.getTime()).split(" ");
			String setDate = date[0]+" "+time;
			System.out.println(setDate);
			try {
				Date d = dateFormat.parse(setDate);
				/*Calendar calendar = new GregorianCalendar();
				String currentDate = dateFormat.format(new Date());
				String[] splitAll = currentDate.split(" ");// Split the whole Date and Time
				String[] splitDate = splitAll[0].split("-");// Split the Date part
				String[] splitTime = splitAll[1].split(":");// Split the Time part
				calendar.set(Calendar.DATE, value)*/
				System.out.println(dateFormat.format(d));
				System.out.println("Time is: "+d.getTime());
				System.out.println("Time is: "+new Date().getTime());
				long seconds = ((d.getTime()-new Date().getTime())/1000)*1000;
				System.out.println(seconds);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	private static String returnMonth(String month){
		return null;
	}
}
