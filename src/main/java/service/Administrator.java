package service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.*;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import telegrambot.Bot;

public class Administrator extends Thread {
	
	private final String ADMIN = ;
	
	private String currentState = "";
	
	private final static long CHATID =  ;
	
	private final static String TEMPLATE[] = {"Списки", "Отметить" , "Сотрудников", "Детей", "Групп", "Добавить", "ЗП", "Назад", "Сотрудника", "Ребенка", "Группу", "Нагрузку", "Занятия"}; 
	
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
	
	public void processCallBack(CallbackQuery message) {
		if(currentState.equals("/Списки/Групп")) {
			String nameOfGroup = message.getData().toString();
			getChildrenFromGroup(message, nameOfGroup);
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
				row.add("Групп");
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
				//currentState = "";
				removeKeyBoard(message);
				SendMessage sendMessage = new SendMessage();
				sendMessage.setChatId(message.getChatId());
				sendMessage.setText("----");
				ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboardRow = new ArrayList<>();
				keyboard.setOneTimeKeyboard(true);
				KeyboardRow row = new KeyboardRow();
				row.add("Назад");
				keyboardRow.add(row);
				keyboard.setKeyboard(keyboardRow);
				sendMessage.setReplyMarkup(keyboard);
				QuerryMessageSender.addMessage(sendMessage);
				System.out.println(currentState + " 131");
				//init();
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
				row.add("Группу");
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
				QuerryMessageSender.addMessage(sendMessage);
			}else if(currentState.endsWith(TEMPLATE[6])) {//Salary
				
			}else if(currentState.endsWith(TEMPLATE[7])) {//Back
				currentState = "";
				removeKeyBoard(message);
				init();
			}
		}else if(currentState.endsWith("/Добавить/Сотрудника") || currentState.endsWith("/Добавить/Группу") || currentState.endsWith("/Добавить/Ребенка") || currentState.endsWith("/Добавить/Нагрузку") || currentState.endsWith("/Добавить/Занятия") ){
			if(currentState.contains("Сотрудника")) {
				addEmployee(message);
				
			}else if(currentState.contains("Группу")) {
				addGroup(message);
			}else if(currentState.contains("Ребенка")) {
				addChild(message);
			}
		}
		
	}
	
	private void getChildrenFromGroup(CallbackQuery message, String nameOfGroup) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getMessage().getChatId());
		List<String> listChildren = Bot.DATA.getChildrenFromGroup(nameOfGroup);
		StringBuilder str = new StringBuilder();
		for(String string : listChildren) {
			str.append(string);
		}
		sendMessage.setText(str.toString());
		if(sendMessage.getText().equals("")) {
			sendMessage.setText("Пока нет учеников!!!");
		}
		QuerryMessageSender.addMessage(sendMessage);
	}
	
	private void addEmployee(Message message) {//Дописать 
		removeKeyBoard(message);
		init();
		String fullname = message.getText();
		String[] str = fullname.split(" ");
		int check = Bot.DATA.addNewEmloyee(str);
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId());
		sendMessage.setText("Уникальный идентификационный номер нового сотрудника: " + check + " Если номер '0', произошла ошибка.");
		QuerryMessageSender.addMessage(sendMessage);
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
		List<String> listOfGroups = Bot.DATA.getGroups();
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
		
		for(String str : listOfGroups) {
			List<InlineKeyboardButton> listKeyboard = new ArrayList<>();
			StringTokenizer token = new StringTokenizer(str, "_");
			String callBack = token.nextToken();
			InlineKeyboardButton button = new InlineKeyboardButton(str.replace("_", " "));
			
			button.setCallbackData(callBack);
			listKeyboard.add(button);
			keyboard.add(listKeyboard);
		}
	
		inlineKeyboardMarkup.setKeyboard(keyboard);
		sendMessage.setReplyMarkup(inlineKeyboardMarkup);
		sendMessage.setText("Список групп");
		QuerryMessageSender.addMessage(sendMessage);
	}
	
	private void removeKeyBoard(Message message) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId());
		sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
		sendMessage.setText("_____________________________________________________");
		QuerryMessageSender.addMessage(sendMessage);
		
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
	
	public void addMessage(Object message) {
		receivedQueue.add(message);
	}
	
	
}
