package DTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlatoDTO {

    private int idPlato;
    private int idCategoria;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private double precio;
    private String imgUrl;
    
    // Lista de 6 booleanos (Gluten, Lactosa, Huevo, FrutosSecos, Pescado, Marisco)
    private List<Boolean> listaAlergenos; 

    public PlatoDTO() {
        // Inicializamos con 6 falsos por defecto para evitar errores
        this.listaAlergenos = new ArrayList<>(Collections.nCopies(6, false));
    }
    
    public PlatoDTO(int idPlato) {
        this.idPlato = idPlato;
        this.listaAlergenos = new ArrayList<>(Collections.nCopies(6, false));
    }
    
    // Constructor completo para listar
    public PlatoDTO(int idPlato, int idCategoria, String nombre, String descripcion, double precio, String imgUrl) {
        this.idPlato = idPlato;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imgUrl = imgUrl;
        this.listaAlergenos = new ArrayList<>(Collections.nCopies(6, false));
    }

    public PlatoDTO(int idPlato, int idCategoria, String nombre, String descripcion, double precio) {
        this.idPlato = idPlato;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.listaAlergenos = new ArrayList<>(Collections.nCopies(6, false));
    }

    public PlatoDTO(int idPlato, int idCategoria, String nombre, String descripcion, int cantidad, double precio, String imgUrl) {
        this.idPlato = idPlato;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
        this.imgUrl = imgUrl;
        this.listaAlergenos = new ArrayList<>(Collections.nCopies(6, false));
    }

    public PlatoDTO(int idPlato, int idCategoria, String nombre, String descripcion, int cantidad, double precio) {
        this.idPlato = idPlato;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
        this.imgUrl = null;
        this.listaAlergenos = new ArrayList<>(Collections.nCopies(6, false));
    }

    // Constructor usado para CREAR UN NUEVO PLATO desde la interfaz
    public PlatoDTO(int idCategoria, String nombre, String descripcion, double precio, String imgUrl, List<Boolean> listaAlergenos) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imgUrl = imgUrl;
        this.listaAlergenos = listaAlergenos;
    }
    
    // Constructor simplificado para crear sin imagen
    public PlatoDTO(int idCategoria, String nombre, String descripcion, double precio, List<Boolean> listaAlergenos) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imgUrl = null;
        this.listaAlergenos = listaAlergenos;
    }
    
    public PlatoDTO(String nombre, int cantidad, double precio) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.listaAlergenos = new ArrayList<>(Collections.nCopies(6, false));
    }

    // Getters y Setters
    public List<Boolean> getListaAlergenos() {
        return listaAlergenos;
    }

    public void setListaAlergenos(List<Boolean> listaAlergenos) {
        this.listaAlergenos = listaAlergenos;
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
}