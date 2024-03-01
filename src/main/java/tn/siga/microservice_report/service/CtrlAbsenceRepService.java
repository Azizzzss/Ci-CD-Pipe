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
import tn.siga.microservice_report.service.dto.AgentDTO;
import tn.siga.microservice_report.service.dto.CtrlAbsence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class CtrlAbsenceRepService {

    private final Logger log = LoggerFactory.getLogger(DiffNetRepService.class);
    private final IdtagentRepository idtagentRepository;
    private final CtrlAbsenceService ctrlAbsenceService;



    public CtrlAbsenceRepService(IdtagentRepository idtagentRepository, CtrlAbsenceService ctrlAbsenceService) {
        this.idtagentRepository = idtagentRepository;
        this.ctrlAbsenceService = ctrlAbsenceService;
    }
    // Helper method to compile the JasperReport
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

    public byte[] generateCtrlAbsenceRep(Integer p_annee, Integer p_mois, Integer p_matricule) throws JRException {

        List<AgentDTO> agentDTOList = idtagentRepository.getCtrlAbscence(p_annee, p_mois, p_matricule);

        log.info("total records {} ",agentDTOList.size());
        if (!agentDTOList.isEmpty()) {
            log.info("DifNetDTO list found");
            String reference = p_annee +" "+" / " +" "+ p_mois;
            String templatePath = "ctrl_absence";
            Map<String, Object> parametres = new HashMap<>();
            parametres.put("annee", p_annee);
            parametres.put("mois", p_mois);
            parametres.put("crps", p_matricule);
            parametres.put("reference", reference);

            //subreport
            JasperReport subReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/tunisair_rep/sub_ctrl_absence.jrxml"));
            JRSaver.saveObject(subReport, "d:\\sub_ctrl_absence.jasper");
            List<CtrlAbsence> ctrlAbsences = ctrlAbsenceService.getCtrlAbsence(agentDTOList);
            //log.info("total records {} ",ctrlAbsences.size());
            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(ctrlAbsences);
            parametres.put("ctrlabsencesource", source);
            //log.info("generating ...");
            return generateReport(templatePath, parametres, source);

        } else {
            throw new IllegalArgumentException("No records found");
        }

    }
}
