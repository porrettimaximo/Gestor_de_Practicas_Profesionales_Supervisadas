package ing.gpps.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.draw.LineSeparator;
import ing.gpps.entity.institucional.Actividad;
import ing.gpps.entity.Solicitud;
import ing.gpps.entity.institucional.Convenio;
import ing.gpps.repository.ConvenioRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConvenioService {

    private final ConvenioRepository convenioRepository;
    private static final String CARPETA_CONVENIOS = "convenios";
    private static final String FONT_PATH = "fonts/arial.ttf";
    private static final String IMAGE_PATH = "src/main/resources/images/UNRN-201x300.jpg"; // Ruta corregida
    private static final float HEADER_HEIGHT = 100; // Altura para el encabezado

    private BaseFont bfArial;
    private Font titleFont;
    private Font subtitleFont;
    private Font normalFont;
    private Font boldFont;
    private Image headerImage; // Movida aquí para ser accesible desde toda la clase

    public ConvenioService(ConvenioRepository convenioRepository) throws DocumentException, IOException {
        this.convenioRepository = convenioRepository;
        // Inicializar fuentes aquí
        bfArial = BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        titleFont = new Font(bfArial, 14, Font.BOLD);
        subtitleFont = new Font(bfArial, 14, Font.BOLD);
        normalFont = new Font(bfArial, 12, Font.NORMAL);
        boldFont = new Font(bfArial, 12, Font.BOLD);
        
        // Inicializar la imagen del encabezado
        headerImage = Image.getInstance(IMAGE_PATH);
        headerImage.scaleToFit(80, 80); // Ajustamos a un tamaño más visible para el logo
    }

    // Clase interna para el evento de página del encabezado
    class HeaderImageEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                // Posicionar la imagen en la esquina superior izquierda
                float imageX = document.leftMargin();
                float imageY = writer.getPageSize().getTop() - 20; // 20 pts de padding desde el borde superior
                headerImage.setAbsolutePosition(imageX, imageY - headerImage.getScaledHeight());
                writer.getDirectContent().addImage(headerImage);

            } catch (Exception e) {
                System.err.println("Error al añadir elementos al encabezado: " + e.getMessage());
            }
        }
    }

    public Resource generarArchivoConvenio(Convenio convenio) {
        try {
            // Crear la carpeta si no existe
            File carpeta = new File(CARPETA_CONVENIOS);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            String nombreArchivo = CARPETA_CONVENIOS + File.separator + "convenio-" + convenio.getProyecto().getTitulo() + ".pdf";
            
            // Crear el documento PDF con un margen superior suficiente para el encabezado
            Document document = new Document(PageSize.A4, 36, 36, HEADER_HEIGHT, 36); // left, right, top, bottom
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            writer.setPageEvent(new HeaderImageEvent()); // Registrar el evento de página
            document.open();

            // Título del acta solo en la primera página
            Paragraph docTitle = new Paragraph("ACTA ACUERDO\nSOBRE INSTANCIAS DE PRÁCTICAS PROFESIONALES SUPERVISADAS", titleFont);
            docTitle.setAlignment(Element.ALIGN_CENTER);
            docTitle.setSpacingBefore(headerImage.getScaledHeight() + 20); // Espacio para la imagen en la primera página
            docTitle.setSpacingAfter(20);
            document.add(docTitle);

            // Introducción
            Paragraph intro = new Paragraph();
            intro.add(new Chunk("Entre la UNIVERSIDAD NACIONAL DE RÍO NEGRO, representada en este acto por su ENTIDAD ", normalFont));
            intro.add(new Chunk(convenio.getEntidad().getNombre(), boldFont));
            intro.add(new Chunk(", y la/el estudiante de la carrera de Licenciatura en Sistemas, ", normalFont));
            intro.add(new Chunk(convenio.getEstudiante().getNombre()+ " " +convenio.getEstudiante().getApellido(), boldFont));
            intro.add(new Chunk(", DNI "+convenio.getEstudiante().getDni(), boldFont));
            intro.add(new Chunk(", en adelante la/el ESTUDIANTE/PRACTICANTE, convienen celebrar el presente acuerdo, conforme lo establecido por la RESOLUCIÓN CDEyVE SEDE ATLÁNTICA N° 011/2021 y con sujeción a las siguientes cláusulas y condiciones:", normalFont));
            intro.setSpacingBefore(10); // Agrega un pequeño espacio antes de la introducción
            intro.setSpacingAfter(15);
            document.add(intro);

            // Cláusulas
            document.add(new Paragraph("PRIMERA:", boldFont));
            document.add(new Paragraph("El presente tiene como objetivo la implementación del Sistema de Instancias de Prácticas Profesionales Supervisadas \"PPS\", a los fines de que la/el ESTUDIANTE/PRACTICANTE de la carrera Licenciatura en Sistemas, realice actividades de carácter formativo relacionadas con la propuesta curricular de la materia Práctica Profesional Supervisada, de acuerdo al Programa de actividades formativas propuesto en el Anexo I.", normalFont));
            document.add(new Paragraph("\nSEGUNDA:", boldFont));
            document.add(new Paragraph("La situación de/el la/el estudiante practicante no generará ninguna relación laboral entre la/el estudiante y la UNRN, ni tampoco demandará retribución económica alguna a favor del estudiante practicante.", normalFont));
            document.add(new Paragraph("\nTERCERA:", boldFont));
            Paragraph terceraClausula = new Paragraph();
            terceraClausula.add(new Chunk("La Secretaría de Docencia y Vida Estudiantil, designa como tutor/a a ", normalFont));
            terceraClausula.add(new Chunk(convenio.getTutorExterno().getNombre() + " " + convenio.getTutorExterno().getApellido(), boldFont));
            terceraClausula.add(new Chunk(", quien cuenta con experiencia específica y capacidad para planificar, implementar y evaluar propuestas formativas. Asimismo se designa Docente Supervisor a ", normalFont));
            terceraClausula.add(new Chunk(convenio.getDocenteSupervisor().getNombre() + " " + convenio.getDocenteSupervisor().getApellido(), boldFont));
            terceraClausula.add(new Chunk(", quién evaluará el cumplimiento de los aspectos formativos de las tareas de la ESTUDIANTE/PRACTICANTE. Ambos deberán elaborar un plan de trabajo que determine el proceso educativo del estudiante practicante para alcanzar los objetivos propuestos.", normalFont));
            document.add(terceraClausula);
            document.add(new Paragraph("\nCUARTA:", boldFont));
            document.add(new Paragraph("La duración de la práctica tendrá un plazo que no podrá superar la cantidad de 200 (doscientas) horas. Finalizada la misma la/el ESTUDIANTE/PRACTICANTE deberá efectuar un informe final escrito y una instancia de presentación oral del mismo a la que se invitará a concurrir al Tutor que ha supervisado el trabajo de campo. El informe final será evaluado por el Profesor asignado como Docente Supervisor.", normalFont));
            document.add(new Paragraph("\nQUINTA:", boldFont));
            document.add(new Paragraph("Las Instancias de Prácticas Profesionales Supervisadas podrán llevarse a cabo en las instalaciones de la UNRN o en el lugar que ésta disponga según el tipo de labor a desarrollar. Dichos ámbitos reunirán las condiciones de higiene y seguridad dispuesta por la Ley 19587 – Ley de Higiene y Seguridad del Trabajo y sus normas reglamentarias. Con previo acuerdo de ambas partes, las actividades también podrán desarrollarse en el Laboratorio de Informática Aplicada de la Sede Atlántica.", normalFont));
            document.add(new Paragraph("\nSEXTA:", boldFont));
            document.add(new Paragraph("La/El ESTUDIANTE/PRACTICANTE deberá ajustarse a las normas y reglamentos internos de la UNRN que le será comunicada y notificada en el comienzo de las actividades. El horario a asignar no podrá superponerse con el dictado de las horas de clases de la carrera.", normalFont));
            document.add(new Paragraph("\nSÉPTIMA:", boldFont));
            document.add(new Paragraph("La UNRN será la encargada de gestionar la cobertura de los seguros necesarios para la/el ESTUDIANTE/PRACTICANTE que formará parte en las Instancias de Prácticas Profesionales Supervisadas.", normalFont));

            // Firma
            Paragraph firma = new Paragraph("\nEn prueba de conformidad y ratificación de las cláusulas que anteceden, las PARTES firman el presente instrumento, como documento único no editable, a un sólo efecto, conforme surja del detalle de la firma digital inserta y la firma ológrafa de la/el ESTUDIANTE validada por su remisión a la SEDE ATLÁNTICA mediante el correo electrónico denunciado.", normalFont);
            firma.setSpacingBefore(20);
            document.add(firma);

            // Anexo
            document.newPage();
            Paragraph anexoTitle = new Paragraph("ANEXO: PROGRAMA DE ACTIVIDADES FORMATIVAS PROPUESTO", subtitleFont);
            anexoTitle.setAlignment(Element.ALIGN_CENTER);
            anexoTitle.setSpacingAfter(20);
            document.add(anexoTitle);

            Paragraph alumnoInfo = new Paragraph();
            alumnoInfo.add(new Chunk("Alumno: ", boldFont));
            alumnoInfo.add(new Chunk(convenio.getEstudiante().getNombre() + " " + convenio.getEstudiante().getApellido()+"               ", normalFont));
            alumnoInfo.add(new Chunk("Legajo: ", boldFont));
            alumnoInfo.add(new Chunk(String.valueOf(convenio.getEstudiante().getLegajo()), normalFont));
            alumnoInfo.setAlignment(Element.ALIGN_CENTER);

            Paragraph objetivosProyecto = new Paragraph();

            objetivosProyecto.add(new Chunk("\n\nTitulo del Proyecto: ", boldFont));
            objetivosProyecto.add(new Chunk(convenio.getProyecto().getTitulo(), normalFont));

            objetivosProyecto.add(new Chunk("\n\nBreve Descripción del Proyecto: ", boldFont));
            objetivosProyecto.add(new Chunk(convenio.getProyecto().getDescripcion(), normalFont));

            if (convenio.getProyecto() != null) {
                List<String> objetivos = convenio.getProyecto().getObjetivos();
                if (objetivos != null && !objetivos.isEmpty()) {
                    objetivosProyecto.add(new Chunk("\n\nObjetivos del Proyecto:\n", boldFont));
                    for (String objetivo : objetivos) {
                        objetivosProyecto.add(new Chunk("\n  • " + objetivo, normalFont));
                    }
                }
            }

            document.add(alumnoInfo); // Añadir alumnoInfo al documento solo una vez aquí
            document.add(objetivosProyecto);

            // Tabla de Plan de Trabajo
            if (convenio.getProyecto().getPlanDeTrabajo() != null) {
                List<Actividad> actividades = convenio.getProyecto().getPlanDeTrabajo().getActividades();
                if (actividades != null && !actividades.isEmpty()) {
                    document.add(new Paragraph("\nPlan de Trabajo:", boldFont));

                    PdfPTable table = new PdfPTable(2); // 2 columnas
                    table.setWidthPercentage(100); // Ancho de la tabla al 100%
                    table.setSpacingBefore(10f); // Espacio antes de la tabla
                    table.setSpacingAfter(10f); // Espacio después de la tabla

                    // Establecer anchos relativos de las columnas (Actividad: 80%, Horas: 20%)
                    float[] columnWidths = {0.80f, 0.20f};
                    table.setWidths(columnWidths);

                    // Encabezados de la tabla
                    PdfPCell cell1 = new PdfPCell(new Phrase(" Actividad", boldFont));
                    cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell1.setBackgroundColor(new BaseColor(236, 236, 236));
                    table.addCell(cell1);

                    PdfPCell cell2 = new PdfPCell(new Phrase(" Horas", boldFont));
                    cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell2.setBackgroundColor(new BaseColor(236, 236, 236));
                    table.addCell(cell2);

                    int totalHoras = 0;
                    for (Actividad actividad : actividades) {
                        table.addCell(new Phrase(" "+actividad.getNombre(), normalFont));
                        PdfPCell horasCell = new PdfPCell(new Phrase(String.valueOf(actividad.getHoras()), normalFont));
                        horasCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(horasCell);
                        totalHoras += actividad.getHoras();
                    }

                    // Fila Total
                    PdfPCell totalTextCell = new PdfPCell(new Phrase(" TOTAL", boldFont));
                    totalTextCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(totalTextCell);

                    PdfPCell totalHoursCell = new PdfPCell(new Phrase(String.valueOf(totalHoras), boldFont));
                    totalHoursCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(totalHoursCell);
                    
                    document.add(table);
                }
            }
            
            document.close();

            // Convertir el archivo a un recurso descargable
            Path filePath = Paths.get(nombreArchivo);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer el archivo generado");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el archivo del convenio: " + e.getMessage(), e);
        }
    }

    public Convenio obtenerConvenioPorSolicitud(Solicitud solicitud) {
        if (solicitud == null || solicitud.getProyecto() == null || solicitud.getSolicitante() == null) {
            throw new IllegalArgumentException("Solicitud, proyecto o solicitante no pueden ser nulos");
        }

        return convenioRepository.findByProyecto(solicitud.getProyecto())
                .orElseThrow(() -> new RuntimeException("No se encontró un convenio para el proyecto: " + solicitud.getProyecto().getTitulo()));
    }
}