package com.fam.client;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.fam.VO.Data;
import com.fam.view.MainLoginController;
import com.fam.view.OverviewController;

public class ChatClient implements Runnable {

	private String serverIP;
	private int port;
	private String nickname;
	private String outputMsg;
	private Thread thread;
	private boolean flag;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	private Socket socket;
	private Data d;
	private OverviewController overviewController;
	
	Vector<String> userVector;

	public ChatClient(String serverIP, int port, String nickname, OverviewController controller) {
		this.serverIP = serverIP;
		this.port = port;
		this.nickname = nickname;
		this.overviewController = controller;
	}

	public void start() {
		System.out.println("여기 start()");
		try {
			
			socket = new Socket(serverIP, port);
			System.out.println("소켓 연결" + serverIP + " : " + port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("oos");
			ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("ois");

			Data d = new Data(nickname, "님이 접속하였습니다 !!!!! \n", Data.FIRST_CONNECTION);
			oos.writeObject(d);
			System.out.println("Server에 접속!!");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("스레드 생성 전");
		thread = new Thread(this);
		thread.start();
		System.out.println("스레드 생성했다아아아아!");

	}// end start

	
	public void run() {
		
		while (!flag) {
			try {
				d = (Data) ois.readObject();
			} catch (IOException e) {
				System.err.println("run method IOException");
				try {
					oos.close();
					ois.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				flag = true;
			} catch (ClassNotFoundException e1) {
				System.err.println("Data class NotFound");
			}
			int state = d.getState();
			String nickname = d.getName();
			System.out.println("name : " + nickname);
			System.out.println(d.toString());

			switch (state) {
			case Data.FIRST_CONNECTION:
			
				outputMsg = "[ " + nickname + " ]" + d.getMessage() + "\n";
				overviewController.appendText(outputMsg);
				
				userVector = d.getUserName(); //벡터에 user들을 담았음
			
				break;

			case Data.DISCONNECTION:
				userVector.remove(nickname);

				outputMsg = "[ " + nickname + " ]" + d.getMessage() + "\n";
				overviewController.appendText(outputMsg);
				break;

			case Data.CHAT_MESSAGE:
				outputMsg = "[ " + nickname + " ]" + d.getMessage() + "\n";
				overviewController.appendText(outputMsg);
				break;

			default:
				System.out.println("error");
			}// switch
		} // while
		
		try {
			ois.close();
		} catch (IOException e) {
			System.err.println(" ChatClientThread에의 ObjectOutputStream을 Close하는 중에 IOException이 발생하였습니다.");
		} // catch
	}// run

	public Vector<String> getUserVector() {
		System.out.println("getUserVector()");
		for(int i = 0; i < userVector.size(); i++) {
			System.out.println(userVector.get(i));
		}
		return userVector;
	}
	
	public void copyText(String message, int state) {
		try {
			oos.writeObject(new Data(nickname, message, state));
		} catch (IOException e2) {
			System.err.println("대화중 IOException이 발생하였습니다 ");
		}
	}
}
