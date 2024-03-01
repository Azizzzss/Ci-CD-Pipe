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
import tn.siga.microservice_report.service.dto.CompRubr;
import tn.siga.microservice_report.service.dto.CompRubrDTO;
import tn.siga.microservice_report.service.dto.CtrlAbsence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompRubRepService {
    private final Logger log = LoggerFactory.getLogger(DiffNetRepService.class);
    private final IdtagentRepository idtagentRepository;
    private final CompRubService compRubService;

    public CompRubRepService(IdtagentRepository idtagentRepository, CompRubService compRubService) {
        this.idtagentRepository = idtagentRepository;
        this.compRubService = compRubService;
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

    public byte[] generateCompRubRep(Integer p_annee,  Integer p_mois, Integer p_code, String p_util, String p_crps, Integer p_annee1, Integer p_mois1) throws JRException {

        List<CompRubrDTO> compRubrDTOList = idtagentRepository.getCompRub(p_annee, p_mois,p_code,p_util,p_crps,p_annee1,p_mois1);

        log.info("total records {} ",compRubrDTOList.size());
        if (!compRubrDTOList.isEmpty()) {
            log.info("DifNetDTO list found");
            String templatePath = "comp_rub";

            String coreDsg = p_crps + " : " + idtagentRepository.getDsgCore(p_crps);
            String indemDsg = idtagentRepository.getIndemDsg(p_code);
            String peride1 = p_annee + " | " + p_mois;
            //log.info(" show peride1 --------- {} ",peride1);
            String peride2 = p_annee1 + " | " + p_mois1;
            //log.info(" show peride2 --------- {} ",peride2);

            Map<String, Object> parametres = new HashMap<>();
            parametres.put("peride1", peride1);
            parametres.put("peride2", peride2);
            parametres.put("code", indemDsg);
            parametres.put("crps", coreDsg);
            // parametres.put("annee1", p_annee1);
            //parametres.put("mois1", p_mois1);
            parametres.put("util", p_util);

            //subreport
            JasperReport subReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/tunisair_rep/sub_comp_rub.jrxml"));
            JRSaver.saveObject(subReport, "d:\\sub_comp_rub.jasper");
            List<CompRubr> compRubrs = compRubService.getCompRubrique( p_annee, p_mois, p_code, p_util, p_crps, p_annee1, p_mois1);

            Double ecart =  compRubrs.stream().mapToDouble(CompRubr::getEcartValue).sum();
            parametres.put("sum_ecart", ecart);

            Integer count = compRubrs.size();
            parametres.put("count_agent", count);

            //log.info("total records {} ",ctrlAbsences.size());
            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(compRubrs);
            parametres.put("comprubsource", source);
            //log.info("generating ...");
            return generateReport(templatePath, parametres, source);

        } else {
            throw new IllegalArgumentException("No records found");
        }

    }


}
