package com.azurenas.azure_nas_manager;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recurso")
public class Recurso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_recurso;
    private String nombre;
    private String tipo;
    private String descripcion;
    private LocalDateTime fecha_registro;
    private String estado;

    public Long getId_recurso() { return id_recurso; }
    public void setId_recurso(Long id_recurso) { this.id_recurso = id_recurso; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFecha_registro() { return fecha_registro; }
    public void setFecha_registro(LocalDateTime fecha_registro) { this.fecha_registro = fecha_registro; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}