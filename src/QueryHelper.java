import java.sql.DriverManager;
import java.sql.SQLException;
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
		// third trigger changed to be before update since we don't delete the
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

	public java.sql.ResultSet searchBook(String[] Fields, String[] values)
			throws SQLException {
		java.sql.Statement stmt = conn.createStatement();
		String select = "";
		int i;
		for (i = 0; i < values.length - 1; i++) {
			select = select + Fields[i] + "=" + values[i] + " AND ";
		}
		select = select + Fields[i] + "=" + values[i];
		java.sql.ResultSet rs;
		try {
			rs = stmt.executeQuery("select * from Book where " + select + ";");
		} catch (SQLException e) {
			// TODO: handle exception
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
		field = field + Fields[i] + ",Privilege";
		val = val + values[i] + ",0";
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
		conditionString = conditionString + "ID=" + user_id;// condition on
															// user_id
		stmt.addBatch("update Sys_User set " + update + " where "
				+ conditionString + ";");
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
		String queryString = "START TRANSACTION;\n"
				+ "insert into Customer_Order(User_ID ,Order_date) values("
				+ user + ",curdate());\n";
		String tempItemsString = "";
		int i;
		for (i = 0; i < items.length; i++) {
			tempItemsString = tempItemsString
					+ "insert into Customer_Order_Items values("
					+ "LAST_INSERT_ID()," + items[i][0] + "," + items[i][1]
					+ ";\n";
			// last_insert_id() will return the latest generated auto increment key 
			// which is the ID of the entry inserted  in the customer_order table
			// book isbn in items[i][0] and number of
			// ordered copies is items[i][1]
			tempItemsString = tempItemsString
					+ "update Book set NO_OF_COPIES=NO_OF_COPIES-"
					+ items[i][1] + "where ISBN=" + items[i][0] + ";\n";
		}
		tempItemsString = tempItemsString + "COMMIT;\n";
		queryString = queryString + tempItemsString;
		java.sql.Statement stmt_itemStatement = conn.createStatement();
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
		stmt.addBatch("update Sys_User set Privilege=1 where ID=" + user_id
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
		val = val + "curdate(),0";
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

	// user sign so return his id if -1 return then it's unsuccessful ,fields
	// will be user_name and password
	public int signIn(String[] fields, String[] values) throws SQLException {
		java.sql.Statement stmt = conn.createStatement();
		String select = "";
		int i;
		for (i = 0; i < values.length - 1; i++) {
			select = select + fields[i] + "=" + values[i] + " AND ";
		}
		select = select + fields[i] + "=" + values[i];
		java.sql.ResultSet rs;
		int id = -1;
		try {
			rs = stmt.executeQuery("select * from Sys_User where " + select
					+ ";");
			if (rs.next()) {
				id = rs.getInt(1);
			}
			return id;
		} catch (SQLException e) {
			// TODO: handle exception
			return id;
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