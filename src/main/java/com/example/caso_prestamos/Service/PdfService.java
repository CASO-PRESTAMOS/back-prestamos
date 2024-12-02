package com.example.caso_prestamos.Service;

import com.example.caso_prestamos.Domain.Entity.*;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.ConverterProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import org.springframework.http.*;

@Service
public class PdfService {

    private final RestTemplate restTemplate;

    public PdfService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static String generateLoanReport(Loan loan) {
        StringBuilder htmlContent = new StringBuilder();

        // Estilos CSS
        String css = "<style>"
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; }"
                + "h1 { color: #1280E3; text-align: center; }"
                + "table { width: 100%; border-collapse: collapse; margin-top: 20px; }"
                + "th, td { border: 1px solid #dddddd; padding: 8px; text-align: center; }"
                + "th { background-color: #1280E3; color: white; }"
                + ".container { padding: 20px; }"
                + ".section-title { font-weight: bold; color: #1280E3; margin-top: 30px; }"
                + ".info-table td { border: none; text-align: left; padding: 4px 0; }"
                + ".info-table { width: 100%; }"
                + ".footer { text-align: center; margin-top: 30px; font-size: 12px; }"
                + "img { vertical-align: middle; height: 16px; width: 16px; margin-left: 5px; }"  // Icono tamaño ajustado
                + "</style>";

        // Encabezado
        htmlContent.append("<html><head><title>Reporte de Préstamo</title>").append(css).append("</head><body>");

        // Título
        htmlContent.append("<div class='container'>")
                .append("<h1>Reporte de Préstamo</h1>");

        // Información del préstamo
        htmlContent.append("<div class='section-title'>Datos del Préstamo:</div>");
        htmlContent.append("<table class='info-table'>")
                .append("<tr><td><b>Usuario:</b> <img src='https://img.icons8.com/ios-filled/50/000000/user.png' alt='Usuario'></td><td>").append(loan.getUser().getFullName()).append("</td></tr>")
                .append("<tr><td><b>Monto:</b> <img src='https://img.icons8.com/ios-filled/50/4caf50/money.png' alt='Monto'></td><td>S/ ").append(loan.getAmount()).append("</td></tr>")
                .append("<tr><td><b>Duración:</b> <img src='https://img.icons8.com/ios-filled/50/000000/calendar.png' alt='Duración'></td><td>").append(loan.getMonths()).append(" meses</td></tr>")
                .append("<tr><td><b>Tasa de Interés:</b> <img src='https://img.icons8.com/ios-filled/50/f44336/percentage.png' alt='Tasa de Interés'></td><td>").append((int)(loan.getInterestRate() * 100)).append("%</td></tr>")
                .append("<tr><td><b>Fecha de Inicio:</b> <img src='https://img.icons8.com/ios-filled/50/000000/calendar.png' alt='Fecha de Inicio'></td><td>").append(loan.getStartDate()).append("</td></tr>")
                .append("<tr><td><b>Fecha de Fin:</b> <img src='https://img.icons8.com/ios-filled/50/000000/calendar.png' alt='Fecha de Fin'></td><td>").append(loan.getEndDate()).append("</td></tr>");

        // Agregar un icono en la sección "Estado"
        if (loan.getStatus() == LoanStatus.PAID) {
            htmlContent.append("<tr><td><b>Estado:</b> <img src='https://img.icons8.com/ios-filled/50/4caf50/ok.png' alt='Pagado'></td><td>Pagado a tiempo</td></tr>");
        } else if (loan.getStatus() == LoanStatus.LATE) {
            htmlContent.append("<tr><td><b>Estado:</b> <img src='https://img.icons8.com/ios-filled/50/f44336/clock.png' alt='Retraso'></td><td>Pagado con retraso</td></tr>");
        }

        htmlContent.append("</table>");

        // Cronograma de Pagos
        htmlContent.append("<div class='section-title'>Cronograma de Pagos:</div>");
        htmlContent.append("<table>")
                .append("<tr><th>N° Cuota</th><th>Fecha Vto</th><th>Situación</th><th>Fecha Pago</th></tr>");

        int paymentNumber = 1;
        for (PaymentSchedule schedule : loan.getPaymentScheduleList()) {
            // Declarar la variable Estado fuera del bloque condicional
            String Estado = "";  // Valor por defecto vacío
            String iconUrl = "";  // URL para los iconos

            // Asigna el valor de Estado y el icono según el estado del préstamo
            if (loan.getStatus() == LoanStatus.PAID) {
                Estado = "Pagado a tiempo";
                iconUrl = "https://img.icons8.com/ios-filled/50/4caf50/ok.png";  // Icono de 'ok'
            } else if (loan.getStatus() == LoanStatus.LATE) {
                Estado = "Pagado con retraso";
                iconUrl = "https://img.icons8.com/ios-filled/50/f44336/clock.png";  // Icono de 'retraso'
            } else {
                Estado = "Sin estado"; // Si no está ni pagado a tiempo ni con retraso
                iconUrl = "https://img.icons8.com/ios-filled/50/9e9e9e/question-mark.png";  // Icono de 'sin estado'
            }

            htmlContent.append("<tr>")
                    .append("<td>").append(paymentNumber++).append("</td>")
                    .append("<td>").append(schedule.getPaymentDate()).append("</td>")
                    .append("<td>").append(Estado).append(" <img src='").append(iconUrl).append("' alt='Estado'></td>")
                    .append("<td>").append(schedule.getLateSince() != null ? schedule.getLateSince() : "-").append("</td>")
                    .append("</tr>");
        }

        htmlContent.append("</table>");
        htmlContent.append("</div>");

        // Pie de página
        htmlContent.append("<div class='footer'>")
                .append("<p>Reporte generado por el sistema de gestión de préstamos.</p>")
                .append("</div>");

        // Cierre del HTML
        htmlContent.append("</body></html>");

        return htmlContent.toString();
    }

    public String generateBoleta(Loan loan, PaymentSchedule paymentSchedule) {
        StringBuilder htmlContent2 = new StringBuilder();

        String css = "<style>"
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; }"
                + "h1 { color: #1280E3; text-align: center; }"
                + "table { width: 100%; border-collapse: collapse; margin-top: 20px; }"
                + "th, td { border: 1px solid #dddddd; padding: 8px; text-align: center; }"
                + "th { background-color: #1280E3; color: white; }"
                + ".container { padding: 20px; }"
                + ".section-title { font-weight: bold; color: #1280E3; margin: 30px 0 10px 0; }"
                + ".info-table { width: 100%; }"
                + ".info-table td { border: none; text-align: left; padding: 8px 15px; }" // Asegura margen uniforme
                + ".info-table td:first-child { font-weight: bold; width: 30%; }" // Alinea el primer td en todas las filas
                + ".footer { text-align: center; margin-top: 30px; font-size: 12px; }"
                + "img { vertical-align: middle; height: 16px; width: 16px; margin-left: 5px; }"
                + "</style>";


        // Encabezado
        htmlContent2.append("<html><head><title>Reporte de Préstamo</title>").append(css).append("</head><body>");

        // Título
        htmlContent2.append("<div class='container'>")
                .append("<h1>Reporte de Préstamo</h1>");
        htmlContent2.append("<div class='section-title'>Datos del Préstamo:</div>");
        if (loan.getUser().getIdentifier().length() == 8) {

            // Generar BOLETA (DNI)
            htmlContent2.append("<table class='info-table'>")
                    .append("<tr><td><b>Usuario:</b> <img src='https://img.icons8.com/ios-filled/50/000000/user.png' alt='Usuario'></td><td>").append(loan.getUser().getFullName()).append("</td></tr>")
                    .append("<tr><td><b>Monto:</b> <img src='https://img.icons8.com/ios-filled/50/4caf50/money.png' alt='Monto'></td><td>S/ ").append(loan.getAmount()).append("</td></tr>")
                    .append("<tr><td><b>Duración:</b> <img src='https://img.icons8.com/ios-filled/50/000000/calendar.png' alt='Duración'></td><td>").append(loan.getMonths()).append(" meses</td></tr>")
                    .append("<tr><td><b>Tasa de Interés:</b> <img src='https://img.icons8.com/ios-filled/50/f44336/percentage.png' alt='Tasa de Interés'></td><td>").append((int) (loan.getInterestRate() * 100)).append("%</td></tr>")
                    .append("<tr><td><b>Fecha de Inicio:</b> <img src='https://img.icons8.com/ios-filled/50/000000/calendar.png' alt='Fecha de Inicio'></td><td>").append(loan.getStartDate()).append("</td></tr>")
                    .append("<tr><td><b>Fecha de Fin:</b> <img src='https://img.icons8.com/ios-filled/50/000000/calendar.png' alt='Fecha de Fin'></td><td>").append(loan.getEndDate()).append("</td></tr>")
                    .append("</table>");
        } else if (loan.getUser().getIdentifier().length() == 11) {

            // Generar FACTURA (RUC)
            htmlContent2.append("<table class='info-table'>")
                    .append("<tr><td><b>RUC:</b> <img src='https://img.icons8.com/ios-filled/50/000000/user.png' alt='Usuario'></td><td>").append(loan.getUser().getIdentifier()).append("</td></tr>")
                    .append("<tr><td><b>Razón Social:</b> <img src='https://img.icons8.com/ios-filled/50/000000/user.png' alt='Usuario'> </td><td>").append(loan.getUser().getFullName()).append("</td></tr>") // Aquí usamos getFullName para la razón social
                    .append("<tr><td><b>Monto:</b> <img src='https://img.icons8.com/ios-filled/50/4caf50/money.png' alt='Monto'></td><td>S/ ").append(loan.getAmount()).append("</td></tr>")
                    .append("<tr><td><b>Duración:</b> <img src='https://img.icons8.com/ios-filled/50/000000/calendar.png' alt='Duración'></td><td>").append(loan.getMonths()).append(" meses</td></tr>")
                    .append("<tr><td><b>Tasa de Interés:</b> <img src='https://img.icons8.com/ios-filled/50/f44336/percentage.png' alt='Tasa de Interés'></td><td>").append((int) (loan.getInterestRate() * 100)).append("%</td></tr>")
                    .append("<tr><td><b>Fecha de Inicio:</b> <img src='https://img.icons8.com/ios-filled/50/000000/calendar.png' alt='Fecha de Inicio'></td><td>").append(loan.getStartDate()).append("</td></tr>")
                    .append("<tr><td><b>Fecha de Fin:</b> <img src='https://img.icons8.com/ios-filled/50/000000/calendar.png' alt='Fecha de Fin'></td><td>").append(loan.getEndDate()).append("</td></tr>")
                    .append("</table>");
        } else {
            // Identificador inválido
            throw new RuntimeException("El identificador no corresponde a un DNI o RUC válido.");
        }

        // Datos del cronograma de pago (PaymentSchedule)
        htmlContent2.append("<div class='section-title'>Detalles del Pago:</div>");
        String Estado = "";  // Valor por defecto vacío
        String iconUrl = "";  // URL para los iconos

        if (paymentSchedule.getStatus() == PaymentStatus.PAID) {
            Estado = "Pagado a tiempo";
            iconUrl = "https://img.icons8.com/ios-filled/50/4caf50/ok.png";  // Ícono de 'ok'
        } else if (paymentSchedule.getStatus() == PaymentStatus.LATE) {
            Estado = "Pagado con retraso";
            iconUrl = "https://img.icons8.com/ios-filled/50/f44336/clock.png";  // Ícono de 'retraso'
        } else {
            Estado = "Sin estado";  // Si no está ni pagado a tiempo ni con retraso
            iconUrl = "https://img.icons8.com/ios-filled/50/9e9e9e/question-mark.png";  // Ícono de 'sin estado'
        }

// Genera el contenido HTML
        htmlContent2.append("<table class='info-table'>")
                .append("<tr>")
                .append("<td><b>Fecha de Pago:</b> <img class='icon' src='https://img.icons8.com/ios-filled/50/000000/calendar.png' alt='Fecha de Pago'></td>")
                .append("<td>").append(paymentSchedule.getPaymentDate()).append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td><b>Monto a Pagar:</b> <img class='icon' src='https://img.icons8.com/ios-filled/50/4caf50/money.png' alt='Monto a Pagar'></td>")
                .append("<td>S/ ").append(paymentSchedule.getAmount()).append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td><b>Fecha de Retraso:</b> <img class='icon' src='https://img.icons8.com/ios-filled/50/9e9e9e/question-mark.png' alt='Retraso'></td>")
                .append("<td>").append(paymentSchedule.getLateSince() != null ? paymentSchedule.getLateSince() : "-").append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td><b>Estado:</b> <img class='icon' src='").append(iconUrl).append("' alt='Estado'></td>")
                .append("<td>").append(Estado).append("</td>")
                .append("</tr>")
                .append("</table>");

        htmlContent2.append("</table>");

        htmlContent2.append("</div>");

        // Pie de página
        htmlContent2.append("<div class='footer'>")
                .append("<p>Reporte generado por el sistema de gestión de préstamos.</p>")
                .append("</div>");

        // Cierre del HTML
        htmlContent2.append("</body></html>");

        return htmlContent2.toString();
    }

    public byte[] generateLoanReportPdf(Loan loan) {
        String htmlContent = generateLoanReport(loan);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
            return outputStream.toByteArray(); // Retorna el PDF como un array de bytes
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Manejo de errores básico
        }
    }
    public byte[] generateBoletagenerate(Loan loan, PaymentSchedule paymentSchedule) {
        String htmlContent2 = generateBoleta(loan, paymentSchedule);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(htmlContent2, outputStream, converterProperties);
            return outputStream.toByteArray(); // Retorna el PDF como un array de bytes
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Manejo de errores básico
        }
    }

}


