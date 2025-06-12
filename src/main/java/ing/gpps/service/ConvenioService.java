package ing.gpps.service;

import ing.gpps.entity.institucional.Convenio;
import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Service
public class ConvenioService {

    public void generarArchivoConvenio(Convenio convenio) {
        String nombreArchivo = "convenio_" + convenio.getId() + "_" + 
                             LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
        
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write("=== DATOS DEL CONVENIO ===\n\n");
            writer.write("ID del Convenio: " + convenio.getId() + "\n");
            writer.write("Estudiante: " + convenio.getEstudiante().getNombre() + "\n");
            writer.write("Proyecto: " + convenio.getProyecto().getTitulo() + "\n");
            writer.write("Tutor Externo: " + convenio.getTutorExterno().getNombre() + "\n");
            writer.write("Entidad: " + convenio.getEntidad().getNombre() + "\n");
            writer.write("Docente Supervisor: " + convenio.getDocenteSupervisor().getNombre() + "\n");
            writer.write("Dirección de Carrera: " + convenio.getDireccionCarrera().getNombre() + "\n");
            writer.write("\nFecha de generación: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo del convenio: " + e.getMessage(), e);
        }
    }
} 