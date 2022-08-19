package com.example.Trivial;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NamePopUp extends JFrame implements ActionListener {
    String name;
    JLabel label = new JLabel();
    JButton btn = new JButton();
    JTextField nameField = new JTextField(10);
    JPanel panel = new JPanel();
    boolean registered = false;
    JLabel pointLabel;
    int score = 0;


    public NamePopUp(JLabel pointLabel, String name){
        label.setText("What's your name?");
        label.setSize(100,50);
        btn.setSize(10, 10);
        btn.setText("Submit");
        btn.addActionListener(this);
        label.setSize(100,50);
        nameField.setSize(200, 50);

        this.add(panel);
        panel.add(label);
        panel.add(nameField);
        panel.add(btn);
        this.setSize(200, 200);
        nameField.setVisible(true);
        label.setVisible(true);
        panel.setVisible(true);
        this.setVisible(true);
        this.pointLabel = pointLabel;
        this.name = name;
        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        name = nameField.getText();
        pointLabel.setText("Name: " + name + "      score: " + score);
        this.dispose();
    }

    public String getName(){
        return name;
}
}
