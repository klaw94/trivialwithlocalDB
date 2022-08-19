package com.example.Trivial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HallOfFame extends JFrame {
    JLabel DBlabel = new JLabel();
    JLabel hallLabel[] = new JLabel[5];
    JButton btn = new JButton();
    JPanel panel = new JPanel();
    Box box = Box.createVerticalBox();


    public HallOfFame() {
        DBlabel.setText("Hall Of Fame");
        DBlabel.setSize(200,50);
        DBlabel.setBorder(new EmptyBorder(10,10,10,10));
        DBlabel.setFont(new Font("Mv Boli", Font.BOLD, 24));


        panel.add(box);
        this.add(panel);
        box.add(DBlabel);
        getInfoFromDB();
        this.setSize(400, 600);
        DBlabel.setVisible(true);
        panel.setVisible(true);
        this.setVisible(true);
        this.pack();

    }

    private void getInfoFromDB() {
        String sql = "SELECT * FROM trivial_people ORDER BY score DESC LIMIT 5";
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/newdb","postgres","password");
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            for(int i = 0; i < 5; i ++){
                rs.next();
                hallLabel[i] = new JLabel();
                hallLabel[i].setText(rs.getString("name") + " - " + rs.getInt("score"));
                box.add(hallLabel[i]);
            }
//            if (rs.next()) {
//                hallLabel.setText(rs.getString("name") + " - " + rs.getInt("score"));
//            }


        } catch(Exception e) {

        }
    }
}
