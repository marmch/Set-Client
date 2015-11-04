package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import misc.Player;
import misc.SetGame;

import client.Client;

import packet.PacketFactory;
import packet.PacketWriter;

public class MainContainer extends JFrame{
	BoxLayout bl;
	JPanel container;
	JPanel mainPanel;
	Client c;
	int lobbyType = 0;
	public Player p = new Player(0, "???", 0);
	public int width = 740;
	public int height = 480;
	
	public MainContainer(Client c) throws IOException{
		super("Set Game");
		
		this.c = c;
		
		setUpContainer();
		
		BufferedImage titleImage = ImageIO.read(getClass().getResource("/gfx/hs.jpg"));
		JLabel title = new JLabel(new ImageIcon(titleImage));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		title.setMaximumSize(new Dimension(800,120));
		this.add(title);
		
		container = new JPanel();
		container.setAlignmentX(Component.CENTER_ALIGNMENT);
		container.setMaximumSize(new Dimension(width, height));
		container.setLayout(new BorderLayout());
		this.add(container);
		
		mainPanel = new LoginMenu(this);
		container.add(mainPanel, BorderLayout.CENTER);
	}
	
	private void setUpContainer(){
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(800, 600);
		this.setResizable(false);
		bl = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
		this.setLayout(bl);
	}
	
	public void changeInterface(int r) throws IOException{
		container.remove(mainPanel);
		switch(r){
		case 0:
			lobbyType = 0;
			mainPanel = new LoginMenu(this);
			break;
		case 1:
			if(lobbyType == 2){
				JOptionPane.showMessageDialog(null, "You have been kicked!");
			}
			lobbyType = 1;
			mainPanel = new MainLobby(this);
			break;
		case 2:
			lobbyType = 2;
			mainPanel = new GameLobby(this);
			break;
		case 3:
			lobbyType = 3;
			mainPanel = new GameUI(this);
			break;
		}
		container.add(mainPanel, BorderLayout.CENTER);
		container.revalidate();
		container.repaint();
	}
	
	public void updatePlayerInfo(String pName, int pId, int wins){
		this.p = new Player(pId, pName, wins);
		
		if(lobbyType == 1)
			((MainLobby)mainPanel).updatePlayerInfo();
		else if(lobbyType ==2)
			((GameLobby)mainPanel).updatePlayerInfo();
		
		mainPanel.repaint();
	}
	
	public void updateMainLobby(Player[] players, SetGame[] games){
		((MainLobby)mainPanel).updateInfo(players, games);
	}
	
	public void updateGameLobby(int hostId, String gName, Player[] players){
		((GameLobby)mainPanel).updateInfo(hostId, players);
	}
	
	public void updateGame(int hostId, String gName, Player[] players, int[] cards){
		((GameUI)mainPanel).updateInfo(hostId, gName, players, cards);
	}
	
	public void updateChat(String message){
		if(lobbyType == 1)
			((MainLobby)mainPanel).updateChat(message);
		else if(lobbyType ==2)
			((GameLobby)mainPanel).updateChat(message);
	}
	
	public void sendChat(String message){
		PacketWriter p = PacketFactory.Chat(message);
		c.sendPacket(p);
	}

	public void login(String username, String password) {
		PacketWriter p = PacketFactory.Login(username, password);
		c.sendPacket(p);
	}

	public void joinLobby(int hostID){
		PacketWriter p = PacketFactory.JoinGame(hostID);
		c.sendPacket(p);
	}
	
	public void createLobby(String gName){
		PacketWriter p = PacketFactory.CreateGame(gName);
		c.sendPacket(p);
	}

	public void startGame(){
		PacketWriter p = PacketFactory.StartGame();
		c.sendPacket(p);
	}
	
	public void kickPlayer(int pId){
		PacketWriter p = PacketFactory.Kick(pId);
		c.sendPacket(p);
	}
	
	public void leaveLobby(){
		lobbyType = 1;
		PacketWriter p = PacketFactory.QuitGame();
		c.sendPacket(p);
	}
	
	public void sendSet(byte c1, byte c2, byte c3, byte n1, byte n2, byte n3){
		PacketWriter p = PacketFactory.SelectSet(c1, c2, c3, n1, n2, n3);
		c.sendPacket(p);
	}
	
	public void register(String username, String password){
		PacketWriter p = PacketFactory.Register(username, password);
		c.sendPacket(p);
	}
	
	public void quitGame(){
		PacketWriter p = PacketFactory.QuitGame();
		c.sendPacket(p);
	}
}
