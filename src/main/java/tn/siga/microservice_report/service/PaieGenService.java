package tn.siga.microservice_report.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.LivrePaie;
import tn.siga.microservice_report.service.dto.Paie_gen;
import tn.siga.microservice_report.service.dto.Paie_genDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PaieGenService {


    Logger log = LoggerFactory.getLogger(PaieGenService.class);

    private final IdtagentRepository idtagentRepository ;

    public PaieGenService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }


    public List<Paie_gen> getPaieGen (Integer p_annee,
                                      Integer p_mois){


        List<Paie_genDto> paiegendtotolist = idtagentRepository.getpaiegen(p_annee , p_mois);

        List<Paie_gen> paieGens =  new java.util.ArrayList<>(paiegendtotolist.stream().map(Paie_gen::new).toList());

        paieGens.sort(Comparator.comparing(Paie_gen::getIdtmatag));


        return  paieGens;
    }



}
