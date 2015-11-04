package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginMenu extends JPanel{
	JTextField userInput;
	JPasswordField passInput;
	MainContainer setContainer;
	JButton loginButton, registerButton;
	
	public LoginMenu(MainContainer main) throws IOException{
		super(new GridBagLayout());
		setContainer = main;
		setupFocus();
		
		KeyListener enterLogin = new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					setContainer.login(userInput.getText(), new String(passInput.getPassword()));
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		};
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.3;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0,200,00,200);
		BufferedImage titleImage = ImageIO.read(getClass().getResource("/gfx/login.png"));
		this.add(new JLabel(new ImageIcon(titleImage)), gbc);
	
		Font inputFont = new Font("SansSerif", Font.PLAIN, 20);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 0.1;
		gbc.insets = new Insets(0,200,0,200);
		userInput = new JTextField("Username");
		userInput.setFont(inputFont);
		userInput.addKeyListener(enterLogin);
		this.add(userInput, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 0.1;
		passInput = new JPasswordField("Password");
		passInput.setFont(inputFont);
		passInput.addKeyListener(enterLogin);
		this.add(passInput, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weighty = 0.1;
		gbc.weightx = 0.4;
		gbc.insets = new Insets(10,200,30,0);
		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setContainer.login(userInput.getText(), new String(passInput.getPassword()));
			}
		});
		this.add(loginButton, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 0.1;
		gbc.insets = new Insets(10,5,30,200);
		registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setContainer.register(userInput.getText(), new String(passInput.getPassword()));
			}
		});
		this.add(registerButton, gbc);
	
	}
	
	void setupFocus(){
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("permanentFocusOwner", new PropertyChangeListener()
		{
		    public void propertyChange(final PropertyChangeEvent e)
		    {
		    	if (e.getNewValue() instanceof JTextField)
		    	{
		    		SwingUtilities.invokeLater(new Runnable()
		    		{
		    			public void run()
		    			{
		    				JTextField textField = (JTextField)e.getNewValue();
		    				textField.selectAll();
		    			}
		    		});
	
		    	}
		    }
		});
	}
}
