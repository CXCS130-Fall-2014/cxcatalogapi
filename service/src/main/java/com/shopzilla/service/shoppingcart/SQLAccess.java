package com.shopzilla.service.shoppingcart;

/**
 * Created by justin on 11/15/14.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

public class SQLAccess {

    /* THE FOLLOWING ARE THE DATABASE CONFIGURATIONS */
    //CHANGE THIS TO YOUR DATABASE PATH.
    private static String dbUrl = "jdbc:mysql://localhost:8889/catalog_site";
    //CHANGE YOUR MYSQL CREDENTIALS HERE.
    private static String username = "root";
    private static String password = "root";

    private static String dbClass = "com.mysql.jdbc.Driver";


    public SQLAccess() {
        // nothing lol

    }

    public void insertTumblrTags(String category, Vector<String> tags) {
        String query = "SELECT * from tumblr_tags";

        //Get the size of the tags vector to use later for looping
        Integer inputSize = tags.size();

        try {
            Class.forName(dbClass);
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();

            //Loop through the vector and add these values to the database
            for (Integer x = 0; x < inputSize; x++) {

                //Set the query string
                String queryString = "INSERT INTO tumblr_tags(category, result_tag, add_date) values (?, ?, ?)";
                PreparedStatement preparedSmt = connection.prepareStatement(queryString);

                //Set the values of the prepared statement
                preparedSmt.setString(1, category);
                preparedSmt.setString(2, tags.elementAt(x));
                preparedSmt.setNull(3, java.sql.Types.TIMESTAMP);

                //Execute the prepared statement
                preparedSmt.execute();

                //System.out.println("Adding " + tags.elementAt(x) + " to category " + category);
            }
            //Close the connection or bad things will happen!
            connection.close();

            //System.out.println("FINISHED THE QUERY");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

            /*
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String tableName = resultSet.getString(1);
                System.out.println("Table name : " + tableName);
            }
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
     }

    public void testInsertTumblrTags() {
        Vector<String> testVec = new Vector<String>();
        testVec.add("coffee");
        testVec.add("jellybeans");
        testVec.add("starbucks");

        insertTumblrTags("test", testVec);
    }


}
