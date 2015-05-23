import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import com.mysql.jdbc.ResultSet;

import net.sf.jasperreports.engine.JRException;
public class JDBC {
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static Random rnd = new Random();
	
	static String randomString( int len ) 
	{
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}
	static long randomNumber( long low,long high ) 
	{ 
		return (long)(low+ Math.random() *(high-low)); 
	}
	static String randomDate(){
		long rangebegin = Timestamp.valueOf("2015-01-01 00:00:00").getTime();
		long rangeend = Timestamp.valueOf("2015-05-23 00:58:00").getTime();
		long diff = rangeend - rangebegin + 1;
		Timestamp rand = new Timestamp(rangebegin + (long)(Math.random() * diff));
		return rand.toString();
	}
  public static void main(String[] argv) throws SQLException, ClassNotFoundException, JRException {
	  //----------------------------TEST--------------------------------------------//
	  //----------------------------TEST--------------------------------------------//
	  //----------------------------TEST--------------------------------------------//
	  //----------------------------TEST--------------------------------------------//
	  QueryHelper helper=new QueryHelper("root", "");
	  for (int i = 0; i < 100; i++) {
		helper.addPublisher(new String []{"Name","Address","Phone"}, new String []{"'"+randomString(10)+"'","'"+randomString(10)+"'","'"+randomString(10)+"'"});		
	}
	  ArrayList<String> iSBNStrings=new ArrayList<>();
	  for (long i = 0; i < 500; i++) {
		  String temp=randomString(13);
		  iSBNStrings.add(temp);
		System.out.println(helper.addBook(new String []{"ISBN","title","Publisher","Publication_Year","Price","Category","Minimum_Threshold","NO_OF_COPIES"}
					 , new String []{"'"+temp+"'","'"+randomString(10)+"'",""+randomNumber(1, 99)+"","'"+randomDate()+"'",""+randomNumber(10, 100)+"",""+randomNumber(1, 6)+"",""+randomNumber(5, 15)+"",""+randomNumber(20, 50)+""}));		
	}
////	  java.sql.ResultSet rSet=helper.searchBook(new String[]{}, new String[]{});
////	  while (rSet.next()){
////		  iSBNStrings.add(rSet.getString(1));
////		  System.out.println("hi");
////	  }
	  for (long i = 0; i < 500; i++) {
			System.out.println("author info "+helper.addAuthor(new String []{"First_Name","Last_Name"}
						 , new String []{"'"+randomString(10)+"'","'"+randomString(10)+"'"}));		
		}
	  for (long i = 0; i < 800; i++) {
			System.out.println("book author "+helper.addBookAuthor(new String []{"Author_ID","ISBN"}
						 , new String []{""+randomNumber(1, 499)+"","'"+iSBNStrings.get((int) randomNumber(0,499))+"'"}));		
		}
	  long id=2;
	  for (long i = 0; i < 100; i++,id++) {
		  long user=randomNumber(0, 2);
			System.out.println(helper.signUP(new String []{"PRIVILEGE","User_name","Password","First_name","Last_name","Email","Phone_number","Shipping_Address"}
						 , new String []{""+user+"","'"+randomString(20)+"'","'"+randomString(20)+"'","'"+randomString(20)+"'","'"+randomString(20)+"'","'"+randomString(20)+"'","'"+randomString(20)+"'","'"+randomString(20)+"'"}));
			if(user==1){//if manager make him place orders
				for (long j = 0; j < randomNumber(3, 10); j++) {
					System.out.println("mgr_order "+helper.placeOrder(new String []{"Mgr_ID","Book_ISBN","NO_OF_COPIES"}
					 , new String []{""+id+"","'"+iSBNStrings.get((int) randomNumber(0,499))+"'",""+randomNumber(5, 30)+""}));					
				}
			}else{//if customer make him order stuff
				for (long j = 0; j< randomNumber(3, 15); j++) {
					String items[][]=new String[(int) randomNumber(1, 5)][2];
					for (int k = 0; k < items.length; k++) {
						items[k][0]=iSBNStrings.get((int) randomNumber(0,499));
						items[k][1]=String.valueOf(randomNumber(1, 3));
					}
					System.out.println("checkout "+helper.checkoutCart(String.valueOf(id),items));
				}
			}
		}
	  helper.viewLastMonthSalesReport();
	  helper.viewSellingBooksReport();
	  helper.viewTopCustomersPurchaseReport();
	  
  }
}