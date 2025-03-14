package org.example.Modelo;

public class Videojuego {
    private int id;
    private String titulo;
    private String descripcion;
    private double precio;
    private int disponibilidad;
    private String genero;
    private String desarrollador;
    private int edadMinimaRecomendada;
    private String plataforma;

    // Constructor
    public Videojuego(int id, String titulo, String descripcion, double precio, int disponibilidad, String genero,
                      String desarrollador, int edadMinimaRecomendada, String plataforma) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponibilidad = disponibilidad;
        this.genero = genero;
        this.desarrollador = desarrollador;
        this.edadMinimaRecomendada = edadMinimaRecomendada;
        this.plataforma = plataforma;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(String desarrollador) {
        this.desarrollador = desarrollador;
    }

    public int getEdadMinimaRecomendada() {
        return edadMinimaRecomendada;
    }

    public void setEdadMinimaRecomendada(int edadMinimaRecomendada) {
        this.edadMinimaRecomendada = edadMinimaRecomendada;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    @Override
    public String toString() {
        return "<videojuego>\n" +
                "    <id>" + id + "</id>\n" +
                "    <titulo>" + titulo + "</titulo>\n" +
                "    <descripcion>" + descripcion + "</descripcion>\n" +
                "    <precio>" + precio + "</precio>\n" +
                "    <disponibilidad>" + disponibilidad + "</disponibilidad>\n" +
                "    <genero>" + genero + "</genero>\n" +
                "    <desarrollador>" + desarrollador + "</desarrollador>\n" +
                "    <edad_minima_recomendada>" + edadMinimaRecomendada + "</edad_minima_recomendada>\n" +
                "    <plataforma>" + plataforma + "</plataforma>\n" +
                "</videojuego>";
    }
}

