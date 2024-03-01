package tn.siga.microservice_report.web.rest;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.siga.microservice_report.service.*;
import tn.siga.microservice_report.service.dto.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestApiResourceController {

    private static final Logger log = LoggerFactory.getLogger(TestApiResourceController.class);
    private final DiffNetRepService diffNetRepService;
    private final ProvisionPaieRepService provisionPaieRepService;
    private final CtrlAbsenceRepService ctrlAbsenceRepService;
    private final CompRubRepService compRubRepService;
    private final CompAgentRepService compAgentRepService;
    private final CompTotRepService compTotRepService;
    private final ComparatifPaieService comparatifPaieService;
    private final ComparatifPaieRepService comparatifPaieRepService;
    private final ComptaPaieService comptaPaieService;
    private final ComptaPaieRepService comptaPaieRepService;
    private final LivrePaieService livrePaieService ;
    private final LivrePaieRepService livrePaieRepService;
    private final LivrePaieAffRepService livrePaieAffRepService ;
    private final Recap_virementRepService recapVirementRepService ;
    private final PaieGenRepService paieGenRepService ;
    private  final Dec_assurRepService dec_assurRepService ;
    private  final DecImpoRepService decImpoRepService ;



    public TestApiResourceController(DiffNetRepService diffNetRepService,
                                     ProvisionPaieRepService provisionPaieRepService,
                                     CtrlAbsenceRepService ctrlAbsenceRepService,
                                     CompRubRepService compRubRepService,
                                     CompAgentRepService compAgentRepService,
                                     CompTotRepService compTotRepService,
                                     ComparatifPaieService comparatifPaieService,
                                     ComparatifPaieRepService comparatifPaieRepService,
                                     ComptaPaieService comptaPaieService,
                                     ComptaPaieRepService comptaPaieRepService,
                                     LivrePaieService livrePaieService,
                                     LivrePaieRepService livrePaieRepService, LivrePaieAffService livrePaieAffService, LivrePaieAffRepService livrePaieAffRepService, Recap_virementService recapVirementService, Recap_virementRepService recapVirementRepService, PaieGenRepService paieGenRepService, Dec_assurRepService decAssurRepService, Dec_assurService decAssurService, Dec_assurRepService decAssurRepService1, Dec_assurRepService decAssurRepService2, Dec_ImpoService decImpoService, DecImpoRepService decImpoRepService
    ) {
        this.diffNetRepService = diffNetRepService;
        this.provisionPaieRepService=provisionPaieRepService;
        this.ctrlAbsenceRepService=ctrlAbsenceRepService;
        this.compRubRepService = compRubRepService;
        this.compAgentRepService = compAgentRepService;
        this.compTotRepService = compTotRepService;
        this.comparatifPaieService = comparatifPaieService;
        this.comparatifPaieRepService = comparatifPaieRepService;
        this.comptaPaieService = comptaPaieService;
        this.comptaPaieRepService = comptaPaieRepService;
        this.livrePaieRepService = livrePaieRepService;
        this.livrePaieService = livrePaieService;
        this.livrePaieAffRepService = livrePaieAffRepService;
        this.recapVirementRepService = recapVirementRepService;
        this.paieGenRepService = paieGenRepService;
        dec_assurRepService = decAssurRepService2;
        this.decImpoRepService = decImpoRepService;
    }


    @GetMapping("/diff_net-report")
    public void generateReportDiffNet( @RequestParam Integer p_annee,
                                       @RequestParam Integer p_mois,
                                       @RequestParam(required = false) String p_crps,
                                       @RequestParam String p_util,
                                       @RequestParam String p_ufg,
                                       HttpServletResponse response) throws IOException, JRException {

        /*log.debug("request generateReportProvisionPaie with {} {} {} {} {} {}  ",p_annee,p_nmm,p_ufg,p_crps,p_util,p_gufg);*/
        byte[] reportBytes = diffNetRepService.generateDiffNetRep(p_annee,p_mois,p_crps,p_util,p_ufg);
        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");
        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);
        response.getOutputStream().flush();
    }


    @GetMapping("/provision_paie-report")
    public void generateReportProvisionPaie( @RequestParam Integer p_annee,
                                             @RequestParam Integer p_nmm,
                                             @RequestParam String p_ufg,
                                             @RequestParam(required = false) String p_crps,
                                             @RequestParam String p_util,
                                             @RequestParam Integer p_gufg, HttpServletResponse response) throws IOException, JRException {

        log.debug("request generateReportProvisionPaie with {} {} {} {} {} {}  ",p_annee,p_nmm,p_ufg,p_crps,p_util,p_gufg);
        byte[] reportBytes = provisionPaieRepService.generateProvisionPaietRep(p_annee,p_nmm,p_ufg,p_crps,p_util,p_gufg);
        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");
        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);
        response.getOutputStream().flush();
    }


    @GetMapping("/ctrl_absence-report")
    public void generateReportProvisionPaie(@RequestParam Integer p_annee,
                                            @RequestParam Integer p_mois,
                                            @RequestParam Integer p_matricule, HttpServletResponse response) throws IOException, JRException {

        log.debug("request generateReportCtrlAbsence with {} {} {} ", p_annee, p_mois, p_matricule);

        byte[] reportBytes = ctrlAbsenceRepService.generateCtrlAbsenceRep(p_annee, p_mois, p_matricule);
        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");
        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);
        response.getOutputStream().flush();
    }


    @GetMapping("/comp-rubrique")
    public void generateReportCompRub( @RequestParam Integer p_annee,
                                       @RequestParam Integer p_mois,
                                       @RequestParam Integer p_annee1,
                                       @RequestParam Integer p_mois1,
                                       @RequestParam Integer p_code,
                                       @RequestParam(required = false) String p_crps,
                                       @RequestParam String p_util, HttpServletResponse response) throws IOException, JRException {

       // log.info("request getCompRubrique with {} {} {} {} {} {} {} ",p_annee,p_mois,p_code,p_util,p_crps,p_annee1,p_mois1 );

       //log.debug("request generateReportCtrlAbsence with {{} {} {} {} {} {} {} ",p_annee,p_mois,p_code,p_util,p_crps,p_annee1,p_mois1);

        byte[] reportBytes = compRubRepService.generateCompRubRep(p_annee,p_mois,p_code,p_util,p_crps,p_annee1,p_mois1);
        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");
        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);
        response.getOutputStream().flush();

    }


    @GetMapping("/comp-Agent")
    public void generateReportCompAgent( @Param("p_annee") Integer p_annee,
                                         @Param("p_mois") Integer p_mois,
                                         @Param("p_matricule") Integer p_matricule,
                                         @Param("p_annee2") Integer p_annee2 ,
                                         @Param("p_mois2") Integer p_mois2, HttpServletResponse response) throws IOException, JRException {

        // log.info("request getCompRubrique with {} {} {} {} {}  ",p_annee, p_mois, p_matricule, p_annee2, p_mois2);

        //log.debug("request generateReportCtrlAbsence with {{} {} {} {} {} {} {} ",p_annee,p_mois,p_code,p_util,p_crps,p_annee1,p_mois1);

        byte[] reportBytes = compAgentRepService.generateCompAgentRep(p_annee, p_mois, p_matricule, p_annee2, p_mois2);
        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");
        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);
        response.getOutputStream().flush();
    }


    @GetMapping("/comp-tot")
    public void generateReportCompTot( @Param("p_annee") Integer p_annee,
                                       @Param("p_mois") Integer p_mois,
                                       @Param("p_crps") String p_crps,
                                       @Param("p_util") String p_util,
                                       @Param("p_annee") Integer p_annee2,
                                       @Param("p_mois") Integer p_mois2, HttpServletResponse response) throws IOException, JRException {

        // log.info("request getCompRubrique with {} {} {} {} {}  ",p_annee, p_mois, p_matricule, p_annee2, p_mois2);

        //log.debug("request generateReportCtrlAbsence with {{} {} {} {} {} {} {} ",p_annee,p_mois,p_code,p_util,p_crps,p_annee1,p_mois1);

        byte[] reportBytes = compTotRepService.generateCompTotRep(p_annee, p_mois,p_crps, p_util, p_annee2, p_mois2);
        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");
        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);
        response.getOutputStream().flush();
    }


    @GetMapping("/comparatif-paie")
    public void generateReportComparatifPaie(@Param("p_annee") Integer p_annee,
                                             @Param("p_mois") Integer p_mois,
                                             @Param("p_annee") Integer p_annee2,
                                             @Param("p_mois") Integer p_mois2,
                                             HttpServletResponse response) throws IOException, JRException {

        // log.info("request getCompRubrique with {} {} {} {} {}  ",p_annee, p_mois, p_matricule, p_annee2, p_mois2);

        //log.debug("request generateReportCtrlAbsence with {{} {} {} {} {} {} {} ",p_annee,p_mois,p_code,p_util,p_crps,p_annee1,p_mois1);

        byte[] reportBytes = comparatifPaieRepService.generateComparatifPaie(p_annee, p_mois, p_annee2, p_mois2);
        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");
        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);
        response.getOutputStream().flush();
    }


    @GetMapping("/compta-paie")
    public void generateReportComparatifPaie(@Param("p_annee") Integer p_annee,
                                             @Param("p_mois") Integer p_mois,
                                             @Param("p_ufg") String p_ufg,
                                             @Param("p_gufg") Integer p_gufg,
                                             HttpServletResponse response) throws IOException, JRException {

        // log.info("request getCompRubrique with {} {} {} {} {}  ",p_annee, p_mois, p_matricule, p_annee2, p_mois2);

        log.debug("request generateReportCtrlAbsence with {} {} {} {}  ", p_annee, p_mois, p_ufg, p_gufg);

        byte[] reportBytes = comptaPaieRepService.generateComptaPaieRep(p_annee, p_mois, p_ufg, p_gufg);
        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");
        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);
        response.getOutputStream().flush();
    }


    @GetMapping("/comptapaie")
    public ResponseEntity<List<ComptaPaie>> getComparatifPaie(@Param("p_annee") Integer p_annee,
                                                              @Param("p_mois") Integer p_mois,
                                                              @Param("p_ufg") String p_ufg,
                                                              @Param("p_gufg") Integer p_gufg){
        try {
            log.info("request getCompTot with {} {} {} {}   ",p_annee, p_mois,  p_ufg, p_gufg);
            List<ComptaPaie> result =  comptaPaieService.getComptaPaie( p_annee, p_mois,p_ufg, p_gufg);
            log.info("sizeListe Comparatif {} ", result.size());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/livre-paie")
    public void generateReportLivrePaie( @Param("p_annee") Integer p_annee,
                                         @Param("p_mois") Integer p_mois,
                                         @Param("p_ufg") String p_ufg,
                                         HttpServletResponse response) throws IOException, JRException {

        // log.info("request getCompRubrique with {} {} {} {} {}  ",p_annee, p_mois, p_matricule, p_annee2, p_mois2);

        log.debug("request generateReportLivrePaie with {} {} {} {}  ",p_annee,p_mois,p_ufg);

        byte [] reportBytes = livrePaieRepService.generateLivrePaieRep(p_annee,p_mois,p_ufg);

        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");

        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);
        response.getOutputStream().flush();

    }


    @GetMapping("/livre-paie-aff")
    public void generateReportLivrePaieAff(@Param("p_annee") Integer p_annee,
                                           @Param("p_mois") Integer p_mois ,
                                           HttpServletResponse response) throws JRException, IOException {

        log.debug("request generateReportLivrePaieAff with {} {} {} {}  ",p_annee,p_mois);

        byte [] reportBytes = livrePaieAffRepService.generateLivrePaieAffRep(p_annee,p_mois);

        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");

        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);
        response.getOutputStream().flush();

    }


    @GetMapping("/recap-virement")
    public void generateReportRecapvirement(@Param("p_annee") Integer p_annee,
                                            @Param("p_mois") Integer p_mois,
                                            @Param("p_gufg") Integer p_gufg,
                                            @Param("p_ufg") Integer p_ufg,
                                            HttpServletResponse response) throws JRException, IOException {

        log.debug("request generateReportLivrePaieAff with {} {} {} {}  ", p_annee, p_mois);

        byte[] reportBytes = recapVirementRepService.generateRecapVirement(p_annee, p_mois, p_gufg, p_ufg);

        // Set the response headers
        response.setContentType("application/pdf");

        response.setHeader("Content-Disposition", "inline; filename=report.pdf");

        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);

        response.getOutputStream().flush();

    }


    @GetMapping("/paie-gen")
    public void generateReportRecapvirement(@Param("p_annee") Integer p_annee,
                                            @Param("p_mois") Integer p_mois,
                                            HttpServletResponse response) throws JRException, IOException

    {
        log.debug("request generateReportLivrePaieAff with {} {} {} {}  ", p_annee, p_mois);

        byte[] reportBytes = paieGenRepService.generatePaieGenRep(p_annee, p_mois);

        // Set the response headers
        response.setContentType("application/pdf");

        response.setHeader("Content-Disposition", "inline; filename=report.pdf");

        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);

        response.getOutputStream().flush();
    }


    @GetMapping("/dec-assur")
    public void generateReportDecassur(@Param("p_annee") Integer p_annee,
                                            @Param("p_mois") Integer p_mois,
                                            HttpServletResponse response) throws JRException, IOException

    {
        log.debug("request generateReportLivrePaieAff with {} {} {} {}  ", p_annee, p_mois);

        byte[] reportBytes = dec_assurRepService.generateDecassurRep(p_annee, p_mois);

        // Set the response headers
        response.setContentType("application/pdf");

        response.setHeader("Content-Disposition", "inline; filename=report.pdf");

        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);

        response.getOutputStream().flush();
    }

    @GetMapping("/dec-impo")
    public void generateReportDecimpo(@Param("p_annee") Integer p_annee,
                                       @Param("p_mois") Integer p_mois,
                                       HttpServletResponse response) throws JRException, IOException

    {
        log.debug("request generateReportDecImpo with {} {} {} {}  ", p_annee, p_mois);

        byte[] reportBytes = decImpoRepService.generateDecimpoRep(p_annee, p_mois);

        // Set the response headers
        response.setContentType("application/pdf");

        response.setHeader("Content-Disposition", "inline; filename=report.pdf");

        // Write the report bytes to the response output stream
        response.getOutputStream().write(reportBytes);

        response.getOutputStream().flush();
    }












}








