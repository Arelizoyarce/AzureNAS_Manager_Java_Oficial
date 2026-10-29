package com.azurenas.azure_nas_manager;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class AzureLocalClient implements AlmacenamientoClient {
    @Override
    public List<String> listarArchivos(String carpeta) {
        return Arrays.asList("Informe-mensual-08.docx", "Matriz-riesgos-Q3.xlsx", "Evidencias-ISO27001.pdf");
    }
}