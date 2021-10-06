package bo.edu.ucb.est;

import java.util.ArrayList;

public class Usuario {
	private String nombre;
	private int pin;
	private ArrayList<Cuenta> Cuentas = new ArrayList<Cuenta>();
	
	
	public Usuario(String nombre, int pin) {
		this.nombre = nombre;
		this.pin = pin;
	}
	
	
	public ArrayList<Cuenta> getCuentas() {
		return Cuentas;
	}


	public Usuario() {
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}

}
