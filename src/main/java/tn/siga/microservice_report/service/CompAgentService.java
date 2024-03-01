package tn.siga.microservice_report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.CompAgent;
import tn.siga.microservice_report.service.dto.CompAgentDTO;

import java.util.Comparator;
import java.util.List;

@Service
public class CompAgentService {

    private final Logger log = LoggerFactory.getLogger(DiffNetRepService.class);
    private final IdtagentRepository idtagentRepository;
    public CompAgentService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }

    public List<CompAgent> getCompAgent ( Integer p_annee,
                                          Integer p_mois,
                                          Integer p_matricule,
                                          Integer p_annee2,
                                          Integer p_mois2){

        List<CompAgentDTO> compAgentDTOList = idtagentRepository.getCompAgent(p_annee, p_mois, p_matricule,p_annee2, p_mois2);

        //String indemDsg = idtagentRepository.getIndemDsg()

        return compAgentDTOList.stream()
                .map(compAgentDTO -> {
                    CompAgent compAgent = new CompAgent();
                    compAgent.setIdtmatag(p_matricule);
                    compAgent.setAnnee(p_annee);
                    compAgent.setMois(p_mois);
                    compAgent.setAnnee2(p_annee2);
                    compAgent.setMois2(p_mois2);
                    compAgent.setCode(compAgentDTO.getIndemCode());
                   // compAgent.setIndemDsg(idtagentRepository.getIndemDsg(compAgent.getCode()));

                    return compAgent;
                }).map(this::addLigne)
                .filter(compAgent -> compAgent.getSomPeriod1() != 0D || compAgent.getSomPeriod2() != 0D)
                .sorted(Comparator.comparing(CompAgent::getCode)).toList();
    }

    public CompAgent addLigne (CompAgent compAgent){

        Double sommeLigne1 = idtagentRepository.getSumPeriod_2(compAgent.getAnnee2(), compAgent.getMois2(), compAgent.getIdtmatag(), compAgent.getCode());
        Double sommeLigne2 = idtagentRepository.getSumPeriod(compAgent.getAnnee(), compAgent.getMois(), compAgent.getIdtmatag(), compAgent.getCode());
//
        //log.info("somme ligne 1 ------ {} ", sommeLigne1);
       // log.info("somme ligne 2 ------ {} ", sommeLigne2);

        sommeLigne1 = sommeLigne1==null   ?0D:sommeLigne1;
        sommeLigne2 = sommeLigne2==null   ?0D:sommeLigne2;

        compAgent.setSomPeriod1(sommeLigne1);
        compAgent.setSomPeriod2(sommeLigne2);

        Double ecartValue = Math.abs(sommeLigne1-sommeLigne2);
        if (ecartValue <= 0 ) {
            compAgent.setColor("#FFFFFF");
        } else compAgent.setColor("#808080");

        if (sommeLigne1 == 0D && sommeLigne2 ==0D){
        }

        compAgent.setColor(compAgent.getColor());
        //log.info("ecartValue ------ {} ", ecartValue <= 0 );

        String indemDsg = idtagentRepository.getIndemNom(compAgent.getCode());
        compAgent.setIndemDsg(indemDsg);

        return compAgent;
    }


}
