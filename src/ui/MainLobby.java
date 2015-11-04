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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import misc.Player;
import misc.SetGame;

public class MainLobby extends JPanel{
	MainContainer setContainer;
	GridBagConstraints gbc = new GridBagConstraints();
	JLabel username, wins;
	JTextArea playerList, chatLog, chatMessage;
	JButton joinButton;
	JScrollPane lobbyScroller, chatScroller;
	JList<String> lobbyList;
	int[] lobbyHosts = new int[0];
	
	public MainLobby(MainContainer main){
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
		lobbyScroller = new JScrollPane();
		this.add(lobbyScroller, gbc);
		
		gbc.gridx = 1;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(0,1));
		joinButton = new JButton("Join Game");
		joinButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				int x = lobbyList.getSelectedIndex();
				if(x != -1)
					setContainer.joinLobby(lobbyHosts[x]);
			}
		});
		JButton createButton = new JButton("Create Game");
		createButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String gName = "";
				while(gName == "")
					gName = JOptionPane.showInputDialog("Enter game name");
				setContainer.createLobby(gName);
			}
		});
		buttonPanel.add(joinButton);
		buttonPanel.add(createButton);
		this.add(buttonPanel, gbc);
		
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.weighty = 0.4;
		chatLog = new JTextArea();
		chatLog.setEditable(false);
		chatLog.setLineWrap(true);
		chatScroller = new JScrollPane(chatLog);
		this.add(chatScroller, gbc);
		
		gbc.gridx = 1;
		playerList = new JTextArea();
		playerList.setEditable(false);
		JScrollPane playerScroller = new JScrollPane(playerList);
		this.add(playerScroller, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weighty = 0.02;
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

	public void updateInfo(Player[] players, SetGame[] games){
		if(games.length == 0)
			joinButton.setEnabled(false);
		else
			joinButton.setEnabled(true);
		
		String playerInfo = "";
		for(int i = 0; i < players.length - 1; i++){
			playerInfo += players[i].name + " - " + players[i].wins + " Wins\n";
			if(setContainer.p.id != 0 && setContainer.p.id == players[i].id){
				setContainer.p.wins = players[i].wins;
				updatePlayerInfo();
			}
		}
		playerInfo += players[players.length-1].name + " - " + players[players.length-1].wins + " Wins";
		playerList.setText(playerInfo);
		
		boolean update = false;
		if(lobbyHosts.length == games.length){
			for(int i = 0; i < games.length; i++){
				if(lobbyHosts[i] != games[i].pId){
					update = true;
					break;
				}
			}
		}
		else
			update = true;
		if(update){
			lobbyHosts = new int[games.length];
			String[] lobbies = new String[games.length];
			for(int i = 0; i < games.length; i++){
				lobbies[i] = "Name: " + games[i].gName + " (Owner: " + 
						games[i].pName + ") - Players: " + games[i].nPlayers;
				lobbyHosts[i] = games[i].pId;
			}
			
			lobbyList = new JList<String>(lobbies);
			lobbyScroller.setViewportView(lobbyList);
		}
		
		this.repaint();
	}
	
	public void updatePlayerInfo() {
		this.username.setText("Username: " + setContainer.p.name);
		this.wins.setText("Wins: " + setContainer.p.wins);
		this.repaint();
	}
}
