package DTO;

public class Mesa_PlatoDTO {
	private int idMesaPlato;
	private int id_mesa, id_plato;
	private int cantidad;
	private double precio;
	
	
	
	public Mesa_PlatoDTO(int id_mesa) {
		this.id_mesa = id_mesa;
	}
	
	
	
	public Mesa_PlatoDTO(int idMesaPlato, int id_mesa, int id_plato, int cantidad, double precio) {
		super();
		this.idMesaPlato = idMesaPlato;
		this.id_mesa = id_mesa;
		this.id_plato = id_plato;
		this.cantidad = cantidad;
		this.precio = precio;
	}
	
	
	
	public Mesa_PlatoDTO(int id_mesa, int id_plato, int cantidad, double precio) {
		this.id_mesa = id_mesa;
		this.id_plato = id_plato;
		this.cantidad = cantidad;
		this.precio = precio;
	}
	
	
	
	public Mesa_PlatoDTO(int id_mesa, int id_plato, int cantidad) {
		this.id_mesa = id_mesa;
		this.id_plato = id_plato;
		this.cantidad = cantidad;
	}
	
	
	
	public int getIdMesaPlato() {
		return idMesaPlato;
	}
	
	public void setIdMesaPlato(int idMesaPlato) {
		this.idMesaPlato = idMesaPlato;
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
	
	public double getPrecio() {
		return precio;
	}
	
	public void setPrecio(double precio) {
		this.precio = precio;
	}	
}