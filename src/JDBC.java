import java.sql.SQLException;
public class JDBC {
 
  public static void main(String[] argv) throws SQLException, ClassNotFoundException {
	  QueryHelper helper=new QueryHelper("root", "");
	  String []fields={"ISBN",  "title","Publisher","Publication_Year" ,"Price","Category","Minimum_threshold","NO_OF_COPIES"};
	  String []values={"2",  "'lol'","1","'1998-01-02'" ,"50","1","5","20"};	  
	  System.out.println(helper.addBook(fields, values));
	  
  }
}