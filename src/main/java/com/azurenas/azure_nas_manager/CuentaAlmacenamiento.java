package com.azurenas.azure_nas_manager;

import jakarta.persistence.*;

@Entity
@Table(name = "cuenta_almacenamiento")
public class CuentaAlmacenamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String region;
    private String tipo_credencial;
    private String credencial_cifrada;
    private Long espacio_asignado_bytes;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getTipo_credencial() { return tipo_credencial; }
    public void setTipo_credencial(String tipo_credencial) { this.tipo_credencial = tipo_credencial; }
    public String getCredencial_cifrada() { return credencial_cifrada; }
    public void setCredencial_cifrada(String credencial_cifrada) { this.credencial_cifrada = credencial_cifrada; }
    public Long getEspacio_asignado_bytes() { return espacio_asignado_bytes; }
    public void setEspacio_asignado_bytes(Long espacio_asignado_bytes) { this.espacio_asignado_bytes = espacio_asignado_bytes; }
}