package tn.siga.microservice_report.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.Dec_assur;
import tn.siga.microservice_report.service.dto.Dec_assurDTO;
import tn.siga.microservice_report.service.dto.LivrePaie;
import tn.siga.microservice_report.service.dto.LivrePaieDTO;

import java.util.Comparator;
import java.util.List;

@Service
public class Dec_assurService {

    Logger log = LoggerFactory.getLogger(Dec_assurService.class);
    private final IdtagentRepository idtagentRepository ;

    public Dec_assurService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }

    public List<Dec_assur> getDecassure(Integer p_annee,
                                        Integer p_mois){

        List<Dec_assurDTO> decassurtolist = idtagentRepository.getdec_assur(p_annee,p_mois);

        List<Dec_assur>  decassur =  new java.util.ArrayList<>(decassurtolist.stream().map(Dec_assur::new).toList());

        decassur.sort(Comparator.comparing(Dec_assur::getIdtmatag));


        return decassur ;
    }


}
