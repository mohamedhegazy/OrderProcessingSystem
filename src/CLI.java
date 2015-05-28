import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import net.sf.jasperreports.engine.JRException;

public class CLI {
	static Scanner scanner = new Scanner(System.in);
	static QueryHelper helper;
	static int[] ans;

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		try {
			helper = new QueryHelper("root", "");
			A: while (true) {
				System.out.println("1-Log In");
				System.out.println("2-Sign Up");
				System.out.println("3-Quit");
				int choice = Integer.valueOf(scanner.nextLine());
				switch (choice) {
				case 1:
					System.out
							.println("Enter user name and password as [username,password]");
					String log = scanner.nextLine();
					ans = helper.login(
							new String[] { "User_name", "Password" },
							log.split(","));
					if (ans != null) {
						if (ans[1] == 1) {// manager
							manager();
						} else {// customer
							customer();
						}
					} else {
						System.out.println("Wrong user name or password!");
					}
					break;
				case 2:
					System.out
							.println("Enter user information as [User_name ,Password ,First_name ,Last_name ,Email ,Phone_number ,Shipping_Address]");
					String info = scanner.nextLine();
					System.out.println(helper.signUP(
							"User_name,Password,First_name,Last_name,Email,Phone_number,Shipping_Address"
									.split(","), info.split(",")));
					break;
				case 3:
					break A;
				default:
					System.out.println("Enter Valid Choice");
					break;
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

	}

	static void searchBook() throws SQLException {
		System.out
				.println("Enter book information as [ISBN,title,Publisher,Publication_Year,Price,Category,Minimum_threshold,NO_OF_COPIES]");
		System.out
				.println("*** IF A FIELD IS LEFT BLANK THEN IT WON'T BE CONSIDERED IN SEARCH ***");
		String info = scanner.nextLine();
		String valString[] = info.split(",");
		String fieldString[] = "ISBN,title,Pub_Name,Publication_Year,Price,Category_Name,Author_First_Name,Author_Last_Name,NO_OF_COPIES"
				.split(",");
		ArrayList<String> values = new ArrayList<>();
		ArrayList<String> field = new ArrayList<>();
		for (int i = 0; i < valString.length; i++) {
			if (valString[i].length() > 0) {
				field.add(fieldString[i]);
				values.add(valString[i]);
			}
		}
		print(helper.search(new String[] { "book", "Category", "Publisher",
				"Authors", "Authors_Names" }, (String[]) field.toArray(new String[field.size()]),
				(String[]) values.toArray(new String[values.size()])), new String[] { "ISBN", "title",
				"Pub_Name", "Publication_Year", "Price","Category_Name", "First_Name",
				"Last_Name","NO_OF_COPIES" }, "ISBN",
				new String[] { "First_Name", "Last_Name" }, 6);

	}

	static void userModify() throws SQLException {
		System.out
				.println("Enter new user information as [User_name ,Password ,First_name ,Last_name ,Email ,Phone_number ,Shipping_Address]");
		System.out
				.println("*** IF A FIELD IS LEFT BLANK THEN IT WON'T BE EDITED ***");
		String info = scanner.nextLine();
		String valString[] = info.split(",");
		String fieldString[] = "User_name,Password,First_name,Last_name,Email,Phone_number,Shipping_Address"
				.split(",");
		ArrayList<String> values = new ArrayList<>();
		ArrayList<String> field = new ArrayList<>();
		for (int i = 0; i < valString.length; i++) {
			if (valString[i].length() > 0) {
				field.add(fieldString[i]);
				values.add(valString[i]);
			}
		}
		System.out.println("WHAT");
		System.out.println(helper.modifyUser((String[]) field.toArray(new String[field.size()]),
				(String[]) values.toArray(new String[values.size()]), String.valueOf(ans[1])));
	}

	static void customer() throws SQLException {
		ArrayList<String> cart = new ArrayList<>();
		while (true) {
			System.out.println("1-Edit personal information");
			System.out.println("2-Search book");
			System.out.println("3-Add books to cart");
			System.out.println("4-View Cart");
			System.out.println("5-Remove Element from cart");
			System.out.println("6-Checkout cart");
			System.out.println("7-Logout");
			int choice = Integer.valueOf(scanner.nextLine());
			switch (choice) {
			case 1: {
				userModify();
				break;
			}
			case 2: {
				searchBook();
				break;
			}
			case 3: {
				System.out.println("Enter book order as [ISBN,NO_OF_COPIES]");
				String info = scanner.nextLine();
				String valString[] = info.split(",");
				java.sql.ResultSet rSet = helper.search(new String[] { "book",
						"Category", "Publisher", "Authors", "Authors_Names" },
						new String[] { "ISBN" }, new String[] { valString[0] });
				if (rSet.next()) {
					int copies = rSet.getInt("NO_OF_COPIES");
					if (copies < Integer.valueOf(valString[1])) {
						System.out
								.println("*** NUMBER OF AVAILABLE COPIES IS LESS THAN YOU WANT!");
					} else {
						String ISBNString = rSet.getString("ISBN");
						String titleString = rSet.getString("title");
						String price = rSet.getString("Price");
						String tupString = (cart.size() + 1) + "-'" + ISBNString
								+ "' " + titleString + " " + price
								+ " " + valString[1];
						cart.add(tupString);
					}
				} else {
					System.out.println("BOOK DOESN'T EXIST!");
				}

			}
				break;
			case 4: {
				System.out.println("   ISBN" + "      " + "TITLE" + "       "
						+ "PRICE" + "       " + "# COPIES");
				int price = 0;
				for (int i = 0; i < cart.size(); i++) {
					price = price + Integer.valueOf(cart.get(i).split(" ")[2])
							* Integer.valueOf(cart.get(i).split(" ")[3]);
					System.out.println(cart.get(i).replace(" ","            " ));
				}
				System.out.println("TOTAL PRICE--------------------->" + price);
			}

				break;
			case 5: {
				System.out.println("ISBN" + "      " + "TITLE" + "       "
						+ "PRICE");
				int price = 0;
				for (int i = 0; i < cart.size(); i++) {
					price = price + Integer.valueOf(cart.get(i).split(" ")[2])
							* Integer.valueOf(cart.get(i).split(" ")[3]);
					System.out.println(cart.get(i));
				}
				System.out.println("TOTAL PRICE--------------------->" + price);
				System.out.println("ENTER NUMBER OF ITEM TO DELETE");
				int item = Integer.valueOf(scanner.nextLine());
				if (item <= cart.size() && item > 0) {
					cart.remove(item - 1);
				} else {
					System.out.println("INVALID ITEM NUMBER!");
				}
			}

				break;
			case 6: {
				System.out
						.println("ENTER YOUR CREDIT CARD NUMBER[VISA OR MASTER CARD]");
				String credit = scanner.nextLine();
				if (credit.matches("^4[0-9]{12}(?:[0-9]{3})?")
						|| credit.matches("^5[1-5][0-9]{14}")) {//example of valid 5289898989898989
					String items[][] = new String[cart.size()][2];
					for (int i = 0; i < cart.size(); i++) {
						items[i][0] = cart.get(i).split(" ")[0].split("-")[1];
						items[i][1] = cart.get(i).split(" ")[3];
					}
					System.out.println(helper.checkoutCart(
							String.valueOf(ans[0]), items));
				} else {
					System.out.println("INVALID CREDIT CARD NUMBER");
				}
			}
				break;
			case 7:
				return;
			default:
				break;
			}
		}
	}

	private static void print(java.sql.ResultSet rs, String[] strings,
			String key, String multival[], int offset) throws SQLException {
		// TODO Auto-generated method stub
		String header = "";
		int space=20;
		String delimiter="--------------------";
		for (int i = 0; i < strings.length; i++) {
			header = header + strings[i];
			delimiter=delimiter+"--------------------";
			for (int j = 0; j < space-strings[i].length()-1; j++) {
				header = header + " ";	
			}
			header=header+"|";
		}
		System.out.println(delimiter);
		System.out.println(header);
		System.out.println(delimiter);
		String tempkeyString = "";
		while (rs.next()) {
			String tupleString = "";
			if (rs.getString(key).equals(tempkeyString)) {// means result of
															// join returned
				// repeated row but having different values for multivalued
				// attributes like author(s)
				for (int j = 0; j < offset; j++) {// offset for printing
													// multival
					for (int k = 0; k < space; k++) {
					tupleString = tupleString + " ";
					}
				}
				for (int i = 0; i < multival.length; i++) {
					String temp=rs.getString(multival[i]);
					tupleString = tupleString +temp;
					for (int j = 0; j < (temp==null?16:space-temp.length()); j++) {
					tupleString = tupleString +" ";
					}
				}
			} else {// new row
				for (int i = 0; i < strings.length; i++) {
					String temp=rs.getString(strings[i]);
					tupleString = tupleString +temp;
					for (int j = 0; j < (temp==null?16:space-temp.length()); j++) {
						tupleString = tupleString +" ";
						}
				}
			}
			System.out.println(tupleString);
			tempkeyString = rs.getString(key);
		}
	}

	static void manager() throws JRException, SQLException {
		while (true) {
			System.out.println("1-Edit personal information");
			System.out.println("2-Search book");
			System.out.println("3-Add new book");
			System.out.println("4-Modify Book");
			System.out.println("5-Place Order");
			System.out.println("6-Confirm Order");
			System.out.println("7-Promote User");
			System.out.println("8-See Last month books sales report");
			System.out.println("9-See Top 5 customers purchase report");
			System.out.println("10-See Top 10 selling  books report");
			System.out.println("11-Add publisher");
			System.out.println("12-Add Author");
			System.out.println("13-Add Book Author");
			System.out.println("14-Search Author");
			System.out.println("15-Search Publishers");
			System.out.println("16-Logout");
			int choice = Integer.valueOf(scanner.nextLine());
			switch (choice) {
			case 1:
				userModify();
				break;
			case 2:
				searchBook();
				break;
			case 3: {
				System.out
						.println("Enter Book Info as [ISBN,title,Publisher ID,Publication_Year,Price,Category ID,Minimum_threshold,NO_OF_COPIES]");
				String info = scanner.nextLine();
				System.out
						.println(helper
								.addBook(
										"ISBN,title,Pub_ID,Publication_Year,Price,Cat_ID,Minimum_threshold,NO_OF_COPIES"
												.split(","), info.split(",")));
				break;
			}
			case 4: {
				System.out.println("ENTER BOOK ISBN");
				String iSBNString=scanner.nextLine();
				System.out
						.println("Enter new book information as [ISBN,title,Publisher ID,Publication_Year,Price,Category ID,Minimum_threshold,NO_OF_COPIES]");
				System.out
						.println("*** IF A FIELD IS LEFT BLANK THEN IT WON'T BE EDITED ***");
				String info = scanner.nextLine();
				String valString[] = info.split(",");
				String fieldString[] = "ISBN,title,Publisher ID,Publication_Year,Price,Category ID,Minimum_threshold,NO_OF_COPIES".split(",");
				ArrayList<String>field=new ArrayList<>();
				ArrayList<String>values=new ArrayList<>();
				for (int i = 0; i < valString.length; i++) {
					if (valString[i].length() > 0) {
						values.add(valString[i]);						
						field.add(fieldString[i]);
					}
				}
				System.out.println(helper.modifyBook(
						(String[]) field.toArray(new String[field.size()]),
						(String[]) values.toArray(new String[values.size()]),new String[]{"ISBN"} ,new String[]{iSBNString}));
			}

				break;
			case 5:
			{
				System.out.println("ENTER BOOK ISBN AND NUMBER OF COPIES as [ISBN,NUMBER OF COPIES]");
				String orderString=scanner.nextLine();
				System.out.println(helper.placeOrder(new String []{"User_ID","ISBN","NO_OF_COPIES"}, (ans[0]+","+orderString).split(",")));
			}
				
				break;
			case 6:
			{
				print(helper.search(new String []{"Manager_Order"}, new String[]{"IS_DELETED"},new String []{"0"}),new String[]{"User_ID","Order_ID","Order_date","Receive_date","ISBN","NO_OF_COPIES","IS_DELETED"} , "Order_ID",new String[]{} , 0);
				System.out.println("Enter Order ID to confirm");
				int id = Integer.valueOf(scanner.nextLine());
				System.out.println(helper.confirmOrder(String.valueOf(id)));
			}
				break;
			case 7:
			{
				print(helper.search(new String[]{"Sys_User"}, new String[]{"Privilege"}, new String[]{"0"}), "User_ID,PRIVILEGE,User_name,Password,First_name,Last_name,Email,Phone_number,Shipping_Address".split(","), "User_ID", new String[]{}, 0);
				System.out.println("Enter User ID to promote");
				int id = Integer.valueOf(scanner.nextLine());
				System.out.println(helper.promoteUser(String.valueOf(id)));
			}
				break;
			case 8:
				helper.viewLastMonthSalesReport();
				break;
			case 9:
				helper.viewTopCustomersPurchaseReport();
				break;
			case 10:
				helper.viewSellingBooksReport();
				break;
			case 11:{
				System.out
				.println("Enter Publisher Info as [Publisher Name,Address,Phone]");
		String info = scanner.nextLine();
		System.out
				.println(helper
						.addPublisher(
								"Pub_Name,Address,Phone".split(","), info.split(",")));
				
			}
				
				break;
			case 12:{
				System.out
				.println("Enter Author Info as [First_Name,Last_Name]");
		String info = scanner.nextLine();
		System.out
				.println(helper
						.addAuthor(
								"First_Name,Last_Name".split(","),  info.split(",")));
				
			}
				break;
			case 13:{
				System.out
				.println("Enter Book Author Info as [Author_ID,ISBN]");
		String info = scanner.nextLine();
		System.out
				.println(helper
						.addBookAuthor(
								"Author_ID,ISBN".split(","), info.split(",")));
				
			}
				break;
			case 14:
				print(helper.search(new String[]{"Authors_Names"},new String []{} , new String[]{}), new String[]{"Author_ID","First_Name","Last_Name"}, "Author_ID", new String[]{}, 0);
				break;
			case 15:
				print(helper.search(new String[]{"Publisher"},new String []{} , new String[]{}), new String[]{"Pub_ID","Pub_Name","Phone","Address"}, "Pub_ID", new String[]{}, 0);
				break;
			case 16:
				return;
			default:
				break;
			}
		}
	}
	}
