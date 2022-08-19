package com.example.Trivial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrivialApplication implements ActionListener {

	JFrame frame;
	JPanel questionPanel;
	JPanel pointPanel;
	JLabel pointLabel;
	String name = "your name";
	int score = 0;
	JLabel question;
	JPanel answerPanel;
	JButton [] btns = new JButton [4];
	char correctAnswer;
	char myAnswer;
	int numberOfQuestions = 0;
	NamePopUp namePopUp;
	List<Integer> alreadyAsked = new ArrayList<Integer>();

	public TrivialApplication() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Trivial");
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(600, 300);

		questionPanel = new JPanel();
		questionPanel.setBackground(Color.GRAY);
		questionPanel.setVisible(true);

		answerPanel = new JPanel();
        answerPanel.setVisible(true);
		answerPanel.setLayout(new GridLayout(2,2));
		for (int i = 0; i < btns.length; i++){
			btns[i] = new JButton();
			btns[i].addActionListener(this);
			btns[i].setText("Answer");
			answerPanel.add(btns[i]);
		}


		question = new JLabel();
		question.setHorizontalAlignment(JLabel.CENTER);
		question.setForeground(Color.white);
		question.setFont(new Font("Mv Boli", Font.BOLD, 20));
		question.setText("Question?");

		pointPanel = new JPanel();
		pointPanel.setBackground(Color.GRAY);
		pointPanel.setVisible(true);

		pointLabel = new JLabel();
		pointLabel.setHorizontalAlignment(JLabel.CENTER);
		pointLabel.setForeground(Color.white);
		pointLabel.setFont(new Font("Mv Boli", Font.BOLD, 15));
		pointLabel.setText("Name: " + name + "      Score: " + score + "      Question: " + numberOfQuestions + "/10");

		pointPanel.add(pointLabel);
		questionPanel.add(question);
		frame.add(pointPanel, BorderLayout.SOUTH);
		frame.add(questionPanel, BorderLayout.NORTH);
		frame.add(answerPanel);

		getName();
		getQuestion();
	}

	private void getQuestion() {
		String sql = "SELECT * FROM trivial ORDER BY RANDOM() LIMIT 1";

		try {
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/newdb","postgres","password");
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
				//int id = rs.getInt("id");
			question.setText(rs.getString("question"));
			int id = rs.getInt("id");
			if(questionIsRepeated(id)){
				getQuestion();
				return;
			}else {
				alreadyAsked.add(id);
				if(rs.getString("question").toCharArray().length > 20){
					question.setFont(new Font("Mv Boli", Font.BOLD, 15));
				} else {
					question.setFont(new Font("Mv Boli", Font.BOLD, 20));
				}
				btns[0].setText(rs.getString("answerone"));
				btns[1].setText(rs.getString("answertwo"));
				btns[2].setText(rs.getString("answerthree"));
				btns[3].setText(rs.getString("answerfour"));
				correctAnswer = rs.getString("correctanswer").charAt(0);
			}



		} catch(Exception e) {

		}

	}

	private boolean questionIsRepeated(int id) {
		if(numberOfQuestions == 0){
			return false;
		} else {
			for (int i = 0; i < alreadyAsked.size(); i++){
				if(id == alreadyAsked.get(i)){
					return true;
				}
			}
		}
		return false;
	}

	private void getName() {
		namePopUp = new NamePopUp(pointLabel, name);
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new TrivialApplication();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
 		if(e.getSource() == btns[0]){
			myAnswer = 'A';
		} else if(e.getSource() == btns[1]){
			myAnswer = 'B';
		} else if(e.getSource() == btns[2]){
			myAnswer = 'C';
		} else if(e.getSource() == btns[3]){
			myAnswer = 'D';
		}

		 checkPoints();
		 checkWin();
	}

	private void checkWin() {
		if(numberOfQuestions == 10){
			for(int i = 0; i< 4; i++){
				btns[i].setEnabled(false);
			}
			name = namePopUp.getName();
			saveMeInDB();
		}
	}

	private void saveMeInDB() {
		try {
			//Creating Connection Object
			Connection connection= DriverManager.getConnection("jdbc:postgresql://localhost:5432/newdb","postgres","password");
			//Preapared Statement
			PreparedStatement ps =connection.prepareStatement("INSERT INTO trivial_people (name, score) VALUES (?, ?)");
			//Specifying the values of it's parameter
			ps.setString(1, name);
			ps.setInt(2, score);
			//Execute the query
			ps.executeUpdate();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		HallOfFame hallOfFame = new HallOfFame();
	}


	private void checkPoints() {
		if(correctAnswer == myAnswer){
			score++;
		}
		numberOfQuestions++;
		refreshLabel();

		getQuestion();


	}

	private void refreshLabel() {
		pointLabel.setText("Name: " + name + "      Score: " + score + "      Question: " + numberOfQuestions + "/10");
	}


}
