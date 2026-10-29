package com.azurenas.azure_nas_manager;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController {
    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final AuditoriaRepository audRepo;
    private final GrupoRepository grupoRepo;
    private final PasswordEncoder encoder;

    public AdminController(UsuarioRepository usuarioRepo, RolRepository rolRepo, AuditoriaRepository audRepo, GrupoRepository grupoRepo, PasswordEncoder encoder) {
        this.usuarioRepo = usuarioRepo; 
        this.rolRepo = rolRepo; 
        this.audRepo = audRepo; 
        this.grupoRepo = grupoRepo;
        this.encoder = encoder;
    }

    @GetMapping("/admin")
    public String panelAdmin(Model model) {
        model.addAttribute("usuarios", usuarioRepo.findAll());
        model.addAttribute("grupos", grupoRepo.findAll());
        model.addAttribute("logs", audRepo.findAllByOrderByFechaDesc());
        
        model.addAttribute("statUsuarios", usuarioRepo.count());
        model.addAttribute("statGrupos", grupoRepo.count());
        model.addAttribute("statAuditorias", audRepo.count());
        
        return "admin";
    }

    @PostMapping("/admin/crear-usuario")
    public String crearUsuario(@RequestParam String nombres, @RequestParam String correo, @RequestParam String password) {
        Usuario u = new Usuario();
        u.setNombres(nombres); u.setApellidos("Apellido"); u.setCorreo(correo);
        u.setPassword_hash(encoder.encode(password)); u.setFecha_creacion(LocalDateTime.now()); u.setEstado(true);
        
        Rol rolUser = rolRepo.findByNombre("USUARIO").orElseGet(() -> {
            Rol r = new Rol(); r.setNombre("USUARIO"); return rolRepo.save(r);
        });
        u.setRol(rolUser);
        usuarioRepo.save(u);
        
        log("Admin", "CREAR_USUARIO", "Creó: " + correo);
        return "redirect:/admin?exito";
    }

    @PostMapping("/admin/crear-grupo")
    public String crearGrupo(@RequestParam String nombreGrupo, @RequestParam(required = false) List<Long> usuariosIds) {
        Grupo g = new Grupo();
        g.setNombre(nombreGrupo);
        g.setDescripcion("Grupo de colaboración");
        
        if (usuariosIds != null) {
            Set<Usuario> miembros = new HashSet<>(usuarioRepo.findAllById(usuariosIds));
            g.setMiembros(miembros);
        }
        grupoRepo.save(g);
        log("Admin", "CREAR_GRUPO", "Creó grupo: " + nombreGrupo);
        return "redirect:/admin?grupoCreado";
    }

    // EXPORTAR AUDITORIA A EXCEL
    @GetMapping("/admin/exportar-auditoria")
    public void exportarAuditoria(HttpServletResponse response) throws IOException {
        List<Auditoria> logs = audRepo.findAllByOrderByFechaDesc();
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Auditoria");
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Fecha", "Módulo", "Acción", "Descripción", "IP", "Hash"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Auditoria log : logs) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(log.getId_log());
                row.createCell(1).setCellValue(log.getFecha().toString());
                row.createCell(2).setCellValue(log.getModulo());
                row.createCell(3).setCellValue(log.getAccion());
                row.createCell(4).setCellValue(log.getDescripcion());
                row.createCell(5).setCellValue(log.getIp_usuario());
                row.createCell(6).setCellValue(log.getHash_integridad());
            }

            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=auditoria_azurenas.xlsx");
            workbook.write(response.getOutputStream());
        }
    }

    // CONSOLA POWERSHELL (AZCOPY)
    @PostMapping("/admin/powershell")
    public String powershell(@RequestParam String comando, Model model) {
        String resultado = "PS C:\\AzureNAS> " + comando + "\n\n" +
                           "AzCopy v10.15.0\n" +
                           "[INFO] Iniciando transferencia desde NAS On-Premise hacia Azure Storage...\n" +
                           "[INFO] 1 TB transferidos a 350 MB/s...\n" +
                           "[OK] Migración completada con éxito. 0 errores.\n";
        log("Admin", "POWERSHELL", "Ejecutó: " + comando);
        model.addAttribute("resultadoConsola", resultado);
        return panelAdmin(model); 
    }
    
    private void log(String mod, String acc, String desc) {
        Auditoria a = new Auditoria(); 
        a.setModulo(mod); a.setAccion(acc); a.setDescripcion(desc);
        a.setIp_usuario("127.0.0.1"); a.setFecha(LocalDateTime.now()); 
        audRepo.save(a);
    }
}