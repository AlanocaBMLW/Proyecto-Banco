/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucb.est;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author ecampohermoso
 */
public class HelloWorldBot extends TelegramLongPollingBot {
	boolean flag = true;
	boolean flag1 = true;
	int primer = 0;
	int segundo = 0;

	@Override
	public String getBotToken() {
		return "1950111729:AAHr4VqoCbKDT5qbWfKQHneZbo1kilQ3gh0";
	}

	@Override
	public void onUpdateReceived(Update update) {
		SendMessage message = new SendMessage();
		message.setChatId(update.getMessage().getChatId().toString());

		try {
			if (update.getMessage().getText().equals("Hola") && update.hasMessage()) { // Verificamos que tenga
																						// mensaje y sea /start
				message.setText(mensajeInicial());

				execute(message); // Envia el mensaje con el mensaje inicial

			} else if (update.getMessage().getText().toString().equals("1") && flag && flag1) {// true entonces entra
																								// false y
				// recibe numeros

				flag = false; // sera false cuando entre al 1er mensaje
				message.setText("Ingrese el 1er numero");
				execute(message);
			} else if (isNumeric(update.getMessage().getText().toString()) && !flag) {

				primer = Integer.parseInt(update.getMessage().getText().toString());
				message.setText("Ingrese el 2do numero");
				execute(message);
				flag = true;
				flag1 = false;
			} else if (isNumeric(update.getMessage().getText().toString()) && !flag1 && flag) {// entra a este 2do flag
																								// para agarrar el 2do
																								// numero.

				segundo = Integer.parseInt(update.getMessage().getText().toString());
				message.setText("La suma es = " + (primer + segundo));
				execute(message);
				flag1 = true;
			} else if (update.getMessage().getText().toString().equals("2") && flag && flag1) {
				message.setText("funcionalidad no Implementada vuelva otro dia\n");
				execute(message);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public boolean isNumeric(String num) {
		return num.matches("-?\\d+(\\.\\d+)?"); // numero entre 0 y 9
	}

	public String mensajeInicial() {
		String mensaje;
		mensaje = "Bienvenido al bot Calculadora\n Seleccione una de las siguientes opciones:"
				+ "\n 1. Sumar dos numeros\n2.Calcular serie fibonacci\n";

		return mensaje;
	}

	@Override
	public String getBotUsername() {
		return "UCB_Yo_Roberto";
	}

}
