package DTO;

import Modelos.Mesa;

public class PedidoDTO {
	
	private int idPedido;
	private MesaDTO idMesa;
	private String observaciones;
	
	
	public PedidoDTO(int idPedido) {
		this.idPedido = idPedido;
	}
	
	
	public PedidoDTO(MesaDTO idMesa, String observaciones) {
		super();
		this.idMesa = idMesa;
		this.observaciones = observaciones;
	}
	
	
	
	public int getIdPedido() {
		return idPedido;
	}
	
	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}
	
	public MesaDTO getIdMesa() {
		return idMesa;
	}
	
	public void setIdMesa(MesaDTO idMesa) {
		this.idMesa = idMesa;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
	
}