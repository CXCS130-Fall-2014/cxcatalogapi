package ranking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;
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

    //Insert a single ranking into the database NOTE: DOES NOT ACCOUNT FOR OVERWRITING PRIMARY KEYS AND CATEGORY...
    //DEPRECATED FOR NOW...
    public void insertSinglePopularTag(String inKey, Integer inVal) {
        if (inKey == null && inVal == null) {
            return;
        }

        try {
            Class.forName(dbClass);
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();

            System.out.println("key: " + inKey + " value: " + inVal);

            String queryString = "INSERT INTO popular_tags(`key`, `value`, `add_date`) values (?, ?, ?)";
            PreparedStatement preparedSmt = connection.prepareStatement(queryString);

            preparedSmt.setString(1, inKey);
            preparedSmt.setInt(2, inVal);
            preparedSmt.setNull(3, java.sql.Types.TIMESTAMP);

            preparedSmt.execute();

            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Insert a new ranking into the database
    public void insertPopularTags(Map<String, Integer> popularTags, String category) {

        //Make sure popularTags is initialized
        if (popularTags == null) {
            return;
        }

        if (category == null || category.length() < 1) {
            return;
        }

        String cat_table;

        if (category == "clothes") {
            cat_table = "popular_clothes";
        }
        else if (category == "cars") {
            cat_table = "popular_cars";
        }
        else if (category == "electronics") {
            cat_table = "popular_electronics";
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

                System.out.println("!!!!!key: " + setKey + " value: " + setVal);

                String queryString = "INSERT INTO " + cat_table + "(`key`, `value`, `add_date`) values (?, ?, ?) ON " +
                        "DUPLICATE KEY UPDATE `value`=VALUES(`value`), `add_date`=VALUES(`add_date`)";
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


}