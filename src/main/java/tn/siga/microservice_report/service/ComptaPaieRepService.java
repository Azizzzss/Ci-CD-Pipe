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
import tn.siga.microservice_report.service.dto.CompTot;
import tn.siga.microservice_report.service.dto.CompTotDTO;
import tn.siga.microservice_report.service.dto.ComptaPaie;
import tn.siga.microservice_report.service.dto.ComptaPaieDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComptaPaieRepService {

    private final Logger log = LoggerFactory.getLogger(DiffNetRepService.class);
    private final IdtagentRepository idtagentRepository;
    private final ComptaPaieService comptaPaieService;

    public ComptaPaieRepService(IdtagentRepository idtagentRepository, ComptaPaieService comptaPaieService) {
        this.idtagentRepository = idtagentRepository;
        this.comptaPaieService = comptaPaieService;
    }

    public byte[] generateComptaPaieRep(Integer p_annee,  Integer p_mois,  String p_ufg,  Integer p_gufg) throws JRException {

        List<ComptaPaieDTO> comptaPaieDTOList = idtagentRepository.getComptaPaie(p_annee, p_mois, p_ufg, p_gufg);

        log.info("total records {} ", comptaPaieDTOList.size());
        if (!comptaPaieDTOList.isEmpty()) {
            String nomMois = idtagentRepository.getNomMois(p_mois);
            String dateCalcule = nomMois + " "  + p_annee;
            String libelletUfg = idtagentRepository.getLibUfg(p_ufg);
            String libelletGufg = idtagentRepository.getLibGufg(p_gufg);
            log.info("libellet UFG {} ",p_ufg);
            String templatePath = "compta_paie";

            Map<String, Object> parametres = new HashMap<>();
            parametres.put("nomMois", nomMois);
            parametres.put("libelletUfg", libelletUfg);
            parametres.put("libelletGufg", libelletGufg);
            parametres.put("dateCalcule", dateCalcule);
            parametres.put("ufg", p_ufg);

            JasperReport subReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/tunisair_rep/sub_compta_paie.jrxml"));
            JRSaver.saveObject(subReport, "d:\\sub_compta_paie.jasper");

            List<ComptaPaie> comptaPaieList = comptaPaieService.getComptaPaie( p_annee, p_mois, p_ufg, p_gufg);

            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(comptaPaieList);
            parametres.put("comptapaiesource", source);

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
