package tn.siga.microservice_report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.Dec_assur;
import tn.siga.microservice_report.service.dto.Dec_assurDTO;
import tn.siga.microservice_report.service.dto.Dec_impo;
import tn.siga.microservice_report.service.dto.Dec_impoDTO;

import java.util.Comparator;
import java.util.List;

@Service
public class Dec_ImpoService {

    Logger log = LoggerFactory.getLogger(Dec_assurService.class);

    private final IdtagentRepository idtagentRepository;


    public Dec_ImpoService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }

    public List<Dec_impo> getDecimpo(Integer p_annee, Integer p_mois){

        List<Dec_impoDTO> decassurtolist = idtagentRepository.getdec_impo(p_annee,p_mois);

        List<Dec_impo>  decimpo =  new java.util.ArrayList<>(decassurtolist.stream().map(Dec_impo::new).toList());

        return decimpo ;
    }




}
