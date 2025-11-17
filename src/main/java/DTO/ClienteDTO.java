package DTO;

import java.time.LocalDate;

import Modelos.Usuario;

public class ClienteDTO {
	
	private int idCliente;
	private UsuarioDTO idUsuario;
	private String nombre;
	private String apellidos;
	private String email;
	private LocalDate fechaNacimiento;
	
	
	public ClienteDTO(int idCliente) {
		this.idCliente = idCliente;
	}
	
	
	public ClienteDTO(String nombre, String apellidos, String email, LocalDate fechaNacimiento) {
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.fechaNacimiento = fechaNacimiento;
	}
	
	
	
	public int getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	public UsuarioDTO getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(UsuarioDTO idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellidos() {
		return apellidos;
	}
	
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	
	
	
}