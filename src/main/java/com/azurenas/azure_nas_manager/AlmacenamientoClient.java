package com.azurenas.azure_nas_manager;
import java.util.List;
public interface AlmacenamientoClient {
    List<String> listarArchivos(String carpeta);
}