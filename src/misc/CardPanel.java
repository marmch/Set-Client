package misc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.GameUI;
import ui.MainContainer;

public class CardPanel extends JPanel implements MouseListener {
	BufferedImage cards;
	JLabel pic;
	int cardpos;
	int cardnum;
	GameUI gUI;
	boolean hover = false;
	public boolean selected = false;
	Image bg;
	
	public CardPanel(int cardnum, int cardpos, BufferedImage cards, Image bg, GameUI gUI) throws IOException{
		super(new BorderLayout());
		this.cardnum = cardnum;
		this.cardpos = cardpos;
		this.gUI = gUI;
		this.bg = bg;
		this.cards = cards; 
		if(cardnum == -1){
			Image card = cards.getSubimage(0, 0, 100, 66);
			pic = new JLabel(new ImageIcon(card));
			pic.addMouseListener(this);
			this.add(pic, BorderLayout.CENTER);
		}
		else{
			int x = cardnum % 9;
			int y = cardnum / 9;
			Image card = cards.getSubimage(100*x, 66*y, 100, 66);
			pic = new JLabel(new ImageIcon(card));
			pic.addMouseListener(this);
			this.add(pic, BorderLayout.CENTER);
		}
	}
	
	public void changeCard(int newCard){
		selected = false;
		this.cardnum = newCard;
		if(newCard == -1){
			pic.setIcon(null);
		}
		else{
			int x = newCard % 9;
			int y = newCard / 9;
			Image card = cards.getSubimage(100*x, 66*y, 100, 66);
			pic.setIcon(new ImageIcon(card));
		}
		this.revalidate();
	}

	public void mouseEntered(MouseEvent e) {
		hover = true;
		this.repaint();
	}

	public void mouseExited(MouseEvent e) {
		hover = false;
		this.repaint();
	}

	public void mousePressed(MouseEvent e) {
		System.out.println("------>" + cardnum);
		if(cardnum != -1){
			selected = !selected;
			this.revalidate();
			if(gUI.setCards.contains(cardpos)){
				gUI.setCards.remove(gUI.setCards.indexOf(cardpos));
				gUI.setNumCards.remove(gUI.setCards.indexOf(cardnum));
			}
			else{
				gUI.setCards.add(cardpos);
				gUI.setNumCards.add(cardnum);
				if(gUI.setCards.size() == 3){
					gUI.sendSet();
				}
			}
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void paintComponent(Graphics page)
	{
	    super.paintComponent(page);
	    this.setBackground(Color.WHITE);
	    if(bg != null){
	    	if((selected || hover) && cardnum != -1){
	    		bg = bg.getScaledInstance( gUI.cwidth, gUI.cheight, Image.SCALE_DEFAULT );
	    		page.drawImage(bg, 0, 0, null );
	    	}
		}
	}
}
