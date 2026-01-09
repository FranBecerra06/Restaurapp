package DTO;

public class MesaDTO {

	private int idMesa, capacidad;
	private String disponibilidad;


	public MesaDTO(int idMesa) {
		this.idMesa = idMesa;
	}


	public MesaDTO(int capacidad, String disponibilidad) {
		super();
		this.capacidad = capacidad;
		this.disponibilidad = disponibilidad;
	}



	public int getIdMesa() {
		return idMesa;
	}

	public void setIdMesa(int idMesa) {
		this.idMesa = idMesa;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public String getDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(String disponibilidad) {
		this.disponibilidad = disponibilidad;
	}




}