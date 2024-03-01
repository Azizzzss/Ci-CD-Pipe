package tn.siga.microservice_report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.AgentDTO;
import tn.siga.microservice_report.service.dto.CtrlAbsence;
import java.util.Comparator;
import java.util.List;

@Service
public class CtrlAbsenceService {

    private static final Logger log = LoggerFactory.getLogger(CtrlAbsenceService.class);
    private final IdtagentRepository idtagentRepository;


    public CtrlAbsenceService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }

    public List<CtrlAbsence> getCtrlAbsence (List<AgentDTO> agentDTOList) {

        List<CtrlAbsence> ctrlAbsences = new java.util.ArrayList<>(agentDTOList.stream().map(CtrlAbsence::new).toList());

        //sort liste with matricule parameter
        ctrlAbsences.sort(Comparator.comparing(CtrlAbsence::getIdtmatag));

        return ctrlAbsences;

    }

}
