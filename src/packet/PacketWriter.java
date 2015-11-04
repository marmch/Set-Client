package packet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class PacketWriter {
	private ByteArrayOutputStream bos;
	
	public synchronized boolean SendPacket(OutputStream outstream) {
		ByteArrayOutputStream ps = bos;
		try {
			// [Short: Size] [Short: Header] [Data: Packet]
			outstream.write((byte) (ps.size() & 0xFF));
			outstream.write((byte) ((ps.size() >>> 8) & 0xFF));
			outstream.write(ps.toByteArray());
			outstream.flush();
			return true;
		} catch(IOException e) {
			return false;
		}
	}
	
	public PacketWriter(){
		bos = new ByteArrayOutputStream();
	}
	
	public void WriteHex(byte[] b) {
		for (int x = 0; x < b.length; x++) {
			bos.write(b[x]);
		}
	}
	
	public void WriteBool(boolean b) {
        byte x = (byte) (b ? 1 : 0);
        bos.write(x);
	}
	
	public void WriteByte(byte i) {
		bos.write(i);
	}
	
	public void WriteByte(int i) {
		bos.write(i);
	}
	
	public void WriteShort(int i) {
		bos.write((byte) (i & 0xFF));
		bos.write((byte) ((i >>> 8) & 0xFF));
	}
	
	public void WriteInt(int i) {
		bos.write((byte) (i & 0xFF));
		bos.write((byte) ((i >>> 8) & 0xFF));
		bos.write((byte) ((i >>> 16) & 0xFF));
		bos.write((byte) ((i >>> 24) & 0xFF));
	}
	
	public void WriteAnsiString(String s) {
		WriteShort((short)s.length());
		WriteHex(s.getBytes(Charset.forName("US-ASCII")));
	}
	
	public ByteArrayOutputStream getStream(){
		return bos;
	}
}