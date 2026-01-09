package DTO;

import java.time.LocalDateTime;

public class ReservaDTO {

	private int idReserva;
	private ClienteDTO idCliente;
	private MesaDTO idMesa;
	private LocalDateTime fechaHora;
	private int numeroComensales;
	private String estado;


	public ReservaDTO(int idReserva) {
		this.idReserva = idReserva;
	}


	public ReservaDTO(ClienteDTO idCliente, MesaDTO idMesa, LocalDateTime fechaHora, int numeroComensales, String estado) {
		this.idCliente = idCliente;
		this.idMesa = idMesa;
		this.fechaHora = fechaHora;
		this.numeroComensales = numeroComensales;
		this.estado = estado;
	}



	public int getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(int idReserva) {
		this.idReserva = idReserva;
	}

	public ClienteDTO getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(ClienteDTO idCliente) {
		this.idCliente = idCliente;
	}

	public MesaDTO getIdMesa() {
		return idMesa;
	}

	public void setIdMesa(MesaDTO idMesa) {
		this.idMesa = idMesa;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public int getNumeroComensales() {
		return numeroComensales;
	}

	public void setNumeroComensales(int numeroComensales) {
		this.numeroComensales = numeroComensales;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}



}