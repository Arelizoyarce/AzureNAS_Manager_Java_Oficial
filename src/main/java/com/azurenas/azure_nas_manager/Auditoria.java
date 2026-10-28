package com.azurenas.azure_nas_manager;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_log;
    private String modulo;
    private String accion;
    private String descripcion;
    private String ip_usuario;
    private LocalDateTime fecha;
    private Long usuario_id;
    private String hash_integridad;

    // Getters y Setters
    public Long getId_log() { return id_log; }
    public void setId_log(Long id_log) { this.id_log = id_log; }
    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getIp_usuario() { return ip_usuario; }
    public void setIp_usuario(String ip_usuario) { this.ip_usuario = ip_usuario; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Long getUsuario_id() { return usuario_id; }
    public void setUsuario_id(Long usuario_id) { this.usuario_id = usuario_id; }
    public String getHash_integridad() { return hash_integridad; }
    public void setHash_integridad(String hash_integridad) { this.hash_integridad = hash_integridad; }
}