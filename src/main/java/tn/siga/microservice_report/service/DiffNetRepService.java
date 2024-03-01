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
import tn.siga.microservice_report.service.dto.BulleteinDesquilibre;
import tn.siga.microservice_report.service.dto.DifNetDTO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiffNetRepService {

    private final Logger log = LoggerFactory.getLogger(DiffNetRepService.class);
    private final IdtagentRepository idtagentRepository;
    private final CreditService creditService;

    public DiffNetRepService(IdtagentRepository idtagentRepository, CreditService creditService) {
        this.idtagentRepository = idtagentRepository;
        this.creditService = creditService;
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
        log.info("Finding subtemplatePath ......");
        // Check templatePath
        InputStream jasperStream = this.getClass().getResourceAsStream("/tunisair_rep/" + templatePath + ".jrxml");
        log.info("Finding templatePath ......");

        try (jasperStream) {
            log.info("in try .....");
            log.info("subjasperPrint  ......");
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

    public byte[] generateDiffNetRep(Integer p_annee, Integer p_mois, String p_crps, String p_util, String p_ufg) throws JRException {

        List<DifNetDTO> difNetDTOList = idtagentRepository.getListDifNet(p_annee, p_mois, p_crps, p_util, p_ufg);

        if (!difNetDTOList.isEmpty()) {
            String templatePath = "diff_net";

            Map<String, Object> parametres = new HashMap<>();
            parametres.put("annee", p_annee);
            parametres.put("mois", p_mois);
            parametres.put("crps", p_crps);
            parametres.put("util", p_util);
            parametres.put("ufg", p_ufg);


            JasperReport subReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/tunisair_rep/sub_diff_net.jrxml"));
            JRSaver.saveObject(subReport, "d:\\sub_diff_net.jasper");
            List<BulleteinDesquilibre> bulleteinDesquilibres=creditService.getListBulletinDesiqui(p_annee, p_mois, p_crps, p_util, p_ufg);

            log.info("total records {} ",bulleteinDesquilibres.size());
            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(bulleteinDesquilibres);

            parametres.put("diffnetsource", source);
            log.info("generating ...");
            return generateReport(templatePath, parametres, source);
        } else {
            throw new IllegalArgumentException("No records found");
        }
    }
}