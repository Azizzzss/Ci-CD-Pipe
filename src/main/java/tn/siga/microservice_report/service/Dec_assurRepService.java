package tn.siga.microservice_report.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.service.dto.Dec_assur;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class Dec_assurRepService {

    Logger log = LoggerFactory.getLogger(Dec_assurRepService.class);
    private final Dec_assurService decAssurService ;

    public Dec_assurRepService(Dec_assurService decAssurService) {
        this.decAssurService = decAssurService;
    }


    private JasperReport compileReport(InputStream jasperStream) throws JRException {
        if (jasperStream == null) {
            throw new JRException("JasperStream is null");
        }
        return JasperCompileManager.compileReport(jasperStream);
    }

    public byte[] generateReport(String templatePath, Map<String, Object> parameters, JRBeanCollectionDataSource dataSource) throws JRException {
        log.info("Generating report ......");
        // Check templatePath
        InputStream jasperStream = this.getClass().getResourceAsStream("/tunisair_rep/" + templatePath + ".jrxml");
        log.info("Finding templatePath ......");

        try (jasperStream) {
            JasperReport jasperReport = compileReport(jasperStream);
            log.info("Compile report jasperStream ......");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            final Exporter exporter = new JRPdfExporter();
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.exportReport();
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Error loading JRXML file: {}", e.getMessage());
            throw new JRException("Error loading JRXML file", e);
        }
    }

    public byte[] generateDecassurRep(Integer p_annee, Integer p_mois) throws JRException {

        List<Dec_assur> Decassurlist = decAssurService.getDecassure(p_annee,p_mois);



        if (!Decassurlist.isEmpty()) {

            //log.info("DifNetDTO list found");
            String templatePath = "dec_assur";

            Map<String, Object> parametres = new HashMap<>();
            parametres.put("p_annee", p_annee);
            parametres.put("p_mois", p_mois);
            parametres.put("FileTitle", "Dec-Assur");


            JasperReport subReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/tunisair_rep/sub_dec_assur.jrxml"));
            JRSaver.saveObject(subReport, "d:\\sub_dec_assur.jasper");

            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(Decassurlist);

            parametres.put("decassuresource", source);

            return generateReport(templatePath, parametres, source);

        } else {

            throw new IllegalArgumentException("no records found");
        }

    }


}
