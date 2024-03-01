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
import tn.siga.microservice_report.service.dto.BulletinNegatif;
import tn.siga.microservice_report.service.dto.DifNetDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProvisionPaieRepService {

    private final Logger log = LoggerFactory.getLogger(DiffNetRepService.class);
    private final IdtagentRepository idtagentRepository;
    private final NegatfService negatfService;

    public ProvisionPaieRepService(IdtagentRepository idtagentRepository, NegatfService negatfService) {
        this.idtagentRepository = idtagentRepository;
        this.negatfService = negatfService;
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

    public byte[] generateProvisionPaietRep(Integer p_annee, Integer p_nmm, String p_ufg, String p_crps, String p_util, Integer p_gufg) throws JRException {

        List<DifNetDTO> difNetDTOList = idtagentRepository.getListBullNeg( p_annee, p_nmm, p_ufg, p_crps, p_util, p_gufg);

        if (!difNetDTOList.isEmpty()) {
            log.info("DifNetDTO list found");

            String corps = idtagentRepository.getDsgCore(p_crps);
            String libGufg =  "Société : " + idtagentRepository.getLibGufg(p_gufg);
            String libelletUfg =  "UG : " + idtagentRepository.getLibUfg(p_ufg);

            String reference = p_annee +" / " + p_nmm;
            String templatePath = "provision_paie";

            Map<String, Object> parametres = new HashMap<>();
            parametres.put("annee", p_annee);
            parametres.put("mois", p_nmm);
            parametres.put("ufg", libelletUfg);
            parametres.put("crps", corps);
            parametres.put("util", p_util);
            parametres.put("gufg", libGufg);
            parametres.put("reference", reference);

            JasperReport subReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/tunisair_rep/sub_provision_paie.jrxml"));
            JRSaver.saveObject(subReport, "d:\\sub_provision_paie.jasper");

            List<BulletinNegatif> bulletinNegatifs = negatfService.getBulletinNegatif(difNetDTOList);

            log.info("total records {} ",bulletinNegatifs.size());
            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(bulletinNegatifs);
            parametres.put("provisionsource", source);
            log.info("generating ...");
            return generateReport(templatePath, parametres, source);
        } else {
            throw new IllegalArgumentException("No records found");
        }
    }


}
