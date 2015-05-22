import java.sql.DriverManager;
import java.sql.SQLException;


//how to bind data from database to combobox in java
//http://community.jaspersoft.com/project/jasperreports-library

public class QueryHelper {
private java.sql.Connection conn;
QueryHelper(String userName, String password) throws SQLException, ClassNotFoundException{
    Class.forName("com.mysql.jdbc.Driver");
    conn = DriverManager
        .getConnection("jdbc:mysql://localhost/OrderProcessingSystem",userName,password);
}

public String addBook(String []Fields,String [] values) throws SQLException{

    String ans = "Successfull!";
    
    java.sql.Statement stmt=conn.createStatement();
    String val="";
    String field="";
    int i;
    for(i=0;i<values.length-1;i++){
    field=field+Fields[i]+",";
    val=val+values[i]+",";
    }
    field=field+Fields[i];
    val=val+values[i];

    stmt.addBatch("insert into Book("+field+") values("+val+");");
    
    try {
    stmt.executeBatch();
    }
    catch (SQLException e) {
    // TODO: handle exception
    ans=e.getMessage();    
    }
        
    return ans;

}    
public String modifyBook(String []Fields,String [] values){
    return null;
}    
public String confirmOrder(String []Fields,String [] values){
    return null;
}    
public String searchBook(String []Fields,String [] values){
    return null;
}    
public String signUP(String []Fields,String [] values){
    return null;
}    
public String modifyUser(String []Fields,String [] values){
    return null;
}    
public String checkoutCart(String []Fields,String [] values,String [][] items){
    return null;
}    





}