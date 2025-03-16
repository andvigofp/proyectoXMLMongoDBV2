package org.example.Modelo;

public class UsuarioGasto {
    private final int usuarioId;
    private final double totalGastado;

    public UsuarioGasto(int usuarioId, double totalGastado) {
        this.usuarioId = usuarioId;
        this.totalGastado = totalGastado;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public double getTotalGastado() {
        return totalGastado;
    }

    @Override
    public String toString() {
        return "UsuarioGasto{" +
                "usuarioId=" + usuarioId +
                ", totalGastado=" + totalGastado +
                '}';
    }
}

