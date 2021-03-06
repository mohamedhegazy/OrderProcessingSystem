create database OrderProcessingSystem;
use  OrderProcessingSystem;


CREATE TABLE Book
(ISBN VARCHAR(13) NOT NULL ,  title  VARCHAR(50),Pub_ID INT NOT NULL,Publication_Year DATE,Price INT NOT
     NULL,Cat_ID INT NOT NULL,Minimum_threshold INT NOT NULL,NO_OF_COPIES INT NOT NULL,  PRIMARY KEY (ISBN));

create table Category (Cat_ID INT NOT NULL AUTO_INCREMENT ,Category_Name varchar(50),Primary Key(Cat_ID));
Create table Authors (Author_ID   INT NOT NULL AUTO_INCREMENT ,ISBN VARCHAR(13),PRIMARY KEY(Author_ID ,ISBN));
Create table Authors_Names (Author_ID INT NOT NULL AUTO_INCREMENT,First_Name  varchar(50) NOT NULL,Last_Name  varchar(50) NOT NULL, PRIMARY KEY (Author_ID));

Create table Publisher(Pub_ID INT NOT NULL AUTO_INCREMENT, Pub_Name VARCHAR(50) NOT NULL,Address VARCHAR(50) NOT NULL,
 Phone VARCHAR(20),PRIMARY KEY(Pub_ID));



alter table Authors add FOREIGN KEY (Author_ID) REFERENCES Authors_Names(Author_ID);
alter table Authors add FOREIGN KEY (ISBN) REFERENCES Book(ISBN);
alter table Book add Foreign KEY (Pub_ID) References Publisher(Pub_ID);
alter table Book add Foreign Key (Cat_ID) References Category(Cat_ID);




insert into Category values (1,'Science');
insert into Category values (2,'Art');
insert into Category values (3,'Religion');
insert into Category values (4,'History');
insert into Category values (5,'Geography');


Create table Sys_User(User_ID  INT NOT NULL AUTO_INCREMENT ,PRIVILEGE INT NOT NULL,
User_name Varchar(50),Password Varchar(50),First_name varchar(50),Last_name varchar(50)
,Email varchar(100),Phone_number varchar(20),Shipping_Address varchar(200) , PRIMARY KEY (User_ID ) , KEY(User_name)
);
insert into Sys_User (User_ID ,PRIVILEGE,USER_NAME) values (1,1,'Order Man');

Create table Manager_Order (User_ID  INT ,Order_ID  INT NULL Auto_INCREMENT,Order_date DATE,Receive_date DATE,ISBN VARCHAR(13),NO_OF_COPIES INT,IS_DELETED INT,PRIMARY KEY(Order_ID));

alter table Manager_Order add Foreign Key (ISBN) References Book(ISBN);
alter table Manager_Order add Foreign Key (User_ID ) References Sys_User(User_ID );

Create table Customer_Order(Order_ID  INT NOT NULL AUTO_INCREMENT ,User_ID  INT NOT NULL,Order_date DATE,PRIMARY KEY(Order_Id));
Create table Customer_Order_Items(Order_ID INT NOT NULL,ISBN varchar(13),NO_OF_COPIES INT,
    PRIMARY KEY (Order_ID,ISBN));
    
alter table Customer_Order add Foreign Key (User_ID ) References Sys_User(User_ID );
alter table Customer_Order_Items add Foreign Key (Order_ID) References Customer_Order(Order_ID);
alter table Customer_Order_Items add Foreign Key (ISBN) References Book(ISBN);

create index book_idx on Book(ISBN) using HASH;
create index title_idx on Book(title) using HASH;
create index category_idx on Book(Cat_ID) using HASH;
create index author_idx on Authors_Names(First_Name) using HASH;
create index author_last_idx on Authors_Names(Last_Name) using HASH;
create index publisher_idx on Book(Pub_ID) using HASH;

create index purchase_idx on Customer_Order(Order_Date) using BTREE;
create index customer_idx on Customer_Order(User_ID) using HASH;







DELIMITER $$
CREATE TRIGGER Book_Quantity
BEFORE UPDATE ON Book
FOR EACH ROW
BEGIN
IF NEW.NO_OF_COPIES < 0 THEN
        SET NEW.NO_OF_COPIES = 0;
END IF;
END;



DELIMITER $$
CREATE TRIGGER Order_Placed
AFTER UPDATE ON Book
FOR EACH ROW
BEGIN
IF NEW.NO_OF_COPIES < NEW.Minimum_threshold  then
   SET @num=NEW.Minimum_threshold-NEW.NO_OF_COPIES;
   INSERT INTO Manager_Order (User_Id,Order_date,ISBN,NO_OF_COPIES,IS_DELETED)
   values(1,curdate(),NEW.ISBN,@num,0);
END IF;
END;



DELIMITER $$
CREATE TRIGGER Order_Received
BEFORE UPDATE ON Manager_Order
FOR EACH ROW
BEGIN
   IF NEW.IS_DELETED = 1 and OLD.IS_DELETED=0
   THEN
   SET @id=OLD.ISBN;
   SET @num=OLD.NO_OF_COPIES;
   SET NEW.Receive_date=curdate();
   UPDATE BOOK SET NO_OF_COPIES=@num+NO_OF_COPIES  where ISBN=@id;
   
END IF;
END;

--1st report query (total sales for books for the last month)
select b.ISBN,title,sum(i.NO_OF_COPIES) as Total_Copies_Sold,sum(b.Price*i.NO_OF_COPIES) as Gain 
from Customer_Order_Items i join Customer_Order o on i.Order_ID=o.Order_ID join BOOK b on i.ISBN=b.ISBN 
where Month(Order_date) >= Month(DATE_SUB(curdate(),INTERVAL 1 MONTH)) AND YEAR(Order_date) = YEAR(DATE_SUB(Order_date,INTERVAL 1 MONTH))
group by ISBN;

--2nd report query (The top 5 customers who purchase the most purchase amount in descending order for the last three months)
select User_name,sum(i.NO_OF_COPIES) as Number_of_Purchases
from Customer_Order o join Sys_User c on o.User_ID=c.User_IDID join Customer_Order_Items i on o.Order_ID=i.Order_ID
where Month(Order_date) >= Month(DATE_SUB(curdate(),INTERVAL 3 MONTH)) AND YEAR(Order_date) = YEAR(DATE_SUB(Order_date,INTERVAL 3 MONTH))
group by c.ID
order by Number_of_Purchases DESC
LIMIT 5
;
--3rd report query (The top 10 selling books for the last three months)
select title,sum(i.NO_OF_COPIES) as Number_of_Copies_Sold
from Customer_Order o join Customer_Order_Items i on o.Order_ID=i.Order_ID join Book b on b.ISBN=i.ISBN
where Month(Order_date) >= Month(DATE_SUB(curdate(),INTERVAL 3 MONTH)) AND YEAR(Order_date) = YEAR(DATE_SUB(Order_date,INTERVAL 3 MONTH)) 
group by b.ISBN
order by Number_of_Copies_Sold DESC
LIMIT 10
;

