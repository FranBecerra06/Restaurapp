package DTO;

public class Mesa_PlatoDTO {
	
	private int id_mesa, id_plato;
	private int cantidad;
	
	
	public Mesa_PlatoDTO(int id_mesa) {
		this.id_mesa = id_mesa;
	}
	
	
	public Mesa_PlatoDTO(int id_mesa, int id_plato, int cantidad) {
		this.id_mesa = id_mesa;
		this.id_plato = id_plato;
		this.cantidad = cantidad;
	}
	
	
	public int getId_mesa() {
		return id_mesa;
	}
	
	public void setId_mesa(int id_mesa) {
		this.id_mesa = id_mesa;
	}
	
	public int getId_plato() {
		return id_plato;
	}
	
	public void setId_plato(int id_plato) {
		this.id_plato = id_plato;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
}