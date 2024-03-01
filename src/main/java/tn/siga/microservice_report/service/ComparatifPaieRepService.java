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
import java.util.stream.Collectors;

@Service
public class ComparatifPaieRepService {
    private final Logger log = LoggerFactory.getLogger(DiffNetRepService.class);
    private final IdtagentRepository idtagentRepository;
    private final ComparatifPaieService comparatifPaieService;

    public ComparatifPaieRepService(IdtagentRepository idtagentRepository, ComparatifPaieService comparatifPaieService) {
        this.idtagentRepository = idtagentRepository;
        this.comparatifPaieService = comparatifPaieService;
    }

    public byte [] generateComparatifPaie( Integer p_annee, Integer p_mois, Integer p_annee2, Integer p_mois2) throws JRException {
        List<ComparatifPaieDTO> comparatifPaieDTOList = idtagentRepository.getComparatifPaie(p_annee, p_mois,  p_annee2, p_mois2);

        log.info("total records {} ",comparatifPaieDTOList.size());
        if (!comparatifPaieDTOList.isEmpty()) {
            //log.info("DifNetDTO list found");
            String templatePath = "comparatif_paie";

            String peride1 = p_annee + " | " + p_mois;
            //log.info(" show peride1 --------- {} ",peride1);
            String peride2 = p_annee2 + " | " + p_mois2;
            //log.info(" show peride2 --------- {} ",peride2);

            Map<String, Object> parametres = new HashMap<>();
            parametres.put("peride1", peride1);
            parametres.put("peride2", peride2);

            JasperReport subReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/tunisair_rep/sub_comparatif_paie.jrxml"));
            JRSaver.saveObject(subReport, "d:\\sub_comparatif_paie.jasper");

            List<ComparatifPaieResult> comparatifPaieList = comparatifPaieService.getComparatif_Paie( p_annee, p_mois, p_annee2, p_mois2);
            //////


            /*List<ComparatifPaieDetails> customComparatifList = comparatifPaieList.stream()
                    .flatMap(result -> result.getIndemnityDetails().stream()
                            .map(dto -> new ComparatifPaieDetails(result, dto)))
                    .toList();*/


            //////

            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(comparatifPaieList);
            //log.info("comparatifPaieList ... {} ",comparatifPaieList.size());

            parametres.put("comparatifpaiesource", source);
            return generateReport(templatePath, parametres, source);

        } else {
            throw new IllegalArgumentException("No records found");
        }
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

    // Helper method to compile the JasperReport
    private JasperReport compileReport(InputStream jasperStream) throws JRException {
        if (jasperStream == null) {
            throw new JRException("JasperStream is null");
        }
        return JasperCompileManager.compileReport(jasperStream);
    }
}