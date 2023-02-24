package service;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import telegrambot.Bot;

public class QuerryMessageSender extends Thread {
	
	private static Queue<SendMessage> sendQueue = new ConcurrentLinkedQueue<>();
	
	private Bot bot;
	
	public QuerryMessageSender(Bot bot) {
		super();
		this.bot = bot;
	}
	
	public static void addMessage(SendMessage sendMessage) {
		sendQueue.add(sendMessage);
	}
	
	public void run() {
		try {
			while(true) {
				if(!sendQueue.isEmpty()) {
					SendMessage sendMessage = sendQueue.poll();
					bot.execute(sendMessage);
					Thread.sleep(300);
						
				}else {
					Thread.sleep(300);
				}
			}
		}catch (TelegramApiException e) {
			
			e.printStackTrace();
		}catch (InterruptedException e) {
	
			e.printStackTrace();
		}	
		
	}
}
