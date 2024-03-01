package tn.siga.microservice_report.service.dto;

import lombok.*;
import tn.siga.microservice_report.domain.Idtagent;

import java.io.Serializable;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdtagentDTO {


    private Integer idtmatag;

    private String idtNomag;

    private String idtNomagar;

    private String idtPrnag;

    private String idtPrnagar;

    private String idtPrnpe;

    public IdtagentDTO (AgentDTO agentDTO){
        this.setIdtmatag(agentDTO.getIdtmatag());
        this.setIdtNomag(agentDTO.getNomPrenom());
    }

}
