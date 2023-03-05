package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class Employee extends Thread {
	
	private String userName;
	
	private long chatID;
	
	private Queue<Object> receivedQueue = new ConcurrentLinkedQueue<>();
	
	private String currentState = "";
	
	public Employee(String userName, long chatID) {
	
		this.userName = userName;
		this.chatID = chatID;
		removeKeyBoard();
		init();
	}
	
	public void run() {
		while(true) {
			if(!receivedQueue.isEmpty()){
				System.out.println("In Employee 36");
				Object message = receivedQueue.poll();
				
				if(message instanceof Message) {
					SendMessage sendMessage = new SendMessage();
					sendMessage.setChatId(chatID);
					sendMessage.setText("Hello Employee");
					QuerryMessageSender.addMessage(sendMessage);
				}else if(message instanceof CallbackQuery) {
					//processCallBack((CallbackQuery)message);
				}
				
				
			
			}else {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void init() {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatID);
		ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboardRow = new ArrayList<>();
		KeyboardRow row = new KeyboardRow();
		row.add("Списки");
		row.add("Отметить");
		keyboardRow.add(row);
		keyboard.setKeyboard(keyboardRow);
		sendMessage.setReplyMarkup(keyboard);
		sendMessage.setText("Меню:");
		QuerryMessageSender.addMessage(sendMessage);
		
	}
	
	private void removeKeyBoard() {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatID);
		sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
		sendMessage.setText("_____________________________________________________");
		QuerryMessageSender.addMessage(sendMessage);
		
	}
	
	private void processMessage(Message message) {
		
	}
	
	private void processCallBack(CallbackQuery message) {
		
	}
	
	public String getNameEmployee() {
		return userName;
	}
	
	public void addMessage(Object message) {
		receivedQueue.add(message);
	}
		
	
}
