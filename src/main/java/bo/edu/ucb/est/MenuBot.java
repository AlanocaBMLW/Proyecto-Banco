/*
 * To change this license header, choose License Headers in Project Properties. 
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucb.est;

import java.util.ArrayList;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MenuBot extends TelegramLongPollingBot {
	int estado = 0;
	// estado= 0 inicio del programa
	// estado= 1 Registro usuario
	// estado=11 Registro PIN
	// estado= 2 Crea cuenta para el usuario
	// estado= 3 Menu
	int decision = 0;
	boolean flag = false;
	String nombre;
	int PIN;
	ArrayList<Usuario> Usuarios = new ArrayList<Usuario>();
	Usuario actual = null;
	Cuenta cnt = null;

	@Override
	public void onUpdateReceived(Update update) {
		if (!flag) {// verifica que los usuarios si estan creados
			aniadirUsuarios();
			flag = true;
		} else {// flag=true entonces los usuarios estan creados
			SendMessage message = new SendMessage();
			message.setChatId(update.getMessage().getChatId().toString());

			try {
				// Verificamos que sea /start
				if (update.getMessage().getText().equals("/start") || estado == 0) {
					verificarUsuario(update.getMessage().getFrom().getFirstName());// cambia el estado a 1 o 2
					if (estado == 1) {// no conoce al usuario
						message.setText("Bienvenido al Banco de la Fortuna.\n He notado que aun no eres cliente "
								+ "procedamos a registrarte.\nCual es tu nombre?");
						execute(message);
					} else if (estado == 2) {// si conoce al usuario
						nombre = update.getMessage().getText();
						message.setText("Hola de nuevo " + update.getMessage().getFrom().getFirstName() + " "
								+ update.getMessage().getFrom().getLastName()
								+ "\n Solo por seguridad. Cual es tu PIN?");
						execute(message);
					}

				} else if (estado == 1) {// creacion de usuario
					nombre = update.getMessage().getText();
					message.setText("Por favor elige un PIN de seguridad");
					execute(message);
					estado = 11;
				} else if (estado == 11) {// verifica PIN (registro) entra aca cuando pregunta nuevo pin
					if (isNumeric(update.getMessage().getText())) {// el texto es un numero
						PIN = Integer.parseInt(update.getMessage().getText());
						registrarUsuario(nombre, PIN);
						message.setText("Te hemos registrado correctamente"
								+ "\nBienvenido\nElige una opcion\n\n1)Ver saldo\n2)Retirar dinero"
								+ "\n3)Depositar dinero\n4)Crear Cuenta\n5)Salir ");
						execute(message);
						estado = 3;
					} else {// el texto no es un numero
						message.setText("Ingrese solo digitos del 0 al 9\nPor favor elige un PIN de seguridad");
						execute(message);
					}

				} else if (estado == 2) {// verificacion PIN(usuario existente)
					if (isNumeric(update.getMessage().getText())) {// el texto es un numero
						verificarPIN();
						message.setText("Bienvenido\nElige una opcion\n\n1)Ver saldo\n2)Retirar dinero"
								+ "\n3)Depositar dinero\n4)Crear Cuenta\n5)Salir ");
						execute(message);
						estado = 3;
					} else {// el texto no es un numero
						message.setText("Ingrese solo digitos del 0 al 9");
						execute(message);
					}
				} else if (estado == 3) {// Este se encarga de mover la decision y el estado MENU
					if (isNumeric(update.getMessage().getText())) {
						decision = Integer.parseInt(update.getMessage().getText());
						if (decision == 1) {//este se encarga de ver el saldo
							if (actual.getCuentas().isEmpty()) {
								estado = 4;
								message.setText("Seleccione la moneda\n1)Dolares\n2)Bolivianos");
								execute(message);
							} else {
								message.setText("Que cuenta desea ver la informacion?\n");
								for (int i = 0; i < actual.getCuentas().size(); i++) {
									message.setText(message.getText() + i + ")\n o \n");
								}
								execute(message);
								estado = 33;
							}
						} else if (decision == 2) {
							message.setText(
									retirarDinero() + "Bienvenido\nElige una opcion\n\n1)Ver saldo\n2)Retirar dinero"
											+ "\n3)Depositar dinero\n4)Crear Cuenta\n5)Salir");
							execute(message);
						} else if (decision == 3) {
							message.setText(
									depositarDinero() + "Bienvenido\nElige una opcion\n\n1)Ver saldo\n2)Retirar dinero"
											+ "\n3)Depositar dinero\n4)Crear Cuenta\n5)Salir");
							execute(message);
						} else if (decision == 5) {
							estado=0;
						}

					} else {
						message.setText("Ingrese solo digitos entre 0 y 9");
						execute(message);
					}
				} else if (estado == 33) {
					String mensaje = "";
					int numCuenta;
					String moneda;
					String tipo;
					double saldo;
					if (isNumeric(update.getMessage().getText())) {
						decision = Integer.parseInt(update.getMessage().getText());//el numero de la cuenta
						for (int i = 0; i < actual.getCuentas().size(); i++) {
							if(decision==i+1) {
								numCuenta = actual.getCuentas().get(i).getNumCuenta();
								moneda = actual.getCuentas().get(i).getMoneda();
								tipo = actual.getCuentas().get(i).getTipo();
								saldo = actual.getCuentas().get(i).getSaldo();
								mensaje = numCuenta+"\n"+moneda+"\n"+tipo+"\n"+saldo+"\n";
							}
						}
						message.setText(mensaje+"Bienvenido\nElige una opcion\n\n1)Ver saldo\n2)Retirar dinero"
								+ "\n3)Depositar dinero\n4)Crear Cuenta\n5)Salir");
						execute(message);
						estado=3;
					}else {
						message.setText("Ingrese solo digitos entre 0 y 9");
						execute(message);
					}
				} else if (estado == 4) {// creacion de cuenta
					if (isNumeric(update.getMessage().getText())) {
						decision = Integer.parseInt(update.getMessage().getText());
						if (decision == 1) {
							cnt.setMoneda("Dolares");
							cnt.setNumCuenta(100001);
							cnt.setTipo("ahorro");// por defecto siempre sera ahorro en la creacion
							cnt.setSaldo(0);
						} else if (decision == 2) {
							cnt.setMoneda("Bolivianos");
							cnt.setNumCuenta(100001);
							cnt.setTipo("ahorro");// por defecto siempre sera ahorro en la creacion
							cnt.setSaldo(0);
						}
						actual.getCuentas().add(cnt);
						message.setText(
								"Se creo la cuenta satisfactoriamente\nElige una opcion\n\n1)Ver saldo\n2)Retirar dinero"
										+ "\n3)Depositar dinero\n4)Crear Cuenta\n5)Salir");
						execute(message);
						estado = 3;// vuelve al menu
					} else {
						message.setText("Ingrese solo digitos entre 0 y 9");
						execute(message);
					}

				}
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	public String retirarDinero() {
		String mensaje = "";
		return mensaje;
	}

	private String depositarDinero() {
		String mensaje = "";
		return mensaje;
	}

	private String crearCuenta() {
		String mensaje = "";
		return mensaje;
	}

	public void verificarPIN() {
		for (int i = 0; i < Usuarios.size(); i++) {
			if (Usuarios.get(i).getPin() == PIN && Usuarios.get(i).getNombre().equalsIgnoreCase(nombre)) {
				actual = Usuarios.get(i);
				estado = 3;// sera 3 solamente si PIN es igual al usuario
				break;
			}
		}
	}

	public void registrarUsuario(String nombre2, int pIN2) {
		Usuario us = new Usuario(nombre2, pIN2);
		Usuarios.add(us);
	}

	public void verificarUsuario(String nomb) {
		estado = 1;// siempre sera 1 si no encuentra el usuario
		for (int i = 0; i < Usuarios.size(); i++) {
			if (Usuarios.get(i).getNombre().equalsIgnoreCase(nomb)) {
				estado = 2;// sera 2 solamente si esta el usuario
				break;
			}
		}
	}

	public void aniadirUsuarios() {// usuarios por defecto
		// primer usuario
		Usuario us1 = new Usuario("Juan Perez", 3333);
		Cuenta cnt1 = new Cuenta(111122, "Bolivianos", "Ahorros", 12000);
		us1.getCuentas().add(cnt1);
		cnt1 = new Cuenta(112211, "USD", "Corriente", 100);
		us1.getCuentas().add(cnt1);
		// segundo usuario
		Usuario us2 = new Usuario("Maria Gomez", 4444);
		cnt1 = new Cuenta(221122, "Bolivianos", "Ahorros", 0);
		us2.getCuentas().add(cnt1);
		// Tercer usuario
		Usuario us3 = new Usuario("Carlos Gomez", 3333);
		cnt1 = new Cuenta(331122, "Bolivianos", "Ahorros", 100);
		us3.getCuentas().add(cnt1);
		cnt1 = new Cuenta(332211, "USD", "Corriente", 1000);
		us3.getCuentas().add(cnt1);
		cnt1 = new Cuenta(332233, "Bolivianos", "Ahorros", 100000);
		us3.getCuentas().add(cnt1);
		cnt1 = null;
		Usuarios.add(us3);
		Usuarios.add(us2);
		Usuarios.add(us1);
	}

	public boolean isNumeric(String num) {
		return num.matches("-?\\d+(\\.\\d+)?"); // numero entre 0 y 9
	}

	@Override
	public String getBotToken() {
		return "2057991954:AAEYA-o6DjclzMp-da1wCoK8NKJHwGPKDEU";
	}

	@Override
	public String getBotUsername() {
		return "SimulacionBanco_bot";
	}

}
