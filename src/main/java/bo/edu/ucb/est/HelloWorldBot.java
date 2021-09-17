/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucb.est;

import java.util.List;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author ecampohermoso
 */
public class HelloWorldBot extends TelegramLongPollingBot {

	@Override
	public String getBotToken() {
		return "1950111729:AAHr4VqoCbKDT5qbWfKQHneZbo1kilQ3gh0";
	}

	@Override
	public void onUpdateReceived(Update update) {
		SendMessage message = new SendMessage();
		message.setChatId(update.getMessage().getChatId().toString());
		try {
			if (update.hasMessage()) { // Verificamos que tenga mensaje
				message.setText(mensajeInicial());

				execute(message); // Envia el mensaje con el mensaje inicial

			}
			if (update.getMessage().getText().toString().equals("1")) {
				message.setText("Ingrese el 1er numero");
				execute(message);
				int primer=0;
				int segundo=0;
				primer=Integer.parseInt(update.getMessage().getText().toString());
				message.setText("Ingrese el 2do numero");
				execute(message);
				segundo=Integer.parseInt(update.getMessage().getText().toString());
				message.setText("La suma es = "+(primer+segundo));
				execute(message);

			} else if (update.getMessage().getText().toString().equals("2")) {
				message.setText("funcionalidad no Implementada vuelva otro dia\n");
				execute(message);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public String mensajeInicial() {
		String mensaje;
		mensaje = "Bienvenido al bot Calculadora\n Seleccione una de las siguientes opciones:"
				+ "\n1. Sumar dos numeros\n2.Calcular serie fibonacci\n";

		return mensaje;
	}

	@Override
	public String getBotUsername() {
		return "UCB_Yo_Roberto";
	}

}
