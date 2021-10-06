package bo.edu.ucb.est;

public class Cuenta {
	private int numCuenta;
	private String moneda;
	private String tipo;
	private double saldo;

	public Cuenta(int numCuenta, String moneda, String tipo, double saldo) {
		this.numCuenta = numCuenta;
		this.moneda = moneda;
		this.tipo = tipo;
		this.saldo = saldo;
	}

	public int getNumCuenta() {
		return numCuenta;
	}

	public void setNumCuenta(int numCuenta) {
		this.numCuenta = numCuenta;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

}
