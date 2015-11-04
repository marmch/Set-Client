package packet;

public class PacketFactory {
	public static PacketWriter Login(String username, String password){
		PacketWriter p = new PacketWriter();
		p.WriteShort(0x00);
		p.WriteByte(0);
		p.WriteAnsiString(username);
		p.WriteAnsiString(password);
		return p;
	}
	
	public static PacketWriter Pong(int ticks){
		PacketWriter p = new PacketWriter();
		p.WriteShort(0x03);
		p.WriteInt(ticks);
		//p.WriteInt((int)System.currentTimeMillis());
		return p;
	}
	
	public static PacketWriter CreateGame(String gamename){
		PacketWriter p = new PacketWriter();
		p.WriteShort(0x08);
		p.WriteAnsiString(gamename);
		return p;
	}
	
	public static PacketWriter Chat(String message){
		PacketWriter p = new PacketWriter();
		p.WriteShort(0x0A);
		p.WriteAnsiString(message);
		return p;
	}
	
	public static PacketWriter JoinGame(int hostID){
		PacketWriter p = new PacketWriter();
		p.WriteShort(0x0C);
		p.WriteInt(hostID);
		return p;
	}
	
	public static PacketWriter Kick(int playerID){
		PacketWriter p = new PacketWriter();
		p.WriteShort(0x0D);
		p.WriteByte(0);
		p.WriteInt(playerID);
		return p;
	}
	
	public static PacketWriter StartGame(){
		PacketWriter p = new PacketWriter();
		p.WriteShort(0x0D);
		p.WriteByte(1);
		return p;
	}
	
	public static PacketWriter QuitGame(){
		PacketWriter p = new PacketWriter();
		p.WriteShort(0x0D);
		p.WriteByte(2);
		return p;
	}
	
	public static PacketWriter SelectSet(byte s1, byte s2, byte s3, byte n1, byte n2, byte n3){
		PacketWriter p = new PacketWriter();
		p.WriteShort(0x0F);
		p.WriteByte(s1);
		p.WriteByte(s2);
		p.WriteByte(s3);
		p.WriteByte(n1);
		p.WriteByte(n2);
		p.WriteByte(n3);
		return p;
	}
	
	public static PacketWriter Register(String username, String password){
		PacketWriter p = new PacketWriter();
		p.WriteShort(0x00);
		p.WriteByte(1);
		p.WriteAnsiString(username);
		p.WriteAnsiString(password);
		return p;
	}
}
