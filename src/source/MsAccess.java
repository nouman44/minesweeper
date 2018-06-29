/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Nouman
 */
public class MsAccess {
    
    MsAccess() {
    
    }
    
    public void insertStat(String d, int time, String status){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {

                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        }
        catch(ClassNotFoundException cnfex) {

            System.out.println("Problem in loading or "
                    + "registering MS Access JDBC driver");
            cnfex.printStackTrace();
        }
        
        try {

            String msAccDB = System.getProperty("user.dir") + "//DB//minesweeper.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB;

            // Step 2.A: Create and get connection using DriverManager class
            connection = DriverManager.getConnection(dbURL);

            // Step 2.B: Creating JDBC Statement 
            statement = connection.createStatement();

            // Step 2.C: Executing SQL & retrieve data into ResultSet
            //resultSet = statement.executeQuery("Insert into Player Values ('wajid')");
            PreparedStatement pst = connection.prepareStatement("INSERT INTO GameStats" + " (GameDate,Time,Status)" + " VALUES (?,?,?) ");
            pst.setString(1, d);
            pst.setInt(2, time);
            pst.setString(3, status);
            pst.executeUpdate();
            
        } catch (SQLException sqlex) {
            //sqlex.printStackTrace();
            System.out.println("Errores");
        } finally {

            // Step 3: Closing database connection
            try {
                if (null != connection) {

                    // cleanup resources, once after processing
                    //  resultSet.close();
                    statement.close();

                    // and then finally close connection
                    connection.close();
                }
            } catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }
    }
    
    public void fillStatistics(Statistics stat){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        GameStat g;
        
        try {

            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        }
        catch(ClassNotFoundException cnfex) {

            System.out.println("Problem in loading or "
                    + "registering MS Access JDBC driver");
            cnfex.printStackTrace();
        }
        
        try {

            String msAccDB = System.getProperty("user.dir")+"//DB//minesweeper.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB; 

            // Step 2.A: Create and get connection using DriverManager class
            connection = DriverManager.getConnection(dbURL); 

            // Step 2.B: Creating JDBC Statement 
            statement = connection.createStatement();

            // Step 2.C: Executing SQL & retrieve data into ResultSet
            resultSet = statement.executeQuery("SELECT * FROM GameStats");

            // processing returned data and printing into console
            while(resultSet.next()) {
                g = new GameStat(resultSet.getString(2),resultSet.getInt(3),resultSet.getString(4));
                stat.addGameToList(g);
            }
        }
        catch(SQLException sqlex){
            sqlex.printStackTrace();
        }
        finally {

            // Step 3: Closing database connection
            try {
                if(null != connection) {

                    // cleanup resources, once after processing
                    resultSet.close();
                    statement.close();

                    // and then finally close connection
                    connection.close();
                }
            }
            catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }

    }
}
