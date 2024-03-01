package tn.siga.microservice_report.service;

import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.BulleteinDesquilibre;
import tn.siga.microservice_report.service.dto.DifNetDTO;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

@Service
public class CreditService {

    private final IdtagentRepository idtagentRepository;

    public CreditService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }
    public List<BulleteinDesquilibre> getListBulletinDesiqui(Integer p_annee,
                                                             Integer p_mois,
                                                             String p_crps,
                                                             String p_util,
                                                             String p_ufg)

    {

        List<DifNetDTO> difNetDTOList=idtagentRepository.getListDifNet(p_annee,p_mois,p_crps,p_util,p_ufg);

        List<BulleteinDesquilibre> bulleteinDesquilibres= new java.util.ArrayList<>(difNetDTOList.stream().map(BulleteinDesquilibre::new).toList());

        bulleteinDesquilibres.forEach(bulleteinDesquilibre -> {
            double resDouble = bulleteinDesquilibre.getMontant()-bulleteinDesquilibre.getCredit();
            String resltString = String.format("%.3f", resDouble).replace(',', '.');;
            Double resFinal = Double.parseDouble(resltString);
                Double credit= idtagentRepository.getSumPaie(p_annee,p_mois,bulleteinDesquilibre.getIdtmatag());
                bulleteinDesquilibre.setCredit(credit);
                bulleteinDesquilibre.setDifference(resFinal);
        });

        bulleteinDesquilibres.sort(Comparator.comparing(BulleteinDesquilibre::getIdtmatag));
        return  bulleteinDesquilibres ;
    }
}
