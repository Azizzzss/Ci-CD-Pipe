package tn.siga.microservice_report.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.CompTot;
import tn.siga.microservice_report.service.dto.CompTotDTO;
import tn.siga.microservice_report.service.dto.Recap_virement;
import tn.siga.microservice_report.service.dto.Recap_virementDTO;

import java.util.Comparator;
import java.util.List;

@Service
public class Recap_virementService {

    Logger log = LoggerFactory.getLogger(Recap_virementService.class);
    private final IdtagentRepository idtagentRepository;

    public Recap_virementService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }

    public List<Recap_virement> getRecap_virement (Integer p_annee,
                                                      Integer p_mois , Integer p_gufg , Integer p_ufg ){

        List<Recap_virementDTO> Recap_virementDTOList = idtagentRepository.getrecapvirement(p_annee,p_mois,p_gufg , p_ufg);

        List<Recap_virement> Recap_virementTotList = new java.util.ArrayList<>(Recap_virementDTOList.stream().map(Recap_virement::new).toList());

        //sort liste with code_indem parameter
        Recap_virementTotList.sort(Comparator.comparing(Recap_virement::getBnkcode));

        return Recap_virementTotList;
    }


}
