package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import misc.Player;

public class GameLobby extends JPanel{
	MainContainer setContainer;
	GridBagConstraints gbc = new GridBagConstraints();
	JLabel username, wins;
	JTextArea chatLog, chatMessage;
	JButton kickButton, startButton;
	JScrollPane playerScroller, chatScroller;
	JList<String> playerList;
	int[] oldPlayerList = new int[0];
	
	public GameLobby(MainContainer main){
		super(new GridBagLayout());
		setContainer = main;
		
		KeyListener enterChat = new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					if(chatMessage.getText().length() > 0)
						setContainer.sendChat(chatMessage.getText());
					chatMessage.setText("");
					e.consume();
				}
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		};
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.8;
		gbc.weighty = 0.1;
		username = new JLabel("Username: " + setContainer.p.name);
		this.add(username, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 0.2;
		wins = new JLabel("Wins: " + setContainer.p.wins);
		this.add(wins, gbc);
		
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.weighty = 0.4;
		playerScroller = new JScrollPane();
		this.add(playerScroller, gbc);
		
		gbc.gridx = 1;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(0,1));
		startButton = new JButton("Start Game");
		startButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setContainer.startGame();
			}
		});
		kickButton = new JButton("Kick Player");
		kickButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				int x = playerList.getSelectedIndex();
				if(x != -1)
					setContainer.kickPlayer(oldPlayerList[x]);
			}
		});
		
		JButton leaveButton = new JButton("Leave Game");
		leaveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setContainer.leaveLobby();
			}
		});
		buttonPanel.add(startButton);
		buttonPanel.add(kickButton);
		buttonPanel.add(leaveButton);
		this.add(buttonPanel, gbc);
		
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.weighty = 0.4;
		gbc.gridwidth = 2;
		chatLog = new JTextArea();
		chatLog.setEditable(false);
		chatLog.setLineWrap(true);
		chatScroller = new JScrollPane(chatLog);
		this.add(chatScroller, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weighty = 0.02;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10,0,20,0);
		chatMessage = new JTextArea();
		chatMessage.setLineWrap(true);
		chatMessage.addKeyListener(enterChat);
		JScrollPane sendMsgScroller = new JScrollPane(chatMessage);
		this.add(sendMsgScroller, gbc);
		
		gbc.gridx = 1;
		JButton sendMsgButton = new JButton("Send");
		sendMsgButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(chatMessage.getText().length() > 0)
					setContainer.sendChat(chatMessage.getText());
				chatMessage.setText("");
			}
		});
		this.add(sendMsgButton, gbc);
	}
	
	public void updateChat(String message){
		chatLog.setText(chatLog.getText() + message + "\n");
		JScrollBar vertical = chatScroller.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum() );
		this.repaint();
	}

	public void updateInfo(int hostId, Player[] players){
		if(setContainer.p.id != hostId && oldPlayerList.length == 0){
			kickButton.setEnabled(false);
			startButton.setEnabled(false);
		}
		
		boolean update = false;
		if(oldPlayerList.length == players.length){
			for(int i = 0; i < players.length; i++){
				if(oldPlayerList[i] != players[i].id){
					update = true;
					break;
				}
			}
		}
		else
			update = true;
		
		if(update){
			oldPlayerList = new int[players.length];
			String[] newPlayers = new String[players.length];
			for(int i = 0; i < players.length; i++){
				if(players[i].id == hostId)
					newPlayers[i] = players[i].name + " (host) - " + players[i].wins + " Wins";
				else
					newPlayers[i] = players[i].name + " - " + players[i].wins + " Wins";
				oldPlayerList[i] = players[i].id;
			}
			
			playerList = new JList<String>(newPlayers);
			playerScroller.setViewportView(playerList);
		}
		
		this.repaint();
	}
	
	public void updatePlayerInfo() {
		this.username.setText("Username: " + setContainer.p.name);
		this.wins.setText("Wins: " + setContainer.p.wins);
	}
}
