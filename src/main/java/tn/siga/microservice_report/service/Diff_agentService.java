package tn.siga.microservice_report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.*;

import java.util.Comparator;
import java.util.List;

public class Diff_agentService {

    Logger log = LoggerFactory.getLogger(Diff_agentService.class);
    private final IdtagentRepository idtagentRepository;

    public Diff_agentService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }

    public List<Diff_agent> getDiffagent1 (Integer p_annee1,
                                          Integer p_mois1,
                                          Integer p_annee2,
                                          Integer p_mois2,
                                          String p_crps,
                                          String p_util
                                          ) {

        List<Diff_agentDTO> diffagenttolist = idtagentRepository.getdiffagentquerry1(p_annee1,p_mois1,p_annee2,p_mois2,p_crps,p_util);

        List<Diff_agent>  diffagent1 =  new java.util.ArrayList<>(diffagenttolist.stream().map(Diff_agent::new).toList());

        diffagent1.sort(Comparator.comparing(Diff_agent::getNomPrenom));


        return diffagent1 ;
    }


    public List<Diff_agent> getDiffagent2 (Integer p_annee1,
                                           Integer p_mois1,
                                           Integer p_annee2,
                                           Integer p_mois2,
                                           String p_crps,
                                           String p_util
    ) {

        List<Diff_agentDTO> diffagenttolist = idtagentRepository.getdiffagentquerry2(p_annee1,p_mois1,p_annee2,p_mois2,p_crps,p_util);

        List<Diff_agent>  diffagent2 =  new java.util.ArrayList<>(diffagenttolist.stream().map(Diff_agent::new).toList());

        diffagent2.sort(Comparator.comparing(Diff_agent::getNomPrenom));

        return diffagent2 ;
    }



}
