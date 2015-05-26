import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
//how to bind data from database to combobox in java
//http://community.jaspersoft.com/project/jasperreports-library
import net.sf.jasperreports.view.JasperViewer;

public class QueryHelper {
	private java.sql.Connection conn;

	QueryHelper(String userName, String password) throws SQLException,
			ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/OrderProcessingSystem", userName,
				password);
	}
	public String addBookAuthor(String[] Fields, String[] values) throws SQLException {
		String ans = "Successfull!";
		java.sql.Statement stmt = conn.createStatement();
		String val = "";
		String field = "";
		int i;
		for (i = 0; i < values.length - 1; i++) {
			field = field + Fields[i] + ",";
			val = val + values[i] + ",";
		}
		field = field + Fields[i];
		val = val + values[i];
		stmt.addBatch("insert into Authors(" + field + ") values(" + val + ");");
		try {
			stmt.executeBatch();
		} catch (SQLException e) {
			// TODO: handle exception
			ans = e.getMessage();
		}
		return ans;
		
	}

	public String addAuthor(String[] Fields, String[] values) throws SQLException {
		String ans = "Successfull!";
		java.sql.Statement stmt = conn.createStatement();
		String val = "";
		String field = "";
		int i;
		for (i = 0; i < values.length - 1; i++) {
			field = field + Fields[i] + ",";
			val = val + values[i] + ",";
		}
		field = field + Fields[i];
		val = val + values[i];
		stmt.addBatch("insert into Authors_Names(" + field + ") values(" + val + ");");
		try {
			stmt.executeBatch();
		} catch (SQLException e) {
			// TODO: handle exception
			ans = e.getMessage();
		}
		return ans;
		
	}

	public String addPublisher(String[] Fields, String[] values) throws SQLException {
		String ans = "Successfull!";
		java.sql.Statement stmt = conn.createStatement();
		String val = "";
		String field = "";
		int i;
		for (i = 0; i < values.length - 1; i++) {
			field = field + Fields[i] + ",";
			val = val + values[i] + ",";
		}
		field = field + Fields[i];
		val = val + values[i];
		stmt.addBatch("insert into Publisher(" + field + ") values(" + val + ");");
		try {
			stmt.executeBatch();
		} catch (SQLException e) {
			// TODO: handle exception
			ans = e.getMessage();
		}
		return ans;
		
	}
	// manager adds book
	public String addBook(String[] Fields, String[] values) throws SQLException {
		String ans = "Successfull!";
		java.sql.Statement stmt = conn.createStatement();
		String val = "";
		String field = "";
		int i;
		for (i = 0; i < values.length - 1; i++) {
			field = field + Fields[i] + ",";
			val = val + values[i] + ",";
		}
		field = field + Fields[i];
		val = val + values[i];
		stmt.addBatch("insert into Book(" + field + ") values(" + val + ");");
		try {
			stmt.executeBatch();
		} catch (SQLException e) {
			// TODO: handle exception
			ans = e.getMessage();
		}
		return ans;
	}

	// manager modifies book
	public String modifyBook(String[] Fields, String[] values,
			String[] condition_field, String[] condition_value)
			throws SQLException {
		String ans = "Successful!";
		java.sql.Statement stmt = conn.createStatement();
		String update = "";
		int i;
		for (i = 0; i < values.length - 1; i++) {
			update = update + Fields[i] + "=" + values[i] + ",";
		}
		update = update + Fields[i] + "=" + values[i];
		String conditionString = "";
		conditionString = conditionString + condition_field[0] + "="
				+ condition_value[0];
		stmt.addBatch("update Book set " + update + " where " + conditionString
				+ ";");
		try {
			stmt.executeBatch();
		} catch (SQLException e) {
			// TODO: handle exception
			ans = e.getMessage();
		}
		return ans;
	}

	// manager says that order is received so he deletes it
	public String confirmOrder(String order_ID) throws SQLException {
		// third trigger changed to be before update since we dont delete the
		// order
		String ans = "Successful!";
		java.sql.Statement stmt = conn.createStatement();
		String update = "";
		update = "IS_DELETED=1";
		String conditionString = "";
		conditionString = conditionString + "Order_ID=" + order_ID;// order id
		stmt.addBatch("update Manager_Order set " + update + " where "
				+ conditionString + ";");
		try {
			stmt.executeBatch();
		} catch (SQLException e) {
			// TODO: handle exception
			ans = e.getMessage();
		}
		return ans;
	}

	public java.sql.ResultSet search(String []table,String[] Fields, String[] values)
			throws SQLException {
		java.sql.Statement stmt = conn.createStatement();
		String tableString="";
		int i;
		for ( i= 0; i < table.length-1; i++) {
			tableString=tableString+table[i]+" NATURAL JOIN ";
		}
		if(table.length>0){
			tableString=tableString+table[i];
		}
		String select = " where ";
		for (i = 0; i < values.length - 1; i++) {
			select = select + Fields[i] + "=" + values[i] + " AND ";
		}
		if(Fields.length>0)
		select = select + Fields[i] + "=" + values[i];
		java.sql.ResultSet rs;
		try {
			if(Fields.length>0) {
				String st = "select * from "+tableString + select + ";";
				//System.out.println(st);
				rs = stmt.executeQuery(st);
			}
			
			else{
				String tString="select * from "+tableString+";";
				//System.err.println(tString);
				rs = stmt.executeQuery("select * from "+tableString+";");	
			}
			
			
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			return null;
		}
		return rs;
	}

	// new customer signed up
	public String signUP(String[] Fields, String[] values) throws SQLException {
		String ans = "Successfull!";
		java.sql.Statement stmt = conn.createStatement();
		String val = "";
		String field = "";
		int i;
		for (i = 0; i < values.length - 1; i++) {
			field = field + Fields[i] + ",";
			val = val + values[i] + ",";
		}
		field = field + Fields[i];
		val = val + ""+values[i];
		if(!field.contains("PRIVILEGE")){
			field = field  + ",PRIVILEGE";
			val = val  + ",0";			
		}
		stmt.addBatch("insert into Sys_User(" + field + ") values(" + val
				+ ");");
		try {
			stmt.executeBatch();
		} catch (SQLException e) {
			// TODO: handle exception
			ans = e.getMessage();
		}
		return ans;
	}
	
	
	
	
	
	public String insertIntoTable(String tableName,String[] Fields, String[] values) throws SQLException {
		String ans = "Successfull!";
		java.sql.Statement stmt = conn.createStatement();
		String val = "";
		String field = "";
		
		int i;
		for (i = 0; i < values.length - 1; i++) {
			field += Fields[i]+" ,";
			val += ""+values[i]+",";
		}
		if(Fields.length>0) {
			field += Fields[i];
			val  = val + ""+values[i];
		}
					
		String st = "insert into "+ tableName +"("+ field +") values(" + val+");";
		stmt.addBatch(st);
		
		try {
			stmt.executeBatch();
		} catch (SQLException e) {
			// TODO: handle exception
			ans = e.getMessage();
		}
		return ans;
	}
	
	
	
	
	

	// user modifies his data
	public String modifyUser(String[] Fields, String[] values, String user_id)
			throws SQLException {
		String ans = "Successful!";
		java.sql.Statement stmt = conn.createStatement();
		String update = "";
		int i;
		for (i = 0; i < values.length - 1; i++) {
			update = update + Fields[i] + "=" + values[i] + ",";
		}
		update = update + Fields[i] + "=" + values[i];
		String conditionString = "";
		conditionString = conditionString + "User_ID=" + user_id;// condition on
															// user_id
		String st = "update Sys_User set " + update + " where "
				+ conditionString + ";";
		stmt.addBatch(st);
		try {
			stmt.executeBatch();
		} catch (SQLException e) {
			// TODO: handle exception
			ans = e.getMessage();
		}
		return ans;
	}

	public String checkoutCart(String user, String[][] items)
			throws SQLException {
		String ans = "Successful!";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String date=dateFormat.format(now);
		java.sql.Statement stmt_itemStatement = conn.createStatement();
		String queryString = "START TRANSACTION;\n";
		stmt_itemStatement.addBatch(queryString);		
		queryString = "";
		queryString=queryString+ "insert into Customer_Order (User_ID,Order_date) values ("
				+ user + ",'"+date+"');\n";
		stmt_itemStatement.addBatch(queryString);
		queryString = "";
		int i;
		for (i = 0; i < items.length; i++) {
			queryString = queryString					
					+ "insert into Customer_Order_Items values("
					+ "last_insert_id()," + items[i][0] + "," + items[i][1]
					+ ");\n";
			// last_insert_id() will return the latest generated auto increment key 
			// which is the ID of the entry inserted  in the customer_order table
			// book isbn in items[i][0] and number of
			// ordered copies is items[i][1]
			stmt_itemStatement.addBatch(queryString);		
			queryString = "";
			queryString = queryString
					+ "update Book set NO_OF_COPIES=NO_OF_COPIES-"
					+ items[i][1] + " where ISBN=" + items[i][0] + ";\n";
			stmt_itemStatement.addBatch(queryString);		
			queryString = "";
		}
		queryString = "";
		queryString = queryString + "COMMIT;\n";
		stmt_itemStatement.addBatch(queryString);
		try {
			stmt_itemStatement.executeBatch();
		} catch (SQLException e) {
			// TODO: handle exception
			ans = e.getMessage();
		}
		return ans;
	}
	
	
	

	public String promoteUser(String user_id) throws SQLException {
		String ans = "Successful!";
		java.sql.Statement stmt = conn.createStatement();
		stmt.addBatch("update Sys_User set Privilege=1 where User_ID=" + user_id
				+ ";");
		try {
			stmt.executeBatch();
		} catch (SQLException e) {
			// TODO: handle exception
			ans = e.getMessage();
		}
		return ans;
	}

	// manager places order
	public String placeOrder(String[] fields, String[] values)
			throws SQLException {
		String ans = "Successful!";
		java.sql.Statement stmt = conn.createStatement();
		String placeString = "";
		int i;
		for (i = 0; i < fields.length - 1; i++) {
			placeString = placeString + fields[i] + ",";
		}
		placeString = placeString + fields[i] + ",";
		placeString = placeString + "Order_date,IS_DELETED";
		String val = "";
		for (i = 0; i < values.length - 1; i++) {
			val = val + values[i] + ",";
		}
		val = val + values[i];
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String date=dateFormat.format(now);
		val = val + ",'"+date+"',0";
		stmt.addBatch("insert into Manager_Order(" + placeString + ") values("
				+ val + ");");
		try {
			stmt.executeBatch();
		} catch (SQLException e) {
			// TODO: handle exception
			ans = e.getMessage();
		}
		return ans;
	}

	// user sign so return his id if -1 return then its unsuccessful ,fields
	// will be user_name and password
	public int[] login(String[] fields, String[] values) throws SQLException {
		java.sql.Statement stmt = conn.createStatement();
		String select = "";
		int i;
		for (i = 0; i < values.length - 1; i++) {
			select = select + fields[i] + "=" + values[i] + " AND ";
		}
		select = select + fields[i] + "=" + values[i];
		java.sql.ResultSet rs;
		int ans[] = new int[2];
		try {
			rs = stmt.executeQuery("select * from Sys_User where " + select
					+ ";");
			if (rs.next()) {
				ans[0] = rs.getInt(1);
				ans[1] = rs.getInt(2);
				return ans;
			}
			else {
				return null;
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			return null;
		}
	}

	// follow these links for jasper
	// http://www.javarefer.com/how-to-create-jasper-report-in-java-using-eclipse/
	// in case if error of uuid:
	// http://stackoverflow.com/questions/11467874/cvc-complex-type-3-2-2-attribute-uuid-is-not-allowed-to-appear-in-element-jas
	public void viewLastMonthSalesReport() throws JRException {
		String report = "Prev_Month_Sales.jrxml";
		JasperReport jasperreport = JasperCompileManager.compileReport(report);
		JasperPrint jp = JasperFillManager.fillReport(jasperreport, null, conn);
		JasperViewer.viewReport(jp);
	}

	public void viewTopCustomersPurchaseReport() throws JRException {
		String report = "TopFiveCustomers.jrxml";
		JasperReport jasperreport = JasperCompileManager.compileReport(report);
		JasperPrint jp = JasperFillManager.fillReport(jasperreport, null, conn);
		JasperViewer.viewReport(jp);
	}

	public void viewSellingBooksReport() throws JRException {
		String report = "TopSellingBooks.jrxml";
		JasperReport jasperreport = JasperCompileManager.compileReport(report);
		JasperPrint jp = JasperFillManager.fillReport(jasperreport, null, conn);
		JasperViewer.viewReport(jp);
	}

}