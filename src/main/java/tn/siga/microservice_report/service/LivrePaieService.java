package tn.siga.microservice_report.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.CompTot;
import tn.siga.microservice_report.service.dto.CompTotDTO;
import tn.siga.microservice_report.service.dto.LivrePaie;
import tn.siga.microservice_report.service.dto.LivrePaieDTO;

import java.util.Comparator;
import java.util.List;

@Service
public class LivrePaieService {


    Logger log = LoggerFactory.getLogger(LivrePaieService.class);
    private final IdtagentRepository idtagentRepository;

    public LivrePaieService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }

    public List<LivrePaie> getLivrePaie (Integer p_annee,
                                         Integer p_mois,
                                         String ufg){


        log.info("--------in service ------");

        List<LivrePaieDTO> livdtolist = idtagentRepository.getLivrePaie(p_annee,p_mois,ufg);

        List<LivrePaie>  livrepaie =  new java.util.ArrayList<>(livdtolist.stream().map(LivrePaie::new).toList());

        livrepaie.sort(Comparator.comparing(LivrePaie::getFamille));


        return livrepaie;
    }



}
