package tn.siga.microservice_report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.ComptaPaie;
import tn.siga.microservice_report.service.dto.ComptaPaieDTO;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ComptaPaieService {

    private final Logger log = LoggerFactory.getLogger(ComptaPaieService.class);
    private final IdtagentRepository idtagentRepository;

    public ComptaPaieService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }

    public List <ComptaPaie> getComptaPaie (Integer p_annee,
                                               Integer p_mois,
                                               String p_ufg,
                                               Integer p_gufg){

        List <ComptaPaieDTO> comptaPaieDTOList = idtagentRepository.getComptaPaie(p_annee,  p_mois, p_ufg, p_gufg);

        List <ComptaPaie> comptaPaieList = new ArrayList<> (comptaPaieDTOList.stream().map(ComptaPaie::new).toList());

        comptaPaieList.sort(Comparator.comparing(ComptaPaie::getCompte));

        ComptaPaie debitSum = new ComptaPaie();

        double totalDebit = comptaPaieList.stream().mapToDouble(ComptaPaie::getDebit).sum();
        double roundedDebit = Math.round(totalDebit * 1000.0) / 1000.0;
        debitSum.setDebit(roundedDebit);

        double totalCredit = comptaPaieList.stream().mapToDouble(ComptaPaie::getCredit).sum();
        double roundedCredit = Math.round(totalCredit * 1000.0) / 1000.0;
        debitSum.setCredit(roundedCredit);

        double totalSolde = comptaPaieList.stream().mapToDouble(ComptaPaie::getSolde).sum();
        double roundedSolde = Math.round(totalSolde * 1000.0) / 1000.0;
        debitSum.setSolde(roundedSolde);

        comptaPaieList.add(debitSum);

        return comptaPaieList;
    }

}
