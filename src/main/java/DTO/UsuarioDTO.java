package DTO;

public class UsuarioDTO {

	private int idUsuario;
	private String nombre, apellidos, email, contrasena, telefono, rol, nombre_usuario;


	public UsuarioDTO() {
	}

	public UsuarioDTO(int idUsuario) {
		this.idUsuario = idUsuario;
	}


	public UsuarioDTO(String nombre, String apellidos, String email, String contrasena, String telefono, String nombre_usuario) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.contrasena = contrasena;
		this.telefono = telefono;
		this.rol = "Cliente";
		this.nombre_usuario = nombre_usuario;
	}

	public UsuarioDTO(String nombre, String apellidos, String email, String contrasena, String telefono, String rol, String nombre_usuario) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.contrasena = contrasena;
		this.telefono = telefono;
		this.rol = rol;
		this.nombre_usuario = nombre_usuario;
	}



	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
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

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getNombre_usuario() {
		return nombre_usuario;
	}

	public void setNombre_usuario(String nombre_usuario) {
		this.nombre_usuario = nombre_usuario;
	}


}