package DTO;

import java.util.ArrayList;
import java.util.List;

public class PlatoDTO {

    private int idPlato;
    private int idCategoria;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private double precio;
	private String imgUrl;
	private List<String> alergenos;



	public PlatoDTO(int idPlato) {
        this.idPlato = idPlato;
    }
    
    
    public PlatoDTO(int idPlato, int idCategoria, String nombre, String descripcion, double precio, String imgUrl) {
        this.idPlato = idPlato;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
		this.imgUrl = imgUrl;
    }

	public PlatoDTO(int idPlato, int idCategoria, String nombre, String descripcion, double precio) {
		this.idPlato = idPlato;
		this.idCategoria = idCategoria;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
	}

	public PlatoDTO(int idPlato, int idCategoria, String nombre, String descripcion, int cantidad, double precio, String imgUrl) {
		this.idPlato = idPlato;
		this.idCategoria = idCategoria;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.precio = precio;
		this.imgUrl = imgUrl;
	}

	public PlatoDTO(int idPlato, int idCategoria, String nombre, String descripcion, int cantidad, double precio) {
		this.idPlato = idPlato;
		this.idCategoria = idCategoria;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.precio = precio;
		this.imgUrl = null;
	}

	public PlatoDTO(int idCategoria, String nombre, String descripcion, double precio, String imgUrl) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
		this.imgUrl = imgUrl;
    }
	public PlatoDTO(int idCategoria, String nombre, String descripcion, double precio) {
		this.idCategoria = idCategoria;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.imgUrl = null;
	}
    
    
    public PlatoDTO(String nombre, int cantidad, double precio) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

	public PlatoDTO(int idPlato, int idCategoria, String nombre, String descripcion, int cantidad, double precio, String imgUrl, List<String> alergenos) {
		this.idPlato = idPlato;
		this.idCategoria = idCategoria;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.precio = precio;
		this.imgUrl = imgUrl;
		this.alergenos = alergenos;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getIdPlato() {
		return idPlato;
	}
	
	public void setIdPlato(int idPlato) {
		this.idPlato = idPlato;
	}
	
	public int getIdCategoria() {
		return idCategoria;
	}
	
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
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

	public List<String> getAlergenos() { return alergenos; }
	public void setAlergenos(List<String> alergenos) { this.alergenos = alergenos; }

	public boolean tieneAlergeno(String alergeno) {
		return alergenos.contains(alergeno);
	}

	public boolean tieneAlergenos() {
		return !alergenos.isEmpty();
	}
	
}
