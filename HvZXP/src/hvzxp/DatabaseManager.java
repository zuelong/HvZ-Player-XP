/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvzxp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aaron
 */
public class DatabaseManager {
    private static Connection c;
    private static Statement s;
    
    public DatabaseManager() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:HvZ.db");
        s = c.createStatement();
        s.execute("CREATE TABLE IF NOT EXISTS players (ID TEXT PRIMARY KEY, NAME TEXT, XP TEXT, MISSION TEXT);");
        s.execute("CREATE TABLE IF NOT EXISTS missions (NAME TEXT PRIMARY KEY, XP INT, PLAYERS INT, DESC TEXT, STATUS INT);");
    }
    
    public void updateXP(String name, String xp) throws SQLException{
        s.execute("UPDATE players set XP="+xp+" WHERE NAME LIKE \""+name+"\";");
    }
    
    public ResultSet sortMission(String order) throws SQLException{
        s.execute("SELECT * FROM missions ORDER BY " + order + ";");
        return s.getResultSet();
    }
    
    public ResultSet sortPlayer(String order) throws SQLException{
        s.execute("SELECT * FROM players ORDER BY " + order + ";");
        return s.getResultSet();
    }
    
    
    public void addMission(Mission m) throws SQLException{
        PreparedStatement update = c.prepareStatement("INSERT INTO missions VALUES (?,?,?,?,?)");
        update.setString(1, m.name);
        update.setInt(2, m.xp);
        update.setInt(3, m.players);
        update.setString(4, m.desc);
        update.setInt(5, m.status);
        update.executeUpdate();
    }
    
    public void startMission(ArrayList<String> p,Mission m) throws SQLException{
        String playerString = "";
        String additionalPlayer = "ID=? OR ";
        
        for(int i = 0; i < p.size() - 1 ; i++){
            playerString += additionalPlayer;
        }
        playerString += "ID=?";
        
        PreparedStatement update = c.prepareStatement("UPDATE players set MISSION=? WHERE " + playerString + ";");
        System.out.println(m.name);
        update.setString(1, m.name);
        
        for(int i = 2; i-2 < p.size(); i++){
            update.setString(i, p.get(i-2));
        }
        
        update.execute();
        update = c.prepareStatement("UPDATE missions set STATUS=1 WHERE NAME=?;");
        update.setString(1, m.name);
        update.execute();
        
    }
    
    public void stopMission(ArrayList<String> p,Mission m) throws SQLException{
        String playerString = "";
        String additionalPlayer = "ID=? OR ";
        
        for(int i = 0; i < p.size() - 1 ; i++){
            playerString += additionalPlayer;
        }
        playerString += "ID=?";
        
        PreparedStatement update = c.prepareStatement("UPDATE players set XP = XP + ? WHERE " + playerString + ";");
        update.setInt(1, m.xp);
        
        for(int i = 2; i-2 < p.size(); i++){
            update.setString(i, p.get(i-2));
        }
        
        update.execute();
        update = c.prepareStatement("UPDATE missions set STATUS=0 WHERE NAME=?;");
        update.setString(1, m.name);
        update.execute();
        update = c.prepareStatement("UPDATE players set MISSION=NULL WHERE MISSION=?;");
        update.setString(1, m.name);
        update.execute();
        
    }
    
    public ResultSet searchPlayer(String player) throws SQLException{
        PreparedStatement update = c.prepareStatement("SELECT * FROM players WHERE NAME LIKE ?;");
        update.setString(1, player);
        ResultSet rs = update.executeQuery();
        return rs;
    }
    
    public ResultSet searchMission(String mission) throws SQLException{
        PreparedStatement update = c.prepareStatement("SELECT * FROM missions WHERE NAME LIKE ?;");
        update.setString(1, mission);
        ResultSet rs = update.executeQuery();
        return rs;
    }
    
    public void importPlayerList() throws IOException, SQLException{
        
        Files.lines(new File("player_scan.txt").toPath()).forEach((String x) -> {
            try {
                PreparedStatement update = c.prepareStatement("INSERT INTO players VALUES (?,?,?,?);");
                update.setString(1, x.substring(x.length()-8));
                update.setString(2, x.substring(0,x.length()-9));
                update.setInt(3, 0);
                update.setString(4, null);
                update.executeUpdate();
                
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    }
    
    public ResultSet getPlayersByName() throws SQLException{
        s.execute("SELECT * FROM players ORDER BY NAME;");
        ResultSet rs = s.getResultSet();
        return rs;
    }
    
    public ResultSet getPlayersByXP() throws SQLException{
        s.execute("SELECT * FROM players ORDER BY XP;");
        ResultSet rs = s.getResultSet();
        return rs;
    }
    
    public ResultSet getMissionsByName() throws SQLException{
        s.execute("SELECT * FROM missions ORDER BY NAME;");
        ResultSet rs = s.getResultSet();
        return rs;
    }
    
    public ResultSet getMissionsByStatus() throws SQLException{
        s.execute("SELECT * FROM missions ORDER BY STATUS;");
        ResultSet rs = s.getResultSet();
        return rs;
    }
    
    public Mission getSelectedMission(String name) throws SQLException{
        Mission m;
        PreparedStatement update = c.prepareStatement("SELECT * FROM missions WHERE NAME=?;");
        update.setString(1, name);
        ResultSet rs = update.executeQuery();
        //ResultSet rs = update.getResultSet();
        rs.next();
        m = new Mission(rs.getString("NAME"),rs.getInt("PLAYERS"),rs.getInt("XP"),rs.getString("DESC"));
        m.status = rs.getInt("STATUS");
        
        return m;
    }
    
}
