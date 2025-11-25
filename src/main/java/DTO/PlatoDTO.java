package DTO;

public class PlatoDTO {
	
	private int idPlato;
	private String nombre, descripcion;
	private double precio;
	private boolean disponible;
	
	
	public PlatoDTO(int idPlato) {
		this.idPlato = idPlato;
	}
	
	
	public PlatoDTO(String nombre, String descripcion, double precio, boolean disponible) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.disponible = disponible;
	}
	
	
	
	public int getIdPlato() {
		return idPlato;
	}
	
	public void setIdPlato(int idPlato) {
		this.idPlato = idPlato;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public double getPrecio() {
		return precio;
	}
	
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	public boolean isDisponible() {
		return disponible;
	}
	
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	
	
	
}