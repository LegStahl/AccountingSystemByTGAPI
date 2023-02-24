package service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.*;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import telegrambot.Bot;

public class Administrator extends Thread {
	
	private final String ADMIN = ;
	
	private String currentState = "";
	
	private final static long CHATID ;
	
	private final static String TEMPLATE[] = {"Списки", "Отметить" , "Сотрудников", "Детей", "Групп", "Добавить", "ЗП", "Назад", "Сотрудника", "Ребенка", "Группу"}; 
	
	private static Queue<Object> receivedQueue = new ConcurrentLinkedQueue<>();
	
	public Administrator() {
		init();
	}
	
	public String getNameAdmin() {
		return ADMIN;
	}
	
	public void run() {
		while(true) {
			if(!receivedQueue.isEmpty()){
				
				Object message = receivedQueue.poll();
				
				if(message instanceof Message) {
					processMessages((Message) message);
				}else if(message instanceof CallbackQuery) {
					processCallBack((CallbackQuery)message);
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
	
	public void processMessages(Message message) {
		String text = message.getText();
		boolean checkCommands = false;
		for(String str : TEMPLATE) {
			if(text.equals(str)) {
				checkCommands = true;
				break;
			}
		}
		
		if(checkCommands) {
			currentState =  currentState + "/" + text;
			System.out.println(currentState);
			if(currentState.endsWith(TEMPLATE[0])) {//Списки
				removeKeyBoard(message);
				SendMessage sendMessage = new SendMessage();
				sendMessage.setChatId(message.getChatId());
				ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
				keyboard.setOneTimeKeyboard(true);
				//System.out.println(keyboard.getOneTimeKeyboard());
				List<KeyboardRow> keyboardRow = new ArrayList<>();
				KeyboardRow row = new KeyboardRow();
				row.add("Сотрудников");
				row.add("Детей");
				row.add("Группу");
				row.add("Назад");
				keyboardRow.add(row);
				keyboard.setKeyboard(keyboardRow);
				 
				sendMessage.setReplyMarkup(keyboard);
				sendMessage.setText("Выберите список: детей/сотрудников/групп");
				QuerryMessageSender.addMessage(sendMessage);
				
			}else if(currentState.endsWith(TEMPLATE[1])) {//Отметить
//				SendMessage sendMessage = new SendMessage();
//				sendMessage.
			}else if(currentState.equals("/Списки/Сотрудников")){//Employee
				currentState = "";
				removeKeyBoard(message);
				init();
				listOfEmployees(message);
			}else if(currentState.equals("/Списки/Детей")){//Children
				currentState = "";
				removeKeyBoard(message);
				init();
				listOfChildren(message);
			}else if(currentState.equals("/Списки/Групп")){//Groups
				currentState = "";
				removeKeyBoard(message);
				init();
				listOfGroups(message);
			}else if(currentState.endsWith(TEMPLATE[5])) {//Add
				removeKeyBoard(message);
				SendMessage sendMessage = new SendMessage();
				sendMessage.setChatId(message.getChatId());
				ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
				keyboard.setOneTimeKeyboard(true);
				//System.out.println(keyboard.getOneTimeKeyboard());
				List<KeyboardRow> keyboardRow = new ArrayList<>();
				KeyboardRow row = new KeyboardRow();
				row.add("Сотрудника");
				row.add("Ребенка");
				row.add("Групп");
				row.add("Назад");
				keyboardRow.add(row);
				keyboard.setKeyboard(keyboardRow);
				 
				sendMessage.setReplyMarkup(keyboard);
				sendMessage.setText("Выберите кого добавить: ребенка/сотрудника/группу");
				QuerryMessageSender.addMessage(sendMessage);
				
			}else if(currentState.startsWith("/Добавить/Сотрудника") || currentState.startsWith("/Добавить/Группу") || currentState.startsWith("/Добавить/Ребенка") ) {
				removeKeyBoard(message);
				SendMessage sendMessage = new SendMessage();
				sendMessage.setChatId(message.getChatId());
				ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
				keyboard.setOneTimeKeyboard(true);
				//System.out.println(keyboard.getOneTimeKeyboard());
				List<KeyboardRow> keyboardRow = new ArrayList<>();
				KeyboardRow row = new KeyboardRow();
				row.add("Назад");
				keyboard.setKeyboard(keyboardRow);
				sendMessage.setReplyMarkup(keyboard);
			
				sendMessage.setText("Введите имя(название), для группы дату следующего занятия, пример(24-02-23)");
			}else if(currentState.endsWith(TEMPLATE[6])) {//Salary
				
			}else if(currentState.endsWith(TEMPLATE[7])) {//Back
				currentState = "";
				removeKeyBoard(message);
				init();
			}
		}else if(currentState.endsWith("/Добавить/Сотрудника") || currentState.endsWith("/Добавить/Группу") || currentState.endsWith("/Добавить/Ребенка")){
			if(currentState.contains("Сотрудника")) {
				addEmployee(message);
			}else if(currentState.contains("Группу")) {
				addGroup(message);
			}else if(currentState.contains("Ребенка")) {
				addChild(message);
			}
		}
		
	}
	
	private void addEmployee(Message message) {//Дописать 
		removeKeyBoard(message);
		init();
		
	}
	
	private void addChild(Message message) {
		removeKeyBoard(message);
		init();
	}
	
	private void addGroup(Message message) {
		removeKeyBoard(message);
		init();
	}
	
	private void listOfEmployees(Message message) {
		String listEmployees = Bot.DATA.getEmployees();
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId());
		sendMessage.setText(listEmployees);
		QuerryMessageSender.addMessage(sendMessage);
	}
	
	private void listOfChildren(Message message) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId());
		String str = Bot.DATA.getChildren();
		sendMessage.setText(str);
		QuerryMessageSender.addMessage(sendMessage);
	}
	
	private void listOfGroups(Message message) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId());
		String str = Bot.DATA.getGroups();
		sendMessage.setText(str);
		QuerryMessageSender.addMessage(sendMessage);
	}
	
	private void removeKeyBoard(Message message) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId());
		sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
		sendMessage.setText("_____________________________________________________");
		QuerryMessageSender.addMessage(sendMessage);
		
	}
	
	public void processCallBack(CallbackQuery message) {
		
	}
	
	private void init() {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(CHATID);
		ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboardRow = new ArrayList<>();
		KeyboardRow row = new KeyboardRow();
		row.add("Списки");
		row.add("Отметить");
		row.add("Добавить");
		row.add("ЗП");
		keyboardRow.add(row);
		keyboard.setKeyboard(keyboardRow);
		sendMessage.setReplyMarkup(keyboard);
		sendMessage.setText("Меню:");
		QuerryMessageSender.addMessage(sendMessage);
		
	}
	
	public void addMessage(Message message) {
		receivedQueue.add(message);
	}
	
	
}
