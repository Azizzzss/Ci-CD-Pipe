package tn.siga.microservice_report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.LivrePaie;
import tn.siga.microservice_report.service.dto.LivrePaieAff;
import tn.siga.microservice_report.service.dto.LivrePaieAffDto;
import tn.siga.microservice_report.service.dto.LivrePaieDTO;

import java.util.Comparator;
import java.util.List;

@Service
public class LivrePaieAffService {

    Logger log = LoggerFactory.getLogger(LivrePaieService.class);
    private final IdtagentRepository idtagentRepository ;

    public LivrePaieAffService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }
    public List<LivrePaieAff> getLivrePaie (Integer p_annee, Integer p_mois)
    {

        List<LivrePaieAffDto> livdtolist = idtagentRepository.getLivrePaieAff(p_annee,p_mois);

        List<LivrePaieAff>  livrepaieaff =  new java.util.ArrayList<>(livdtolist.stream().map(LivrePaieAff::new).toList());

        livrepaieaff.sort(Comparator.comparing(LivrePaieAff::getIdtmatag));


        return livrepaieaff ;
    }

}
