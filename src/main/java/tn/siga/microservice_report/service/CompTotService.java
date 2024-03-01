package tn.siga.microservice_report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.CompTot;
import tn.siga.microservice_report.service.dto.CompTotDTO;


import java.util.Comparator;
import java.util.List;

@Service
public class CompTotService {

    private final Logger log = LoggerFactory.getLogger(DiffNetRepService.class);
    private final IdtagentRepository idtagentRepository;

    public CompTotService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }

    public List<CompTot> getCompTot (Integer p_annee,
                                     Integer p_mois,
                                     String p_crps,
                                     String p_util,
                                     Integer p_annee2,
                                     Integer p_mois2) {

        List<CompTotDTO> compTotDTOList = idtagentRepository.getCompTot(p_annee, p_mois, p_crps, p_util, p_annee2, p_mois2);

        List<CompTot> compTotList = new java.util.ArrayList<>(compTotDTOList.stream().map(CompTot::new).toList());

        //sort liste with code_indem parameter
        compTotList.sort(Comparator.comparing(CompTot::getCode));

        return compTotList;
    }



   /* public CompTot addLigne (CompTot compTot){
        // Set indemDsg for each column
        String indemDsg = idtagentRepository.getIndemNom(compTot.getCode());
        compTot.setIndemDsg(indemDsg);

        // Set somme mnt total for each column
        Double somme1 = idtagentRepository.getSomme(compTot.getAnnee1(), compTot.getMois1(), compTot.getCode(), compTot.getCrps(), compTot.getUtil());
        Double somme2 = idtagentRepository.getSomme(compTot.getAnnee2(), compTot.getMois2(), compTot.getCode(), compTot.getCrps(), compTot.getUtil());

        //if somme is null then take 0
        somme1 = somme1==null ?0D:somme1;
        somme2 = somme2==null ?0D:somme2;

        compTot.setSomPeriod1(somme1);
        compTot.setSomPeriod2(somme2);

        //set nbr Agent in the periode
        Integer nbr1 = idtagentRepository.nbrAgent(compTot.getAnnee1(), compTot.getMois1(), compTot.getCode(), compTot.getCrps(), compTot.getUtil());
        Integer nbr2 = idtagentRepository.nbrAgent(compTot.getAnnee2(), compTot.getMois2(), compTot.getCode(), compTot.getCrps(), compTot.getUtil());
        compTot.setNbrAgent1(nbr1);
        compTot.setNbrAgent2(nbr2);

        //set ecart value between somm1 & somme2
        compTot.setEcart(Math.abs(somme1-somme2));

        return compTot;
    }

    public List<CompTot> getCompTotUfg () {

        List<CompTotDTO> compTotDTOList = idtagentRepository.getCompTotUfg();
        return compTotDTOList.stream()
                .map(compTotDTO -> {
                    CompTot compTot = new CompTot();
                    compTot.setCode(compTot.getCode());
                    compTot.getAnnee1();
                    compTot.getMois1();
                    compTot.getAnnee2();
                    compTot.getMois2();
                    compTot.getNbrAgent1();
                    compTot.getNbrAgent2();
                    compTot.getEcart();
                    return compTot;

                }).map(this::addLigne).sorted(Comparator.comparing(CompTot::getCode)).toList();
    }*/




    /*
    select INDEMCODE,LIB_INDEM,MNT_PERIODE1,MNT_PERIODE2,nvl(MNT_PERIODE1,0) - nvl(MNT_PERIODE2,0) ecart from (
select b.IND_CODE  as indemCode ,ind_dsg as lib_indem,
(select sum(decode(a.sens,2,-mvp_mnt,mvp_mnt)) from mvtpaie a
    where a.ind_code = b.ind_code
    and a.ann_an = :p_annee and a.nmm_code = :p_mois) as mnt_periode1,
(select sum(decode(a.sens,2,-mvp_mnt,mvp_mnt)) from mvtpaie a
    where a.ind_code = b.ind_code
    and a.ann_an = :p_annee2 and a.nmm_code = :p_mois2) as mnt_periode2
from indem b
where ind_code not in(99995,900050,900060,1002)
order by 1 )
where nvl(MNT_PERIODE1,0) - nvl(MNT_PERIODE2,0) <> 0
*/

}
