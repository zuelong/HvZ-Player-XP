package hvzxp;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.table.DefaultTableModel;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aaron
 */
public class HvZXP_Frame extends javax.swing.JFrame {
    private static DatabaseManager dm;
    private static MissionFrame mf;
    private static StartFrame sf;
    private static StopFrame stf;
    private static DefaultTableModel playerModel;
    private static DefaultTableModel missionModel;
    
    /**
     * Creates new form HvZXP_Frame
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public HvZXP_Frame() throws ClassNotFoundException, SQLException {
        initComponents();
        dm = new DatabaseManager();
        mf = new MissionFrame(this,dm);
        sf = new StartFrame(this);
        stf = new StopFrame(this);
        
        playerModel = (DefaultTableModel) playerTable.getModel();
        missionModel = (DefaultTableModel) missionTable.getModel();
        
        JMenuItem cm = new JMenuItem("Create Mission");
        JMenu sort = new JMenu("Sort");
        JMenuItem imp = new JMenuItem("Import Players");
        menu.add(cm);
        menu.add(sort);
        menu.add(imp);
        
        JMenu sortPlayers = new JMenu("Sort Players");
        JMenu sortMissions = new JMenu("Sort Missions");
        sort.add(sortPlayers);
        sort.add(sortMissions);
        
        JMenuItem sortPlayersName = new JMenuItem("By Name");
        JMenuItem sortPlayersXP = new JMenuItem("By Experience");
        JMenuItem sortMissionsName = new JMenuItem("By Name");
        JMenuItem sortMissionsStatus = new JMenuItem("By Status");
        sortPlayers.add(sortPlayersName);
        sortPlayers.add(sortPlayersXP);
        sortMissions.add(sortMissionsName);
        sortMissions.add(sortMissionsStatus);
        
        sortPlayersName.addActionListener((ActionEvent e) -> {
            try {
                showPlayerList();
            } catch (SQLException ex) {
                Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        sortPlayersXP.addActionListener((ActionEvent e) -> {
            try {
                playerModel.setRowCount(0);
                ResultSet rs = dm.sortPlayer("XP");
                
                while(rs.next()){
                    String currentMission = rs.getString("MISSION");
            
                    if(currentMission == null){
                        currentMission = "None";
                    }
            
                    playerModel.addRow(new Object[]{rs.getString("NAME"),rs.getInt("XP"),currentMission});
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        sortMissionsName.addActionListener((ActionEvent e) -> {
            try {
                missionModel.setRowCount(0);
                ResultSet rs = dm.sortMission("NAME");
                
                while(rs.next()){
            
                    String status;

                    if(rs.getInt("STATUS") == 1){
                        status = "Active";
                    }
                    else{
                        status = "Inactive";
                    }
            
                    missionModel.addRow(new Object[]{rs.getString("NAME"),rs.getInt("PLAYERS"),rs.getInt("XP"),status});
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        });
        
        sortMissionsStatus.addActionListener((ActionEvent e) -> {
            try {
                showMissionList();
            } catch (SQLException ex) {
                Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        imp.addActionListener((ActionEvent e) -> {
            try {
                importPlayers();
                showPlayerList();
            } catch (IOException | SQLException ex) {
                Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        cm.addActionListener((ActionEvent e) -> {
            createMission();
        });
        
        playerClear.addActionListener((ActionEvent e) -> {
            try {
                showPlayerList();
            } catch (SQLException ex) {
                Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        missionClear.addActionListener((ActionEvent e) -> {
            try {
                showMissionList();
            } catch (SQLException ex) {
                Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        playerField.addActionListener((ActionEvent e) -> {
            try {
                playerModel.setRowCount(0);
                ResultSet rs = dm.searchPlayer(playerField.getText());
                
                while(rs.next()){
                    String currentMission = rs.getString("MISSION");
            
                    if(currentMission == null){
                        currentMission = "None";
                    }
            
                    playerModel.addRow(new Object[]{rs.getString("NAME"),rs.getInt("XP"),currentMission});
                    playerField.setText("");
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        missionField.addActionListener((ActionEvent e) -> {
            try {
                missionModel.setRowCount(0);
                ResultSet rs = dm.searchMission(missionField.getText());
                
                while(rs.next()){
            
                    String status;

                    if(rs.getInt("STATUS") == 1){
                        status = "Active";
                    }
                    else{
                        status = "Inactive";
                    }
            
                    missionModel.addRow(new Object[]{rs.getString("NAME"),rs.getInt("PLAYERS"),rs.getInt("XP"),status});
                    missionField.setText("");
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        showPlayerList();
        showMissionList();
        
        
        
    }
    
    private void importPlayers() throws IOException, SQLException{
        dm.importPlayerList();
    }
    
    private void showPlayerList() throws SQLException{
        playerModel.setRowCount(0);
        ResultSet rs = dm.getPlayersByName();
        
        while(rs.next()){
            String currentMission = rs.getString("MISSION");
            
            if(currentMission == null){
                currentMission = "None";
            }
            
            playerModel.addRow(new Object[]{rs.getString("NAME"),rs.getInt("XP"),currentMission});
        }
        
    }
    
    public void showMissionList() throws SQLException{
        missionModel.setRowCount(0);
        ResultSet rs = dm.getMissionsByStatus();
        
        while(rs.next()){
            String status;
            
            if(rs.getInt("STATUS") == 1){
                status = "Active";
            }
            else{
                status = "Inactive";
            }
            
            missionModel.addRow(new Object[]{rs.getString("NAME"),rs.getInt("PLAYERS"),rs.getInt("XP"),status});
        }
    }
    
    private void createMission(){
        mf.setVisible(true);
    }
    
    public void stopMission(ArrayList<String> idList){
        try {
            // TODO add your handling code here:
            Mission m = dm.getSelectedMission((String) missionTable.getValueAt(missionTable.getSelectedRow(),0));
            
            /*for(int i = 0; i < m.players; i++){
                System.out.println("Enter: ");
                String player = in.nextLine();
                player = player.substring(player.length() - 9, player.length() - 1);
                System.out.println(player);
                idList.add(player);
            }*/
            //in.close();
            dm.stopMission(idList, m);
            showPlayerList();
            showMissionList();
        } catch (SQLException ex) {
            Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void startMission(ArrayList<String> idList){
        try {
            // TODO add your handling code here:
            Mission m = dm.getSelectedMission((String) missionTable.getValueAt(missionTable.getSelectedRow(),0));
            
            /*for(int i = 0; i < m.players; i++){
                System.out.println("Enter: ");
                String player = in.nextLine();
                player = player.substring(player.length() - 9, player.length() - 1);
                System.out.println(player);
                idList.add(player);
            }*/
            //in.close();
            dm.startMission(idList, m);
            showPlayerList();
            showMissionList();
        } catch (SQLException ex) {
            Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        playerTable = new javax.swing.JTable();
        playerField = new javax.swing.JTextField();
        missionField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        missionTable = new javax.swing.JTable();
        startButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        playerClear = new javax.swing.JButton();
        missionClear = new javax.swing.JButton();
        EditXPButton = new javax.swing.JButton();
        editXPField = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        menu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SSU HvZ Experience 1.0.0");

        playerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Player", "XP", "Mission"
            }
        ));
        jScrollPane1.setViewportView(playerTable);

        playerField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerFieldActionPerformed(evt);
            }
        });

        missionField.setToolTipText("");

        missionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mission", "Players", "XP", "Status"
            }
        ));
        jScrollPane2.setViewportView(missionTable);

        startButton.setText("Start Mission");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Stop Mission");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Search Players:");

        jLabel2.setText("Search Missions:");

        playerClear.setText("Clear");

        missionClear.setText("Clear");

        EditXPButton.setText("Edit XP");
        EditXPButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditXPButtonActionPerformed(evt);
            }
        });

        menu.setText("Menu");
        jMenuBar1.add(menu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(88, 88, 88))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(playerField, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(playerClear)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(missionField, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(missionClear))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopButton)
                        .addGap(99, 99, 99)
                        .addComponent(editXPField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EditXPButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(stopButton)
                    .addComponent(EditXPButton)
                    .addComponent(editXPField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(missionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(playerClear)
                    .addComponent(missionClear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void playerFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_playerFieldActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        sf.setVisible(true);
        
        
    }//GEN-LAST:event_startButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        // TODO add your handling code here:
        stf.setVisible(true);
    }//GEN-LAST:event_stopButtonActionPerformed

    private void EditXPButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditXPButtonActionPerformed
        // TODO add your handling code here:
        String newXP = editXPField.getText();
        System.out.println(newXP);
        String playerName = (String) playerTable.getValueAt(playerTable.getSelectedRow(), 0);
        try {
            showPlayerList();
        } catch (SQLException ex) {
            Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dm.updateXP(playerName,newXP);
        } catch (SQLException ex) {
            Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_EditXPButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HvZXP_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HvZXP_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HvZXP_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HvZXP_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new HvZXP_Frame().setVisible(true);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(HvZXP_Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton EditXPButton;
    private javax.swing.JTextField editXPField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenu menu;
    private javax.swing.JButton missionClear;
    private javax.swing.JTextField missionField;
    private javax.swing.JTable missionTable;
    private javax.swing.JButton playerClear;
    private javax.swing.JTextField playerField;
    private javax.swing.JTable playerTable;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables
}
