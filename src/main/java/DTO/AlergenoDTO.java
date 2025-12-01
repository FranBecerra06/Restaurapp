package DTO;

public class AlergenoDTO {
	
	private int id;
	private String nombre;
	
	
	public AlergenoDTO(int id) {
		this.id = id;
	}
	
	
	public AlergenoDTO(String nombre) {
		this.nombre = nombre;
	}
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
}