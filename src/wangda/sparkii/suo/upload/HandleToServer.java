package wangda.sparkii.suo.upload;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import wangda.sparkii.suo.xml.XmlEncoder;

public class HandleToServer {
	private Socket socket = null;

	private OutputStreamWriter output = null;
	private BufferedWriter bufOut;

	public HandleToServer(Socket socket) {
		try {
			this.socket = socket;
			this.output = new OutputStreamWriter(socket.getOutputStream(),
					"utf8");
			bufOut = new BufferedWriter(this.output);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	private void sendOut(String outString) {
		try {
			System.out.println("HandleToServer sendout: " + outString);
			bufOut.write(outString + "\r\n");
			bufOut.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * upload exp to server
	 * 
	 * @param toUser
	 * @param message
	 */
	public void uploadExp(String name, String user, String exp) {
		try {
			String outXml = XmlEncoder.encodeExp(name, user, exp);
			sendOut(outXml);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * user login or register
	 * 
	 * @param jsonString
	 */
	public void uploadLogin(String jsonString) {
		try {
			sendOut(jsonString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
