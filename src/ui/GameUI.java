package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import misc.CardPanel;
import misc.Player;

public class GameUI extends JPanel{
	MainContainer setContainer;
	JPanel gamePanel;
	JTextArea playerList;
	int[] oldCards = new int[25];
	CardPanel[][] cardGrid = new CardPanel[5][5];
	BufferedImage cards = ImageIO.read(getClass().getResource("/gfx/cards.png"));
	Image cardbg = ImageIO.read(getClass().getResource("/gfx/selection.png"));
	public List<Integer> setCards = new ArrayList<Integer>();
	public List<Integer> setNumCards = new ArrayList<Integer>();
	public int cwidth;
	public int cheight;
	
	public GameUI(MainContainer main) throws IOException{
		super(new FlowLayout());
		setContainer = main;
		cwidth = (int) (setContainer.width*0.75/cardGrid[0].length);
		cheight = (int) (setContainer.height/cardGrid.length);
		
		Arrays.fill(oldCards, -2);
		
		gamePanel = new JPanel(new GridLayout(cardGrid.length,cardGrid[0].length));
		gamePanel.setPreferredSize(new Dimension((int) (setContainer.width*0.75), setContainer.height-40));
		for(int i = 0; i < cardGrid.length; i++){
			for(int j = 0; j < cardGrid[i].length; j++){
				cardGrid[i][j] = new CardPanel(-1, cardGrid[i].length*i + j, cards, cardbg, this);
				cardGrid[i][j].revalidate();
				gamePanel.add(cardGrid[i][j]);
			}
		}
		this.add(gamePanel);
		
		playerList = new JTextArea();
		playerList.setEditable(false);
		JScrollPane playerScroller = new JScrollPane(playerList);
		playerScroller.setPreferredSize(new Dimension((int) (setContainer.width*0.22), setContainer.height-40));
		this.add(playerScroller);
	}
	
	public void updateInfo(int hostId, String gName, Player[] players, int[] cards){
		String playerInfo = "";
		for(int i = 0; i < players.length-1; i++){
			playerInfo += players[i].name + ": " + players[i].score + " Points\n";
		}
		playerInfo += players[players.length-1].name + ": " + players[players.length-1].score + " Points";
		playerList.setText(playerInfo);
		System.out.println("-------------------------------");
		for(int i = 0; i < oldCards.length; i++){
			int width = cardGrid[0].length;
			if(i >= cards.length){
				if(oldCards[i] == -1)
					continue;
				oldCards[i] = -1;
				cardGrid[i/width][i%width].changeCard(-1);
			}
			else{
				System.out.println(cards[i]);
				if(oldCards[i] == cards[i])
					continue;
				oldCards[i] = cards[i];
				cardGrid[i/width][i%width].changeCard(cards[i]);
			}
		}
		
		this.repaint();
	}
	
	public void sendSet(){
		setContainer.sendSet(setCards.get(0).byteValue(), setCards.get(1).byteValue(), setCards.get(2).byteValue(), setNumCards.get(0).byteValue(), setNumCards.get(1).byteValue(), setNumCards.get(2).byteValue());
		cardGrid[setCards.get(0)/cardGrid[0].length][setCards.get(0)%cardGrid[0].length].selected = false;
		cardGrid[setCards.get(0)/cardGrid[0].length][setCards.get(0)%cardGrid[0].length].repaint();
		cardGrid[setCards.get(1)/cardGrid[0].length][setCards.get(1)%cardGrid[0].length].selected = false;
		cardGrid[setCards.get(1)/cardGrid[0].length][setCards.get(1)%cardGrid[0].length].repaint();
		cardGrid[setCards.get(2)/cardGrid[0].length][setCards.get(2)%cardGrid[0].length].selected = false;
		cardGrid[setCards.get(2)/cardGrid[0].length][setCards.get(2)%cardGrid[0].length].repaint();
		setCards = new ArrayList<Integer>();
		setNumCards = new ArrayList<Integer>();
	}

}
