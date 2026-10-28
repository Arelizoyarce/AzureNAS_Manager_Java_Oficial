package com.azurenas.azure_nas_manager;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final RecursoRepository recursoRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository; // <-- Agregado
    private final Path rootLocation = Paths.get("uploads");

    public HomeController(RecursoRepository recursoRepository, AuditoriaRepository auditoriaRepository, UsuarioRepository usuarioRepository) {
        this.recursoRepository = recursoRepository;
        this.auditoriaRepository = auditoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private void log(String mod, String acc, String desc) {
        Auditoria a = new Auditoria();
        a.setModulo(mod); a.setAccion(acc); a.setDescripcion(desc);
        a.setIp_usuario("127.0.0.1"); a.setFecha(LocalDateTime.now());
        auditoriaRepository.save(a);
    }

    @GetMapping("/") public String inicio() { return "index"; }
    @GetMapping("/login") public String login() { return "login"; }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Recurso> recursos = recursoRepository.findAll();
        model.addAttribute("recursos", recursos);
        model.addAttribute("totalFileShares", recursos.stream().filter(r -> "FILE_SHARE".equals(r.getTipo())).count());
        model.addAttribute("totalContainers", recursos.stream().filter(r -> "CONTAINER".equals(r.getTipo())).count());
        model.addAttribute("operacionesHoy", auditoriaRepository.count());
        model.addAttribute("espacioUsado", "4.2 TB");
        return "dashboard";
    }

    @GetMapping("/explorador")
    public String explorador(@RequestParam(value = "recurso", required = false, defaultValue = "fs-seguridad-info") String recurso, Model model, Authentication auth) {
        String username = auth.getName();
        log("Explorador", "ABRIR", "Abrió: " + recurso + " como " + username);
        
        List<String> archivos = new ArrayList<>();
        archivos.add("Informe-mensual-08.docx");
        archivos.add("Matriz-riesgos-Q3.xlsx");
        
        Path userDir = rootLocation.resolve(username);
        try { if (Files.exists(userDir)) Files.list(userDir).forEach(p -> archivos.add(p.getFileName().toString())); } catch (IOException e) { }

        model.addAttribute("carpetaActual", recurso + " / 2026 / Informes");
        model.addAttribute("nombreRecurso", recurso);
        model.addAttribute("archivos", archivos);
        // ESTO TRAE LOS USUARIOS DE MYSQL PARA EL DROPDOWN
        model.addAttribute("usuarios", usuarioRepository.findAll()); 
        return "explorador";
    }

    @PostMapping("/transferir-archivo")
    public String transferirArchivo(@RequestParam String archivo, @RequestParam String destino, RedirectAttributes ra) {
        // Aquí se registra la transferencia en la auditoría
        log("Explorador", "TRANSFERIR", "Transfirió " + archivo + " al correo " + destino);
        ra.addFlashAttribute("mensajeExito", "¡Archivo " + archivo + " transferido exitosamente a " + destino + "!");
        return "redirect:/explorador"; // ESTO ARREGLA EL 404
    }

    @GetMapping("/busqueda")
    public String busqueda(@RequestParam(value = "q", required = false) String q, Model model) {
        if (q != null && !q.isEmpty()) {
            model.addAttribute("resultados", recursoRepository.findByNombreContainingIgnoreCaseOrTipoContainingIgnoreCase(q, q));
            log("Búsqueda", "FILTRAR", "Buscó: " + q);
        } else {
            model.addAttribute("resultados", recursoRepository.findAll());
        }
        model.addAttribute("query", q);
        return "busqueda"; // ESTO ARREGLA EL 404
    }

    @GetMapping("/transferencias")
    public String transferencias(Model model, Authentication auth) {
        String username = auth.getName();
        List<String> subidos = new ArrayList<>();
        Path userDir = rootLocation.resolve(username);
        try { if (Files.exists(userDir)) Files.list(userDir).forEach(p -> subidos.add(p.getFileName().toString())); } catch (IOException e) {}
        model.addAttribute("archivosSubidos", subidos);
        return "transferencias";
    }

    @PostMapping("/subir-archivo-real")
    public String subir(@RequestParam("archivo") MultipartFile archivo, RedirectAttributes ra, Authentication auth) {
        String username = auth.getName();
        if (!archivo.isEmpty()) {
            try {
                Path userDir = rootLocation.resolve(username);
                Files.createDirectories(userDir);
                Files.copy(archivo.getInputStream(), userDir.resolve(archivo.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                log("Transferencias", "SUBIR", "Subió: " + archivo.getOriginalFilename() + " como " + username);
                ra.addFlashAttribute("mensajeExito", "¡Archivo " + archivo.getOriginalFilename() + " subido exitosamente!");
            } catch (IOException e) { ra.addFlashAttribute("mensajeError", "Error: " + e.getMessage()); }
        }
        return "redirect:/transferencias";
    }

    @GetMapping("/descargar-archivo")
    public void descargar(@RequestParam("nombre") String nombre, HttpServletResponse response, Authentication auth) {
        String username = auth.getName();
        try {
            Path userDir = rootLocation.resolve(username);
            Path file = userDir.resolve(nombre);
            if (!Files.exists(file)) {
                Files.createDirectories(userDir);
                Files.writeString(file, "Archivo de ejemplo de AzureNAS.");
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + nombre + "\"");
            Files.copy(file, response.getOutputStream());
            response.flushBuffer();
            log("Explorador", "DESCARGAR", "Descargó: " + nombre + " como " + username);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @GetMapping("/auditoria")
    public String auditoria(Model model) {
        model.addAttribute("logs", auditoriaRepository.findAllByOrderByFechaDesc());
        return "auditoria";
    }
}