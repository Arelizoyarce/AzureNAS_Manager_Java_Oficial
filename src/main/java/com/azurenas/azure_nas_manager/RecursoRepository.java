package com.azurenas.azure_nas_manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {
    List<Recurso> findByNombreContainingIgnoreCaseOrTipoContainingIgnoreCase(String nombre, String tipo);
}