package DTO;

public class Pedido_PlatoDTO {
	
	private int id_pedido, id_plato;
	private int cantidad;
	
	
	public Pedido_PlatoDTO(int id_pedido, int id_plato, int cantidad) {
		this.id_pedido = id_pedido;
		this.id_plato = id_plato;
		this.cantidad = cantidad;
	}
	
	
	public int getId_pedido() {
		return id_pedido;
	}
	
	public void setId_pedido(int id_pedido) {
		this.id_pedido = id_pedido;
	}
	
	public int getId_plato() {
		return id_plato;
	}
	
	public void setId_platp(int id_plato) {
		this.id_plato = id_plato;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
}