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
import org.springframework.data.repository.query.Param;
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
public class CompTotRepService {
    private final Logger log = LoggerFactory.getLogger(DiffNetRepService.class);
    private final IdtagentRepository idtagentRepository;
    private final CompTotService compTotService;


    public CompTotRepService(IdtagentRepository idtagentRepository, CompTotService compTotService) {
        this.idtagentRepository = idtagentRepository;
        this.compTotService = compTotService;
    }

    // helper method to compile the JasperReport
    private JasperReport compileReport(InputStream jasperStream) throws JRException {
        if (jasperStream == null) {
            throw new JRException("JasperStream is null ");
        }
        return JasperCompileManager.compileReport(jasperStream);

    }

    public byte[] generateReport(String templatePath, Map<String, Object> parameters, JRBeanCollectionDataSource dataSource) throws JRException {
        log.info("Generating  report  ......");
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
            throw new JRException("Error loading  JRXML file", e);
        }
    }

    public byte[] generateCompTotRep(Integer p_annee, Integer p_mois, String p_crps, String p_util, Integer p_annee2, Integer p_mois2) throws JRException {

        List<CompTotDTO> compTotDTOList = idtagentRepository.getCompTot(p_annee, p_mois, p_crps, p_util, p_annee2, p_mois2);

        log.info("the total records  {} ", compTotDTOList.size());

        if (!compTotDTOList.isEmpty()) {
            //log.info("DifNetDTO list found");

            String periode1 = p_annee + " " + " | " + " " + p_mois;
            String periode2 = p_annee2 + " " + " | " + " " + p_mois2;
            String templatePath = "comp_tot_ufg";


            Map<String, Object> parametres = new HashMap<>();
            parametres.put("periode1", periode1);
            parametres.put("periode2", periode2);


            JasperReport subReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/tunisair_rep/sub_comp_tot_ufg.jrxml"));
            JRSaver.saveObject(subReport, "d:\\sub_comp_tot_ufg.jasper");

            List<CompTot> compTots = compTotService.getCompTot( p_annee, p_mois, p_crps, p_util, p_annee2, p_mois2);

            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(compTots);
            parametres.put("comptosource", source);

            return generateReport(templatePath, parametres, source);

        } else {
            throw new IllegalArgumentException("no records found");
        }

    }


}
