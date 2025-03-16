package org.example.Modelo;

public class CarritoCoste {
    private final int carritoId;
    private final int usuarioId;
    private final double costeTotal;

    public CarritoCoste(int carritoId, int usuarioId, double costeTotal) {
        this.carritoId = carritoId;
        this.usuarioId = usuarioId;
        this.costeTotal = costeTotal;
    }

    public int getCarritoId() {
        return carritoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public double getCosteTotal() {
        return costeTotal;
    }

    @Override
    public String toString() {
        return "CarritoCoste{" +
                "carritoId=" + carritoId +
                ", usuarioId=" + usuarioId +
                ", costeTotal=" + costeTotal +
                '}';
    }
}