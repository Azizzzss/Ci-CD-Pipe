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
import tn.siga.microservice_report.service.dto.CompAgent;
import tn.siga.microservice_report.service.dto.CompAgentDTO;
import tn.siga.microservice_report.service.dto.CompRubr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class CompAgentRepService {
    private final Logger log = LoggerFactory.getLogger(DiffNetRepService.class);
    private final IdtagentRepository idtagentRepository;
    private final CompAgentService compAgentService;

    public CompAgentRepService(IdtagentRepository idtagentRepository, CompAgentService compAgentService) {
        this.idtagentRepository = idtagentRepository;
        this.compAgentService = compAgentService;
    }

    // helper method to compile the jasper report
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


    public byte [] generateCompAgentRep( Integer p_annee, Integer p_mois, Integer p_matricule, Integer p_annee2, Integer p_mois2) throws JRException {
        List<CompAgentDTO> compAgentDTOList = idtagentRepository.getCompAgent(p_annee, p_mois, p_matricule, p_annee2, p_mois2);

        log.info("total records  {} ",compAgentDTOList.size());
        if (!compAgentDTOList.isEmpty()) {

            log.info("DifNetDTO list  found");

            String templatePath = "comp_agent";

            String peride2 = p_annee + " | " + p_mois;
            //log.info(" show peride1 --------- {} ",peride1);
            String peride1 = p_annee2 + " | " + p_mois2;
            //log.info(" show peride2 --------- {} ",peride2);

            Map<String, Object> parametres = new HashMap<>();
            parametres.put("peride1", peride1);
            parametres.put("peride2", peride2);

            String nomPrenom = idtagentRepository.getNomAgent(p_matricule);
            parametres.put("nomPrenom", nomPrenom);
            parametres.put("matricule",p_matricule);

            JasperReport subReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/tunisair_rep/sub_comp_agent.jrxml"));
            JRSaver.saveObject(subReport, "d:\\sub_comp_agent.jasper");
            List<CompAgent> compAgents = compAgentService.getCompAgent( p_annee, p_mois,p_matricule, p_annee2, p_mois2);

            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(compAgents);

            parametres.put("comprubsource", source);
            return generateReport(templatePath, parametres, source);


        } else {
            throw new IllegalArgumentException("No records found");
        }
    }
}
