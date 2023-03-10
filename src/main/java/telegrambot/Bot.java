package telegrambot;
import database.*;
import service.Administrator;
import service.Employee;
import service.Manager;
import service.QuerryMessageSender;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.util.*;

public class Bot extends TelegramLongPollingBot {
	
	private static  Administrator ADMINISTRATOR = null;
	
	private static final String CEO= null;
	
	private static QuerryMessageSender querySend;
	
	public static DataBase DATA;
	
	private static List<Employee> EMPLOYEES = new ArrayList<Employee>();
	
	private static List<Manager> MANAGERS = new ArrayList<Manager>();
	
	public static synchronized void addEmployee(Employee employee) {
		EMPLOYEES.add(employee);
	}
	
	private synchronized boolean checkManager(String username) {
		if(MANAGERS.isEmpty()) {
			return false;
		}
		else {
			for(Manager manager : MANAGERS) {
				if(manager.getNameManager().equals(username)) {
					return true;
				}
			}
			return false;
		}
	}
	
	@Override
	public void onUpdateReceived(Update update) {
		System.out.println(EMPLOYEES.size() + " " + MANAGERS.size());
		if(update.hasMessage()) {
			
			handleMessage(update.getMessage());
			Message message = update.getMessage();
			System.out.println(message.getFrom().getUserName());
			if(message.getFrom().getUserName().equals(ADMINISTRATOR.getNameAdmin())) {
				System.out.println("64");
				ADMINISTRATOR.addMessage(message);
			}
			else if(DATA.checkUserName(message.getFrom().getUserName())) {
				if(EMPLOYEES.isEmpty()) {
					Employee employee = new Employee(message.getFrom().getUserName(), message.getChatId());
					employee.start();
					addEmployee(employee);
					System.out.println("68");
				}else {
					boolean  checkAttendance = false;
					for(Employee employee : EMPLOYEES) {
						System.out.println("76");
						if(employee.getNameEmployee().equals(message.getFrom().getUserName())) {
							employee.addMessage(message);
							System.out.println("79");
							checkAttendance = true;
							break;
						}
					}
					if(!checkAttendance) {
						Employee employee = new Employee(message.getFrom().getUserName(), message.getChatId());
						employee.start();
						EMPLOYEES.add(employee);
					}
				}
				
			}
			else if(checkManager(message.getFrom().getUserName())) {
				
				for(Manager manager : MANAGERS) {
					System.out.println("72");
					if(manager.getNameManager().equals(message.getFrom().getUserName())) {
						manager.addMessage(update.getMessage());
					}
				}
			}
			else {
				Manager manager = new Manager(update.getMessage().getFrom().getUserName(), update.getMessage().getChatId());
				//manager.addMessage(message);
				System.out.println("80");
				MANAGERS.add(manager);
				manager.start();
				
			}
		}else if(update.hasCallbackQuery()) {
			CallbackQuery message = update.getCallbackQuery();
			if(message.getFrom().getUserName().equals(ADMINISTRATOR.getNameAdmin())) {
				ADMINISTRATOR.addMessage(message);
			}
		}
		System.out.println("92");
		checkManagerIsWorking();
//		else if(update.hasCallbackQuery()) {
//			
////			handleMessage(update.getCallbackQuery().getMessage());
//			System.out.println(update.getCallbackQuery().getData());
//			SendMessage message = new SendMessage();
//			message.setChatId(1070270011l);
//			message.setText("eeeee");
//			System.out.println(update.getCallbackQuery().getMessage().getChatId());
//			try {
//				execute(message);
//			} catch (TelegramApiException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
		
	}
	
	private synchronized void checkManagerIsWorking() {
		for(Manager manager: MANAGERS) {
			if(!manager.getIsWorking()) {
				MANAGERS.remove(manager);
			}
		}
	}

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "";
	}
	
	@Override
	public String getBotToken() {
		return "";
		
	}

	public void handleMessage(Message message)  {
		
//		if(message.hasText() ) {
//			if(message.getFrom().getUserName().equals(ADMINNAME) || message.getFrom().getUserName().equals(CEO)) {
//				System.out.println(message.getText());
//				if(message.getText().contains("??????e????????")) {
//					SendMessage message2 = new SendMessage();
//					message2.setChatId(message.getChatId());
//					message2.setText("???????????????? ?????????????? ??????????????...");
//					message2.setReplyMarkup(new ReplyKeyboardRemove(true));
//					try {
//						execute(message2);
//					    try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					    message2 = new SendMessage();
//				
//					    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//					    
//				        // Create the keyboard (list of InlineKeyboardButton list)
//				        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
//				        // Create a list for buttons
//				        List<InlineKeyboardButton> Buttons = new ArrayList<InlineKeyboardButton>();
//				        List<InlineKeyboardButton> Buttons2 = new ArrayList<InlineKeyboardButton>();
//				        List<InlineKeyboardButton> Buttons3 = new ArrayList<InlineKeyboardButton>();
//				        List<InlineKeyboardButton> Buttons4 = new ArrayList<InlineKeyboardButton>();
////				        List<InlineKeyboardButton> Buttons5 = new ArrayList<InlineKeyboardButton>();
//				        // Initialize each button, the text must be written
//				        InlineKeyboardButton youtube= new InlineKeyboardButton();
//				        // Also must use exactly one of the optional fields,it can edit  by set method
//				        for(int i = 0; i < 20; i++) {
//				        		youtube= new InlineKeyboardButton("?????? ???? ???? 18:30" + i);
//				        		youtube.setCallbackData("The buttons" + i + "has been pressed");
//				        		if(i >= 3 && i < 6) {
//				        			Buttons2.add(youtube);
//				        			
//				        		}
//				        		if(i >= 6 && i < 9) {
//				        			Buttons3.add(youtube);
//				        		}
//				        		if(i >= 9 && i < 12 ) {
//				        			Buttons4.add(youtube);
//				        		}
//				        		
//				        		
//				        }
//				       
//				        keyboard.add(Buttons);
//				        keyboard.add(Buttons2);
//				        keyboard.add(Buttons3);
//				        keyboard.add(Buttons4);
//				        inlineKeyboardMarkup.setKeyboard(keyboard);
//				        // Add it to the message
//				        message2.setReplyMarkup(inlineKeyboardMarkup);
////						ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
////						System.out.println(keyboard.getOneTimeKeyboard());
////						List<KeyboardRow> keyboardRow = new ArrayList<>();
////						KeyboardRow row = new KeyboardRow();
////						row.add("????????????");
////						row.add("???????????? 2");//???????????? ?????????????? ?????????? ???????????????? ???? ????????????????????
////						row.add("???????????? 3");
////						keyboardRow.add(row);
////						keyboard.setKeyboard(keyboardRow);
////						 
////						message2.setChatId(message.getChatId());
//						message2.setText("Pick up group");
//						System.out.print(message.getChatId());
////						message2.setReplyMarkup(keyboard);
//					    message2.setChatId(message.getChatId());
//						System.out.println(execute(message2));
//					} catch (TelegramApiException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}else {
//					ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
//					keyboard.setOneTimeKeyboard(true);
//					System.out.println(keyboard.getOneTimeKeyboard());
//					List<KeyboardRow> keyboardRow = new ArrayList<>();
//					KeyboardRow row = new KeyboardRow();
//					row.add("??????e????????");
//					row.add("??????a???????? ???????????? ????????????????????");//???????????? ?????????????? ?????????? ???????????????? ???? ????????????????????
//					row.add("??????a???????? ???????? ????????????????????");
//					keyboardRow.add(row);
//					keyboard.setKeyboard(keyboardRow);
//					 
//					SendMessage message2 = new SendMessage();
//					message2.setReplyMarkup(keyboard);
//					
//					
//					message2.setChatId(message.getChatId());
//					message2.setText("What do you want my milord?" + " " + DATA.getLastID());
//					
//					try {
//						execute(message2);
//					} catch (TelegramApiException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}else if(DATA.checkTeachers(message.getFrom().getUserName())) {
//				if(message.getText().contains("???????????????? ")) {
//					System.out.println(message.getFrom().getUserName());
//					SendMessage message2 = new SendMessage();
//			        message2.setChatId(message.getChatId());
//			        message2.setText("Custom message text");
//			        
//			        // Create ReplyKeyboardMarkup object
//			        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//			        // Create the keyboard (list of keyboard rows)
//			        List<KeyboardRow> keyboard = new ArrayList<>();
//			        
//			        // Create a keyboard row
//			        KeyboardRow row = new KeyboardRow();
//			        // Set each button, you can also use KeyboardButton objects if you need something else than text
//			        row.add("Row 1 Button 1");
//			        row.add("Row 1 Button 2");
//			        row.add("Row 1 Button 3");
//			        // Add the first row to the keyboard
//			        keyboard.add(row);
//			        // Create another keyboard row
//			        row = new KeyboardRow();
//			        // Set each button for the second line
//			        row.add("Row 3 Button 1");
//			        row.add("Row 2 Button 2");
//			        row.add("Row 2 Button 3");
//			        // Add the second row to the keyboard
//			        keyboard.add(row);
//			        // Set the keyboard to the markup
//			        keyboardMarkup.setKeyboard(keyboard);
//			        // Add it to the message
//			        message2.setReplyMarkup(keyboardMarkup);
//			        
//			        try {
//			            // Send the message
//			            execute(message2);
//			        } catch (TelegramApiException e) {
//			            e.printStackTrace();
//			        }
//				}
//				else if(message.getText().equals("???????????????? ???????????????????? ")) {
//					
//				}
//			}else {
//				SendMessage sendMessage = new SendMessage();
//				sendMessage.setChatId(message.getChatId());
//				sendMessage.setText("?????? ?????");
//				try {
//					execute(sendMessage);
//				}catch(TelegramApiException e){
//					e.printStackTrace();
//					
//				}
//			}
//	    }
//			
	}
	
	private String getSchedule(String usernameOfTeacher) {
		String schedule = DATA.getSchedule(usernameOfTeacher);
		return schedule;
	}
	
	/*
	//message.getFrom();
	SendMessage sendMessage = new SendMessage();
	sendMessage.setChatId(message.getChatId().toString());
	sendMessage.setText("???????????? ???? ???? ????????");
	System.out.println(message.getFrom().getUserName());
	ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    // Create the keyboard (list of keyboard rows)
    List<KeyboardRow> keyboard = new ArrayList<>();
    // Create a keyboard row
    KeyboardRow row = new KeyboardRow();
    // Set each button, you can also use KeyboardButton objects if you need something else than text
    row.add("Row 1 Button 1");
    row.add("Row 1 Button 2");
    row.add("Row 1 Button 3");
    // Add the first row to the keyboard
    keyboard.add(row);
    // Create another keyboard row
    row = new KeyboardRow();
    // Set each button for the second line
    row.add("Row 2 Button 1");
    row.add("Row 2 Button 2");
    row.add("Row 2 Button 3");
    // Add the second row to the keyboard
    keyboard.add(row);
    // Set the keyboard to the markup
    keyboardMarkup.setKeyboard(keyboard);
    // Add it to the message
    message.setReplyMarkup(keyboardMarkup);
    try {
		execute(sendMessage);
	} catch (TelegramApiException e) {
		
		e.printStackTrace();
	}
	*/

	public static void main(String[] args) {
		try {
			DATA = new DataBase();
		
			Bot bot = new Bot();
			querySend = new QuerryMessageSender(bot);
			
		
		
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(bot);
			ADMINISTRATOR = new Administrator();
			querySend.start();
			ADMINISTRATOR.start();
			
		
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
