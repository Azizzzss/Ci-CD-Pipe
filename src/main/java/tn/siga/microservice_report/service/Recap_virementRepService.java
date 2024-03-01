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
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.LivrePaie;
import tn.siga.microservice_report.service.dto.LivrePaieDTO;
import tn.siga.microservice_report.service.dto.Recap_virement;
import tn.siga.microservice_report.service.dto.Recap_virementDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Recap_virementRepService {


    Logger log = LoggerFactory.getLogger(Recap_virementService.class);

    private final IdtagentRepository idtagentRepository;


    private final Recap_virementService recapVirementService ;



    public Recap_virementRepService(IdtagentRepository idtagentRepository, Recap_virementService recapVirementService) {
        this.idtagentRepository = idtagentRepository;
        this.recapVirementService = recapVirementService;
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


    public byte[] generateRecapVirement(Integer p_annee, Integer p_mois,  Integer p_gufg ,Integer p_ufg) throws JRException {

        List<Recap_virementDTO> livrePaieDTOList = idtagentRepository.getrecapvirement(p_annee,p_mois,p_gufg, p_ufg);

        //log.info("the total records  {} ", livrePaieDTOList.size());

        if (!livrePaieDTOList.isEmpty()) {
            //log.info("DifNetDTO list found");

            String templatePath = "Recap-virement";

            Map<String, Object> parametres = new HashMap<>();
            parametres.put("p_annee", p_annee);
            parametres.put("p_mois", p_mois);
            parametres.put("p_ufg", p_ufg);
            parametres.put("p_gufg", p_gufg);
            parametres.put("FileTitle", "Recap Virement");


            JasperReport subReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/tunisair_rep/subRecap_virement.jrxml"));
            JRSaver.saveObject(subReport, "d:\\subRecap_virement.jasper");

            List<Recap_virement> Recapvirement = recapVirementService.getRecap_virement( p_annee, p_mois, p_gufg , p_ufg);

            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(Recapvirement);

            parametres.put("Recapvirement", source);

            return generateReport(templatePath, parametres, source);

        } else {
            throw new IllegalArgumentException("no records found");
        }

    }

}
