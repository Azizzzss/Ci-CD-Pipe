package tn.siga.microservice_report.service;

import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.BulletinNegatif;
import tn.siga.microservice_report.service.dto.DifNetDTO;

import java.util.Comparator;
import java.util.List;

@Service
public class NegatfService {

    private final IdtagentRepository idtagentRepository;

    public NegatfService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }

    public List<BulletinNegatif> getBulletinNegatif ( List<DifNetDTO> difNetDTOList) {

        List<BulletinNegatif> bulletinNegatif = new java.util.ArrayList<>(difNetDTOList.stream().map(BulletinNegatif::new).toList());

        bulletinNegatif.forEach(bulletinNegatiff -> {
            String nomAgent = idtagentRepository.getNomAgent(bulletinNegatiff.getIdtmatag());
            bulletinNegatiff.setNomPrenom(nomAgent);

            String corp = idtagentRepository.getDsgCore(bulletinNegatiff.getCorpAgent());
            bulletinNegatiff.setCorpAgent(corp);

        });

        bulletinNegatif.sort(Comparator.comparing(BulletinNegatif::getIdtmatag));
        return bulletinNegatif;


    }
}
