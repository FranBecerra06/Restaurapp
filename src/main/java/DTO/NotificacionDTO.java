package DTO;

import java.util.Map;

public class NotificacionDTO {
    private Map<PlatoDTO, Integer> pedido;
    private int mesa;

    public NotificacionDTO(Map<PlatoDTO, Integer> pedido, int mesa){
        this.pedido = pedido;
        this.mesa = mesa;
    }

    public Map<PlatoDTO, Integer> getPedido() {
        return pedido;
    }

    public void setPedido(Map<PlatoDTO, Integer> pedido) {
        this.pedido = pedido;
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    @Override
    public String toString() {
        return "NotificacionDTO{" +
                "pedido=" + pedido.toString() +
                ", mesa=" + mesa +
                '}';
    }
}
