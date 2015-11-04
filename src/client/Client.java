package client;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

import javax.swing.JOptionPane;

import misc.Player;
import misc.SetGame;

import packet.PacketFactory;
import packet.PacketReader;
import packet.PacketWriter;
import ui.MainContainer;

public class Client extends Thread {
	private Socket clientsocket;
	private DataOutputStream outstream;
	private boolean terminated = false;
	private static MainContainer m;
	
	public Client(Socket cs) {
		super();
		clientsocket = cs;
	}
	
	public void close() {
		terminated = true;
	}
	
	public synchronized void sendPacket(PacketWriter p) {
		ByteArrayOutputStream ps = p.getStream();
		try {
			// [Short: Size] [Short: Header] [Data: Packet]
			outstream.write((byte) (ps.size() & 0xFF));
			outstream.write((byte) ((ps.size() >>> 8) & 0xFF));
			outstream.write(ps.toByteArray());
			outstream.flush();
		} catch(IOException e) {
			close();
		}
	}
	
	private void parsePacket(PacketReader p) throws IOException {
		short header = p.ReadShort();
		switch(header) {
		case 0x01: // Change lobby
			int lobbyid = p.ReadInt();
			m.changeInterface(lobbyid);
			break;
		case 0x02: // Ping
			int ticks = p.ReadInt();
			PacketWriter pong = PacketFactory.Pong(ticks);
			sendPacket(pong);
			break;
		case 0x04: // get player info
			int playerID = p.ReadInt();
			int playerWins = p.ReadInt();
			String playerName = p.ReadAnsiString();
			m.updatePlayerInfo(playerName, playerID, playerWins);
			break;
		case 0x05:{ // get main lobby info
			short numPlayers = p.ReadShort();
			Player[] players = new Player[numPlayers];
			for(int i = 0; i < numPlayers; i++){
				int pId = p.ReadInt();
				String pName = p.ReadAnsiString();
				int pWins = p.ReadInt();
				players[i] = new Player(pId, pName, pWins);
			}
			short numGames = p.ReadShort();
			SetGame[] games = new SetGame[numGames];
			for(int i = 0; i < numGames; i++){
				int pId = p.ReadInt();
				String pName = p.ReadAnsiString();
				String gName = p.ReadAnsiString();
				int nPlayers = p.ReadInt();
				games[i] = new SetGame(pId, pName, gName, nPlayers);
			}
			m.updateMainLobby(players, games);
			break;}
		case 0x06:{ // update game lobby info
			int hostId = p.ReadInt();
			String gName = p.ReadAnsiString();
			short numPlayers = p.ReadShort();
			Player[] players = new Player[numPlayers];
			for(int i = 0; i < numPlayers; i++){
				int pId = p.ReadInt();
				String pName = p.ReadAnsiString();
				int pWins = p.ReadInt();
				players[i] = new Player(pId, pName, pWins);
			}
			m.updateGameLobby(hostId, gName, players);
			break;}
		case 0x07: //Update game info
			int hostId = p.ReadInt();
			String gName = p.ReadAnsiString();
			short numPlayers = p.ReadShort();
			Player[] players = new Player[numPlayers];
			for(int i = 0; i < numPlayers; i++){
				int pId = p.ReadInt();
				String pName = p.ReadAnsiString();
				int pWins = p.ReadInt();
				players[i] = new Player(pId, pName, pWins);
				players[i].score = p.ReadInt();
			}
			short numCards = p.ReadShort();
			int[] cards = new int[numCards];
			for(int i = 0; i < numCards; i++){
				cards[i] = p.ReadByte();
			}
			m.updateGame(hostId, gName, players, cards);
			break;
		case 0x09: // messagebox
			String messageBox = p.ReadAnsiString();
			JOptionPane.showMessageDialog(null, messageBox);
			//System.out.println(messageBox);
			break;
		case 0x0B: // chat message
			String chatMessage = p.ReadAnsiString();
			m.updateChat(chatMessage);
			break;
		case 0x0E: // Set functions
			int b = p.ReadByte();
			if(b==0){
				// Other got set
			}
			else if(b==1){
				// You got set
			}
			else if(b==2){
				JOptionPane.showMessageDialog(null, "You win!");
				m.changeInterface(1);
			}
			else if(b==3){
				JOptionPane.showMessageDialog(null, "You lose!");
				m.changeInterface(1);
			}
			break;
		}
		
	}
	
	@Override
	public void run() {
		try{
			BufferedInputStream in = new BufferedInputStream(clientsocket.getInputStream());
			outstream = new DataOutputStream(clientsocket.getOutputStream());
			while(!terminated){
				int available = in.available();
				if(available > 2){
					int PacketSize = in.read();
					PacketSize += in.read() << 8;
					if (PacketSize > available - 2) {
						System.out.println("Client: Invalid packet size...");
						break;
					}
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					for (int i=0; i < PacketSize; i++)
						buffer.write(in.read());
					buffer.flush();
					PacketReader p = new PacketReader(new ByteArrayInputStream(buffer.toByteArray()));
					try {
						parsePacket(p);
					} catch (Exception e) {
						System.out.println("Client: ParsePacket Error...");
					}
				}
				try {
					//ParseTimers();
				} catch (Exception e) {
					System.out.println("Client: ParseTimers Error...");
				}
				Thread.sleep(1);
			}
			System.out.println("Client: Connection Closed...");
			clientsocket.close();
			in.close();
			outstream.close();
		}catch(Exception e){
			System.out.println("Client: Main loop exception...");
		}
	}
	
	public static void main(String[] args) {
		Socket s;
		try {
			s = new Socket("199.98.20.124", 8888);
			Client c = new Client(s);
			c.start();
			
			m = new MainContainer(c);
			m.setVisible(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "FAILED TO CONNECT");
			e.printStackTrace();
		};
	}
}
