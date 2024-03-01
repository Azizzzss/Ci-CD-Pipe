package tn.siga.microservice_report.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.DifNetDTO;

import java.util.List;

@Service
public class TestApiRessourceService {

    @Autowired
    private IdtagentRepository idtagentRepository;

   /* public TestApiRessourceService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }*/

    public List<DifNetDTO> getListDifNet(Integer p_annee, Integer p_mois, String p_crps, String p_util, String p_ufg) {
        return idtagentRepository.getListDifNet(p_annee, p_mois, p_crps, p_util, p_ufg);
    }
}
