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
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaieGenRepService {


    private final PaieGenService paieGenService ;

    Logger log = LoggerFactory.getLogger(PaieGenRepService.class) ;

    public PaieGenRepService(PaieGenService paieGenService) {
        this.paieGenService = paieGenService;
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


    public byte[] generatePaieGenRep(Integer p_annee, Integer p_mois) throws JRException {

        List<Paie_gen> PaiegenList = paieGenService.getPaieGen(p_annee,p_mois);

        for (Paie_gen paieGen : PaiegenList) {
            paieGen.setIdtposte("");
        }

        //log.info("the total records  {} ", livrePaieDTOList.size());

        if (!PaiegenList.isEmpty()) {

            //log.info("DifNetDTO list found");
            String templatePath = "paie_gen";

            Map<String, Object> parametres = new HashMap<>();
            parametres.put("p_annee", p_annee);
            parametres.put("p_mois", p_mois);
            parametres.put("FileTitle", "Paie-Gen");


            JasperReport subReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/tunisair_rep/sub_paie_gen.jrxml"));
            JRSaver.saveObject(subReport, "d:\\sub_paie_gen.jasper");

            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(PaiegenList);

            parametres.put("paiegensource", source);

            return generateReport(templatePath, parametres, source);

        } else {
            throw new IllegalArgumentException("no records found");
        }

    }




}
