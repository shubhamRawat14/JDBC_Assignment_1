package com.knoldus.JDBC;
import java.sql.*;
import java.util.Scanner;
public class JDBC_Assignment {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Products?characterEncoding=latin1","sqluser","Anton123456@");
            Statement stmt=con.createStatement();

            ResultSet rs=stmt.executeQuery("select * from Product");

            System.out.println("\t Products in DATABASE \n"+"-".repeat( 30 ) );
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+String.format("%" + 3 + "s", rs.getInt(2))+"  "+rs.getString(3));

            System.out.println("-".repeat( 30 ) );
            char ch='n';
            do{
                // to insert into the Cart Table
                System.out.print("\nEnter Product ID to add into the cart: ");
                int Pid = scanner.nextInt();
                System.out.print("\nEnter Quantity: ");
                int quantity = scanner.nextInt();
                PreparedStatement stm=con.prepareStatement("insert into Cart values(?,?)");
                stm.setInt(1,Pid);//1 specifies the first parameter in the query
                stm.setInt(2,quantity);

                int i=stm.executeUpdate();
                System.out.println(" item is added to the cart");

                System.out.print("\n Want to Continue y/n: ");

                ch = scanner.next().charAt(0);

            }while(ch!='n');
            rs=stmt.executeQuery("select * from Cart");
            int sum=0;
            System.out.println("\t Carts Database \n"+"-".repeat( 30 ) );
            while(rs.next()) {
                System.out.println(rs.getInt(1) + "  " + String.format("%" + 3 + "s", rs.getInt(2)));
                Statement st=con.createStatement();
                ResultSet addPrice= st.executeQuery("select * from Product where pid="+rs.getInt(1));
                addPrice.next();
                sum = sum+addPrice.getInt(2)* rs.getInt(2);
            }
            System.out.println("-".repeat( 30 ) );
            System.out.println("Total cost after checkout is: "+sum);

            //for empty the cart table
            PreparedStatement stm=con.prepareStatement("TRUNCATE TABLE Cart");
            stm.executeUpdate();
            System.out.println("Thank You");
            scanner.close();
            con.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
