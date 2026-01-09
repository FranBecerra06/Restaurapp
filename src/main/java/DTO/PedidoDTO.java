package DTO;

import java.time.LocalDateTime;

public class PedidoDTO {

    private int idPedido;
    private int idCamarero;
    private MesaDTO idMesa;
    private LocalDateTime fecha;
    private double total;
    private String observaciones;
    
    
    public PedidoDTO() {
    }
    
    public PedidoDTO(int idPedido) {
        this.idPedido = idPedido;
    }
    
    
    public PedidoDTO(int idCamarero, MesaDTO idMesa, LocalDateTime fecha, double total, String observaciones) {
        this.idCamarero = idCamarero;
        this.idMesa = idMesa;
        this.fecha = fecha;
        this.total = total;
        this.observaciones = observaciones;
    }
    
    
    
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdCamarero() {
        return idCamarero;
    }

    public void setIdCamarero(int idCamarero) {
        this.idCamarero = idCamarero;
    }

    public MesaDTO getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(MesaDTO idMesa) {
        this.idMesa = idMesa;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    
    public String getCamarero() {
    	return "Camarero" + idCamarero;
    }
}