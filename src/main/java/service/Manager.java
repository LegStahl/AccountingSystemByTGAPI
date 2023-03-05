package service;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import telegrambot.Bot;

public class Manager extends Thread {
	
	private static Queue<Object> receivedQueue = new ConcurrentLinkedQueue<>();
	
	private boolean isWorking;
	
	private String userName;
	
	private long chatID;
	
	private String currentState = "";
	
	public Manager(String userName, long chatID) {
		
		isWorking = true;
		
		this.userName = userName;
		
		this.chatID = chatID;
		
		removeKeyBoard();
		
		init();
	}
	
	public String getNameManager() {
		return userName;
	}
	
	public boolean getIsWorking() {
		return isWorking;
	}
	
	public void run() {
		while(isWorking) {
			if(!receivedQueue.isEmpty()){
				
				Object message = receivedQueue.poll();
				
				if(message instanceof Message) {
					processMessages((Message) message);
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
	
	private void processMessages(Message message) {
		if(message.getText().equals("Работник")) {
			currentState = currentState + "Работник" + "/";
			SendMessage sendMessage = new SendMessage();
			sendMessage.setText("Пожалуйста введите своё фамилию, имя и идентификационный номер \n Пример: Попов Денис 98345");
			sendMessage(sendMessage);
		}
		else if(message.getText().equals("Родитель")) {
			currentState = currentState + "/" + "Родитель";
			SendMessage sendMessage = new SendMessage();
			sendMessage.setText("Пожалуйста введите фамилию, имя ребенка \n Пример: Попов Денис");
			sendMessage(sendMessage);
		}
		else {
			System.out.println("In manager 83");
			if(currentState.equals("Работник/")) {
				String[] name = message.getText().split(" "); 
				SendMessage sendMessage = new SendMessage();
				
				if(Bot.DATA.addToDataBase(name, userName)) {
					System.out.println("In manager 89");
					Employee employee = new Employee(userName, chatID);
					Bot.addEmployee(employee);
					employee.start();
					isWorking = false;
					sendMessage.setText("Вы авторизованы!");
					sendMessage(sendMessage);
				}else {
					sendMessage.setText("Что-то пошло не так.");
					sendMessage(sendMessage);
				}
			}
			else if(currentState.equals("Работник/")) {
					
				
			}
		}
		
	}
	
	private void init() {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatID);
		ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboardRow = new ArrayList<>();
		KeyboardRow row = new KeyboardRow();
		row.add("Работник");
		row.add("Родитель");
		keyboardRow.add(row);
		keyboard.setKeyboard(keyboardRow);
		sendMessage.setReplyMarkup(keyboard);
		sendMessage.setText("Меню: \n --------------------------- \n Выберите категорию, кем вы являетесь.");
		QuerryMessageSender.addMessage(sendMessage);
		
	}
	
	private void removeKeyBoard() {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatID);
		sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
		sendMessage.setText("_____________________________________________________");
		QuerryMessageSender.addMessage(sendMessage);
		
	}
	
	private void sendMessage(SendMessage sendMessage) {
		sendMessage.setChatId(chatID);
		QuerryMessageSender.addMessage(sendMessage);
	}
	
	public void addMessage(Object message) {
		receivedQueue.add(message);
	}
	
}
