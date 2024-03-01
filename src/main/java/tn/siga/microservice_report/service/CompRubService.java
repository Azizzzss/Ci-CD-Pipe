package tn.siga.microservice_report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.*;


import java.util.Comparator;
import java.util.List;

@Service
public class CompRubService {


    private final Logger log = LoggerFactory.getLogger(DiffNetRepService.class);
    private final IdtagentRepository idtagentRepository;

    public CompRubService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }

    public List<CompRubr> getCompRubrique(Integer p_annee,
                                          Integer p_mois,
                                          Integer p_code,
                                          String p_util,
                                          String p_crps,
                                          Integer p_annee1,
                                          Integer p_mois1) {

        List<CompRubrDTO> compRubrDTOList = idtagentRepository.getCompRub(p_annee, p_mois, p_code, p_util, p_crps, p_annee1, p_mois1);
      //  result.stream().mapToDouble(CompRubr::getEcartValue).sum();
        String corp = idtagentRepository.getDsgCore(p_crps);

       // log.info("nb idtagent {} ",compRubrDTOList.size());
       return compRubrDTOList.stream()
               .parallel()
                .map(compRubrDTO -> {
                    CompRubr compRubr = new CompRubr();
                    compRubr.setIdtmatag(compRubrDTO.getIdtmatag());
                    compRubr.setAnnee(p_annee);
                    compRubr.setCorpAgent(corp);
                    compRubr.setNomPrenom(compRubrDTO.getFullname());
                    compRubr.setMois(p_mois);
                    compRubr.setAnnee1(p_annee1);
                    compRubr.setMois1(p_mois1);
                    compRubr.setCode(p_code);
                    return compRubr;

                })
                .map(this::fillLigne).sorted(Comparator.comparing(CompRubr::getIdtmatag)).toList();



    }


    public CompRubr fillLigne( CompRubr compRubr){

        // BigDecimal sommep1=BigDecimal.ZERO;
        Double sommep1 = idtagentRepository.getSumPeriod(compRubr.getAnnee(), compRubr.getMois(), compRubr.getIdtmatag(), compRubr.getCode());
       // log.info("getting somme 1 {} {} {} {} = {}",compRubr.getAnnee(), compRubr.getMois(), compRubr.getIdtmatag(), compRubr.getCode() ,sommep1);

        Double sommep2 = idtagentRepository.getSumPeriod(compRubr.getAnnee1(), compRubr.getMois1(), compRubr.getIdtmatag(), compRubr.getCode());
        //log.info("getting somme 2 {} {} {} {} = {}",compRubr.getAnnee1(), compRubr.getMois1(), compRubr.getIdtmatag(), compRubr.getCode() ,sommep2);

        sommep2=sommep2==null   ?0D:sommep2;
        sommep1=sommep1==null   ?0D:sommep1;

        compRubr.setSomPeriod1(sommep1);
        compRubr.setSomPeriod2(sommep2);
        compRubr.setEcartValue(Math.abs(sommep2-sommep1));

        return compRubr;
    }

}
