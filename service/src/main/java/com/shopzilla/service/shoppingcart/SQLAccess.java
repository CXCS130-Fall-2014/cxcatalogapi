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
import java.util.Map;
import java.util.Vector;
import com.shopzilla.service.shoppingcart.resource.Item;

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

    //Insert a new ranking into the database
    public void insertPopularTags(Map<String, Integer> popularTags) {

        //Make sure popularTags is initialized
        if (popularTags == null) {
            return;
        }

        try {
            Class.forName(dbClass);
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();

            //Retrieve the key value pairs
            String setKey;
            Integer setVal;
            for (String key : popularTags.keySet()) {
                setKey = key;
                setVal = popularTags.get(key);

                System.out.println("key: " + setKey + " value: " + setVal);

                String queryString = "INSERT INTO popular_tags(`key`, `value`, `add_date`) values (?, ?, ?)";
                PreparedStatement preparedSmt = connection.prepareStatement(queryString);

                preparedSmt.setString(1, setKey);
                preparedSmt.setInt(2, setVal);
                preparedSmt.setNull(3, java.sql.Types.TIMESTAMP);

                preparedSmt.execute();
            }
            connection.close();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public String getPopularTags(String key) {

        if (key.length() < 1) {
            return "0";
        }

        String query = "SELECT value FROM `popular_tags` WHERE `key` = '" + key + "'";
        String queryVal = new String();
        try {
            Class.forName(dbClass);
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            //Go through the results and add them into our return vector
            while(resultSet.next()) {
                //System.out.println(resultSet.getString("result_tag"));
                queryVal = resultSet.getString("value");
            }

            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return queryVal;
    }

    //Code inserts a tumblr tag into the database
    public void insertTumblrTags(String category, Vector<String> tags) {

        //Check for shitty parameters
        if (category.length() < 1 || tags.size() < 1) {
            return;
        }

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

        return;
     }

    //Get a number of strings based on category
    public Vector<String> getTumblrTags(String category, Integer limitNum) {

        //No shitty parameters please
        if (category.length() < 1 || limitNum < 1) {
            return new Vector<String>();
        }

        //Query will get tags by category, limit it to how many we asked for (though no guarantees it will fulfill that many
        //because if you ask for 500 tags, there might only exist 200 tags in the db so it will only return 200.
        //Orders them by descending date so you get the most recently added tags.
        String query = "SELECT result_tag FROM tumblr_tags WHERE category=\"" + category + "\" ORDER BY add_date DESC LIMIT " + limitNum.toString();
        //System.out.println(query);
        Vector<String> resultsList = new Vector<String>();

        try {
            Class.forName(dbClass);
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            //Go through the results and add them into our return vector
            while(resultSet.next()) {
                //System.out.println(resultSet.getString("result_tag"));
                resultsList.add(resultSet.getString("result_tag"));
            }

            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultsList;
    }

    //Take data on the the catalog's query tag and its other data elements. Inserts ONE catalog element.
    public void insertCatalogData(String keyword, Item catalogItem) {

        //Get the last image in the Vector array for images because it has the best dimensions
        Integer imageVectorSize = catalogItem.getImage_url().size();
        String image_url = catalogItem.getImage_url().elementAt(imageVectorSize-1);

        String redirect_url = catalogItem.getRedirect_url();
        String title = catalogItem.getTitle();
        String description = catalogItem.getDescription();
        Double price = (double)catalogItem.getPrice();  //NOTE. Casting this to a double because getPrice returns an int...error in the class?

        //Make sure we have a queried source and length for the string
        if (keyword.length() < 1 || redirect_url.length() < 1) {
            return;
        }

        try {
            Class.forName(dbClass);
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();

            //Set the query string
            String queryString = "INSERT INTO catalog_data(result_tag, add_date, image_url, redirect_url, title," +
                    "description, price) values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedSmt = connection.prepareStatement(queryString);

            //Set the values of the prepared statement
            preparedSmt.setString(1, keyword);
            preparedSmt.setNull(2, java.sql.Types.TIMESTAMP);
            preparedSmt.setString(3, image_url);
            preparedSmt.setString(4, redirect_url);
            preparedSmt.setString(5, title);
            preparedSmt.setString(6, description);
            preparedSmt.setDouble(7, price);

            //Execute the prepared statement
            preparedSmt.execute();

            connection.close();
            //System.out.println("FINISHED THE QUERY");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return;
    }

    //Returns the most recent catalog item associated with the source_tag.
    public Item getCatalogData(String keyword) {

        Item resultItem = new Item();

        //Check for bad parameters
        if (keyword.length() < 1) {
            return new Item();
        }

        //Query will fetch a catalog item based on the parameter used
        String query = "SELECT image_url, redirect_url, title, description, description, price FROM catalog_data WHERE result_tag=\"" +
               keyword + "\" ORDER BY add_date DESC LIMIT 1";

        try {
            Class.forName(dbClass);
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            //Go through the results and add them into our return vector
            while(resultSet.next()) {
                /*
                System.out.println(resultSet.getString("image_url"));
                System.out.println(resultSet.getString("redirect_url"));
                System.out.println(resultSet.getString("title"));
                System.out.println(resultSet.getString("description"));
                System.out.println(resultSet.getDouble("price")); */
                //resultItem.setImage_url("image_url");

                Vector<String> tempUrlHolder = new Vector();
                tempUrlHolder.add(resultSet.getString("image_url"));

                resultItem.setImage_url(tempUrlHolder);
                resultItem.setRedirect_url(resultSet.getString("redirect_url"));
                resultItem.setTitle(resultSet.getString("title"));
                resultItem.setDescription(resultSet.getString("description"));
                resultItem.setPrice((int)resultSet.getDouble("price"));
            }
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultItem;
    }

    //Remove duplicate tumblr tag entries to help clean up the database.
    //Will remove duplicates from specific categories. If category is 'all',
    //then it will clean the entire database
    public void removeDuplicates(String category) {
        System.out.println("SUP THERE MISTER");
        if (category == null) {
            return;
        }

        String query;
        String removeAllQuery = "DELETE s1 FROM tumblr_tags s1, tumblr_tags s2 WHERE s1.result_tag = s2.result_tag " +
                "AND s1.add_date < s2.add_date";
        String removeCatQuery = "DELETE s1 FROM tumblr_tags s1, tumblr_tags s2 WHERE s1.category=\'" + category + "\'" +
                " AND s1.result_tag = s2.result_tag AND s1.add_date < s2.add_date";

        if (category == "all") {
            query = removeAllQuery;
        }
        else {
            query = removeCatQuery;
        }

        //Run the database query
        try {
            Class.forName(dbClass);
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
     * Functions that are used for testing.
     */

    private void testGetCatalogData() {
        Item testItem = new Item();
        testItem = getCatalogData("test");
    }

    private void testInsertTumblrTags() {
        Vector<String> testVec = new Vector<String>();
        testVec.add("coffee");
        testVec.add("jellybeans");
        testVec.add("starbucks");
        testVec.add("papaya");
        testVec.add("buhao");
        testVec.add("ranch dressing");

        insertTumblrTags("test", testVec);
    }

    private void testInsertCatalogData() {
        String source_tag = "test";
        Item testItem = new Item();

        Vector<String> testVec = new Vector<String>();
        testVec.add("coffee.jpg");
        testVec.add("jellybeans.jpg");

        testItem.setImage_url(testVec);
        testItem.setRedirect_url("www.google.com");
        testItem.setTitle("Dog Food");
        testItem.setDescription("Your dog will love this. Trust us");
        testItem.setPrice(100);

        insertCatalogData(source_tag, testItem);
    }

    private void testGetTumblrTags() {
        Vector<String> testVec = getTumblrTags("test", 5);

        System.out.println(testVec.toString());
    }
}