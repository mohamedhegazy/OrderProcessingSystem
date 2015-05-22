import java.sql.SQLException;

import net.sf.jasperreports.engine.JRException;
public class JDBC {
 
  public static void main(String[] argv) throws SQLException, ClassNotFoundException, JRException {
	  QueryHelper helper=new QueryHelper("root", "");
	  String []fields={"ISBN", "title","Publisher","Publication_Year" ,"Price","Category","Minimum_threshold","NO_OF_COPIES"};
	  String []values={"3",  "'lol'","1","curdate()" ,"50","1","5","20"};	  
	  System.out.println();	  
	  helper.viewSellingBooksReport();
	  
  }
}