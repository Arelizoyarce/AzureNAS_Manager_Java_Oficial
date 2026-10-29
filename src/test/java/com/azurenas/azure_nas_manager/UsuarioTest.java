package com.azurenas.azure_nas_manager;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    public void testEncriptacionPassword() {
        // Creamos un encriptador (el mismo que usa Spring Security en tu sistema)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Encriptamos una contraseña de prueba
        String passwordPlana = "123456";
        String hash = encoder.encode(passwordPlana);
        
        // Verificamos que el hash NO sea igual a la contraseña plana
        assertNotEquals(passwordPlana, hash, "El hash no debe ser igual a la contraseña");
        
        // Verificamos que al comparar la contraseña plana con el hash, coincida
        assertTrue(encoder.matches(passwordPlana, hash), "La contraseña debe coincidir con el hash");
    }
}