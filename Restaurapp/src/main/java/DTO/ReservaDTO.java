package DTO;

import java.time.LocalDateTime;

import Modelos.Cliente;
import Modelos.Mesa;

public class ReservaDTO {
	
	private int idReserva;
	private Cliente idCliente;
	private Mesa idMesa;
	private LocalDateTime fechaHora;
	private int numeroComensales;
	private String estado;
	
	
	public ReservaDTO(int idReserva) {
		this.idReserva = idReserva;
	}
	
	
	public ReservaDTO(Cliente idCliente, Mesa idMesa, LocalDateTime fechaHora, int numeroComensales, String estado) {
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
	
	public Cliente getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(Cliente idCliente) {
		this.idCliente = idCliente;
	}
	
	public Mesa getIdMesa() {
		return idMesa;
	}
	
	public void setIdMesa(Mesa idMesa) {
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