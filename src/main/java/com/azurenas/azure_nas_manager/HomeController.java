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
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class HomeController {

    private final RecursoRepository recursoRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;
    private final Path rootLocation = Paths.get("uploads");

    public HomeController(RecursoRepository recursoRepository, AuditoriaRepository auditoriaRepository, UsuarioRepository usuarioRepository, GrupoRepository grupoRepository) {
        this.recursoRepository = recursoRepository;
        this.auditoriaRepository = auditoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
    }

    private void log(String mod, String acc, String desc, String username) {
        Auditoria a = new Auditoria();
        a.setModulo(mod); a.setAccion(acc); a.setDescripcion(desc);
        a.setIp_usuario("127.0.0.1"); a.setFecha(LocalDateTime.now());
        Usuario u = usuarioRepository.findByCorreo(username).orElse(null);
        a.setUsuario_id(u != null ? u.getId_usuario() : null);
        try {
            String dataToHash = mod + acc + desc + a.getFecha().toString();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dataToHash.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            a.setHash_integridad(hexString.toString());
        } catch (Exception e) { a.setHash_integridad("ERROR_HASH"); }
        auditoriaRepository.save(a);
    }

    @GetMapping("/") public String inicio() { return "index"; }
    @GetMapping("/login") public String login() { return "login"; }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        String username = auth.getName();
        Usuario usuarioLogueado = usuarioRepository.findByCorreo(username).orElse(null);
        
        List<Recurso> recursos = recursoRepository.findAll();
        long totalBytes = 0; long totalArchivos = 0;
        Path userDir = rootLocation.resolve(username);
        try {
            if (Files.exists(userDir)) {
                List<Path> files = Files.list(userDir).filter(Files::isRegularFile).toList();
                totalArchivos = files.size();
                totalBytes = files.stream().mapToLong(p -> p.toFile().length()).sum();
            }
        } catch (IOException e) {}

        double mb = (double) totalBytes / (1024 * 1024);
        
        model.addAttribute("usuarioLogueado", usuarioLogueado);
        model.addAttribute("usuarios", usuarioRepository.findAll()); 
        
        List<Grupo> misGrupos = new ArrayList<>();
        if (usuarioLogueado != null) {
            misGrupos = grupoRepository.findAll().stream()
                .filter(g -> (g.getMiembros() != null && g.getMiembros().contains(usuarioLogueado))
                        || (g.getCreador() != null && g.getCreador().equals(usuarioLogueado)))
                .toList();
        }
        model.addAttribute("misGrupos", misGrupos);

        Map<Long, List<String>> archivosPorGrupo = new HashMap<>();
        for (Grupo g : misGrupos) {
            List<String> files = new ArrayList<>();
            Path groupDir = rootLocation.resolve("grupo_" + g.getId_grupo());
            try {
                if (Files.exists(groupDir)) {
                    Files.list(groupDir).forEach(p -> files.add(p.getFileName().toString()));
                }
            } catch (IOException e) {}
            archivosPorGrupo.put(g.getId_grupo(), files);
        }
        model.addAttribute("archivosPorGrupo", archivosPorGrupo);
        
        model.addAttribute("recursos", recursos);
        model.addAttribute("totalFileShares", recursos.stream().filter(r -> "FILE_SHARE".equals(r.getTipo())).count());
        model.addAttribute("totalContainers", recursos.stream().filter(r -> "CONTAINER".equals(r.getTipo())).count());
        model.addAttribute("operacionesHoy", auditoriaRepository.count());
        model.addAttribute("espacioUsado", String.format("%.2f MB", mb));
        model.addAttribute("archivosSubidos", totalArchivos);
        
        return "dashboard";
    }

    @PostMapping("/crear-grupo-usuario")
    public String crearGrupoUsuario(@RequestParam String nombreGrupo, @RequestParam(required = false) List<Long> usuariosIds, Authentication auth) {
        String username = auth.getName();
        Usuario creador = usuarioRepository.findByCorreo(username).orElse(null);
        
        Grupo g = new Grupo();
        g.setNombre(nombreGrupo);
        g.setDescripcion("Grupo creado por " + username);
        g.setCreador(creador);
        
        Set<Usuario> miembros = new HashSet<>();
        if (creador != null) miembros.add(creador);
        if (usuariosIds != null) miembros.addAll(usuarioRepository.findAllById(usuariosIds));
        
        g.setMiembros(miembros);
        grupoRepository.save(g);
        log("Dashboard", "CREAR_GRUPO", "El usuario creó el grupo: " + nombreGrupo, username);
        return "redirect:/dashboard?grupoCreado";
    }

    @PostMapping("/grupo/agregar-miembro")
    public String agregarMiembro(@RequestParam Long grupoId, @RequestParam Long usuarioId, Authentication auth, RedirectAttributes ra) {
        Grupo g = grupoRepository.findById(grupoId).orElse(null);
        Usuario u = usuarioRepository.findById(usuarioId).orElse(null);
        Usuario current = usuarioRepository.findByCorreo(auth.getName()).orElse(null);

        if (g != null && u != null && current != null) {
            boolean isCreador = g.getCreador() != null && g.getCreador().getId_usuario().equals(current.getId_usuario());
            boolean isAdmin = current.getRol() != null && current.getRol().getNombre().equals("ADMINISTRADOR");
            
            if (isCreador || isAdmin) {
                g.getMiembros().add(u);
                grupoRepository.save(g);
                log("Dashboard", "AGREGAR_MIEMBRO", "Añadió a " + u.getNombres() + " al grupo " + g.getNombre(), auth.getName());
                ra.addFlashAttribute("mensajeExito", "Miembro añadido correctamente.");
            } else {
                ra.addFlashAttribute("mensajeError", "No tienes permiso para añadir miembros a este grupo.");
            }
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/grupo/quitar-miembro")
    public String quitarMiembro(@RequestParam Long grupoId, @RequestParam Long usuarioId, Authentication auth, RedirectAttributes ra) {
        Grupo g = grupoRepository.findById(grupoId).orElse(null);
        Usuario u = usuarioRepository.findById(usuarioId).orElse(null);
        Usuario current = usuarioRepository.findByCorreo(auth.getName()).orElse(null);

        if (g != null && u != null && current != null) {
            boolean isCreador = g.getCreador() != null && g.getCreador().getId_usuario().equals(current.getId_usuario());
            boolean isAdmin = current.getRol() != null && current.getRol().getNombre().equals("ADMINISTRADOR");
            
            if (isCreador || isAdmin) {
                g.getMiembros().remove(u);
                grupoRepository.save(g);
                log("Dashboard", "QUITAR_MIEMBRO", "Quitó a " + u.getNombres() + " del grupo " + g.getNombre(), auth.getName());
                ra.addFlashAttribute("mensajeExito", "Miembro eliminado correctamente.");
            } else {
                ra.addFlashAttribute("mensajeError", "No tienes permiso para quitar miembros de este grupo.");
            }
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/grupo/subir-archivo")
    public String subirArchivoGrupo(@RequestParam("archivo") MultipartFile archivo, @RequestParam("grupoId") Long grupoId, RedirectAttributes ra, Authentication auth) {
        if (!archivo.isEmpty()) {
            try {
                Path groupDir = rootLocation.resolve("grupo_" + grupoId);
                Files.createDirectories(groupDir);
                Files.copy(archivo.getInputStream(), groupDir.resolve(archivo.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                log("Grupo", "SUBIR", "Subió archivo al grupo ID " + grupoId, auth.getName());
                ra.addFlashAttribute("mensajeExito", "¡Archivo subido al grupo exitosamente!");
            } catch (IOException e) {
                ra.addFlashAttribute("mensajeError", "Error al subir: " + e.getMessage());
            }
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/grupo/descargar-archivo")
    public void descargarArchivoGrupo(@RequestParam("grupoId") Long grupoId, @RequestParam("nombre") String nombre, @RequestParam(value = "preview", required = false, defaultValue = "false") boolean preview, HttpServletResponse response, Authentication auth) {
        try {
            Path file = rootLocation.resolve("grupo_" + grupoId).resolve(nombre);
            if (Files.exists(file)) {
                response.setContentType("application/octet-stream");
                String disposition = preview ? "inline" : "attachment";
                response.setHeader("Content-Disposition", disposition + "; filename=\"" + nombre + "\"");
                Files.copy(file, response.getOutputStream());
                response.flushBuffer();
                log("Grupo", preview ? "VISTA_PREVIA" : "DESCARGAR", "Accedió a " + nombre, auth.getName());
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    @PostMapping("/grupo/eliminar-archivo")
    public String eliminarArchivoGrupo(@RequestParam("grupoId") Long grupoId, @RequestParam("nombre") String nombre, RedirectAttributes ra, Authentication auth) {
        try {
            Path file = rootLocation.resolve("grupo_" + grupoId).resolve(nombre);
            Files.deleteIfExists(file);
            log("Grupo", "ELIMINAR", "Eliminó " + nombre, auth.getName());
            ra.addFlashAttribute("mensajeExito", "¡Archivo eliminado del grupo!");
        } catch (IOException e) {
            ra.addFlashAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/explorador")
    public String explorador(@RequestParam(value = "recurso", required = false, defaultValue = "fs-seguridad-info") String recurso, Model model, Authentication auth) {
        String username = auth.getName();
        Usuario usuarioLogueado = usuarioRepository.findByCorreo(username).orElse(null);
        List<String> archivos = new ArrayList<>();
        
        Path userDir = rootLocation.resolve(username);
        try { if (Files.exists(userDir)) Files.list(userDir).forEach(p -> archivos.add(p.getFileName().toString())); } catch (IOException e) { }
        
        List<Grupo> misGrupos = new ArrayList<>();
        if (usuarioLogueado != null) {
            misGrupos = grupoRepository.findAll().stream()
                .filter(g -> (g.getMiembros() != null && g.getMiembros().contains(usuarioLogueado))
                        || (g.getCreador() != null && g.getCreador().equals(usuarioLogueado)))
                .toList();
            for (Grupo g : misGrupos) {
                Path groupDir = rootLocation.resolve("grupo_" + g.getId_grupo());
                try { 
                    if (Files.exists(groupDir)) {
                        Files.list(groupDir).forEach(p -> archivos.add("🏢 [" + g.getNombre() + "] " + p.getFileName().toString())); 
                    }
                } catch (IOException e) { }
            }
        }
        model.addAttribute("misGrupos", misGrupos);
        model.addAttribute("usuarioLogueado", usuarioLogueado);
        model.addAttribute("carpetaActual", recurso + " / 2026 / Informes");
        model.addAttribute("nombreRecurso", recurso);
        model.addAttribute("archivos", archivos);
        model.addAttribute("usuarios", usuarioRepository.findAll()); 
        return "explorador";
    }

    @GetMapping("/busqueda")
    public String busqueda(@RequestParam(value = "q", required = false) String q, @RequestParam(value = "ext", required = false) String ext, Model model, Authentication auth) {
        String username = auth.getName();
        List<String> resultados = new ArrayList<>();
        Path userDir = rootLocation.resolve(username);
        try {
            if (Files.exists(userDir)) {
                Stream<Path> files = Files.list(userDir);
                if (q != null && !q.isEmpty()) files = files.filter(p -> p.getFileName().toString().toLowerCase().contains(q.toLowerCase()));
                if (ext != null && !ext.isEmpty()) files = files.filter(p -> p.getFileName().toString().endsWith(ext));
                resultados = files.map(p -> p.getFileName().toString()).collect(Collectors.toList());
            }
        } catch (IOException e) { }
        if (q != null && !q.isEmpty()) log("Búsqueda", "FILTRAR", "Buscó: " + q, username);
        
        model.addAttribute("usuarioLogueado", usuarioRepository.findByCorreo(username).orElse(null));
        model.addAttribute("resultados", resultados);
        model.addAttribute("query", q);
        return "busqueda";
    }

    @GetMapping("/transferencias")
    public String transferencias(Model model, Authentication auth) {
        String username = auth.getName();
        List<String> subidos = new ArrayList<>();
        Path userDir = rootLocation.resolve(username);
        try { if (Files.exists(userDir)) Files.list(userDir).forEach(p -> subidos.add(p.getFileName().toString())); } catch (IOException e) {}
        model.addAttribute("usuarioLogueado", usuarioRepository.findByCorreo(username).orElse(null));
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
                log("Transferencias", "SUBIR", "Subió: " + archivo.getOriginalFilename(), username);
                ra.addFlashAttribute("mensajeExito", "¡Archivo " + archivo.getOriginalFilename() + " subido exitosamente!");
            } catch (IOException e) { ra.addFlashAttribute("mensajeError", "Error: " + e.getMessage()); }
        }
        return "redirect:/transferencias";
    }

    @PostMapping("/transferir-archivo")
    public String transferir(@RequestParam("archivo") String archivo, @RequestParam("destino") String destino, RedirectAttributes ra, Authentication auth) {
        String username = auth.getName();
        try {
            String nombreReal = archivo.replaceAll("^🏢 \\[[^\\]]+\\] ", "").trim();
            Path origen = rootLocation.resolve(username).resolve(nombreReal);
            if (!Files.exists(origen)) {
                for (Path p : Files.list(rootLocation).filter(Files::isDirectory).toList()) {
                    if (p.getFileName().toString().startsWith("grupo_")) {
                        Path f = p.resolve(nombreReal);
                        if (Files.exists(f)) { origen = f; break; }
                    }
                }
            }
            Path destinoDir = destino.startsWith("GRUPO_") ? rootLocation.resolve("grupo_" + destino.replace("GRUPO_", "")) : rootLocation.resolve(destino);
            Files.createDirectories(destinoDir);
            Files.copy(origen, destinoDir.resolve(nombreReal), StandardCopyOption.REPLACE_EXISTING);
            log("Explorador", "TRANSFERIR", "Transfirió " + nombreReal + " a " + destino, username);
            ra.addFlashAttribute("mensajeExito", "¡Archivo compartido exitosamente!");
        } catch (IOException e) {
            ra.addFlashAttribute("mensajeError", "Error al transferir: " + e.getMessage());
        }
        return "redirect:/explorador";
    }

    @PostMapping("/eliminar-archivo")
    public String eliminar(@RequestParam("archivo") String archivo, RedirectAttributes ra, Authentication auth) {
        String username = auth.getName();
        try {
            String nombreReal = archivo.replaceAll("^🏢 \\[[^\\]]+\\] ", "").trim();
            Path file = rootLocation.resolve(username).resolve(nombreReal);
            if (!Files.exists(file)) {
                for (Path p : Files.list(rootLocation).filter(Files::isDirectory).toList()) {
                    if (p.getFileName().toString().startsWith("grupo_")) {
                        Path f = p.resolve(nombreReal);
                        if (Files.exists(f)) { file = f; break; }
                    }
                }
            }
            Files.deleteIfExists(file);
            log("Explorador", "ELIMINAR", "Eliminó: " + nombreReal, username);
            ra.addFlashAttribute("mensajeExito", "¡Archivo eliminado!");
        } catch (IOException e) {
            ra.addFlashAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/explorador";
    }

    @PostMapping("/crear-carpeta")
    public String crearCarpeta(@RequestParam("nombreCarpeta") String nombreCarpeta, RedirectAttributes ra, Authentication auth) {
        String username = auth.getName();
        try {
            Path carpeta = rootLocation.resolve(username).resolve(nombreCarpeta);
            Files.createDirectories(carpeta);
            log("Explorador", "CREAR_CARPETA", "Creó carpeta: " + nombreCarpeta, username);
            ra.addFlashAttribute("mensajeExito", "¡Carpeta " + nombreCarpeta + " creada!");
        } catch (IOException e) {
            ra.addFlashAttribute("mensajeError", "Error al crear carpeta: " + e.getMessage());
        }
        return "redirect:/explorador";
    }

    @GetMapping("/descargar-archivo")
    public void descargar(@RequestParam("nombre") String nombre, @RequestParam(value = "preview", required = false, defaultValue = "false") boolean preview, HttpServletResponse response, Authentication auth) {
        String username = auth.getName();
        try {
            String nombreReal = nombre.replaceAll("^🏢 \\[[^\\]]+\\] ", "").trim();
            Path file = rootLocation.resolve(username).resolve(nombreReal);
            if (!Files.exists(file)) {
                 for (Path p : Files.list(rootLocation).filter(Files::isDirectory).toList()) {
                    if (p.getFileName().toString().startsWith("grupo_")) {
                        Path f = p.resolve(nombreReal);
                        if (Files.exists(f)) { file = f; break; }
                    }
                }
            }
            if (!Files.exists(file)) {
                Files.createDirectories(rootLocation.resolve(username));
                Files.writeString(file, "Archivo de ejemplo generado por AzureNAS.");
            }
            response.setContentType("application/octet-stream");
            String disposition = preview ? "inline" : "attachment";
            response.setHeader("Content-Disposition", disposition + "; filename=\"" + nombreReal + "\"");
            Files.copy(file, response.getOutputStream());
            response.flushBuffer();
            log("Explorador", preview ? "VISTA_PREVIA" : "DESCARGAR", (preview?"Vio: ":"Descargó: ") + nombreReal, username);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @GetMapping("/auditoria")
    public String auditoria(Model model) {
        model.addAttribute("logs", auditoriaRepository.findAllByOrderByFechaDesc());
        return "auditoria";
    }
}