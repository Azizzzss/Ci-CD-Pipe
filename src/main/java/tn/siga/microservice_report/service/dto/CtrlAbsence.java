package tn.siga.microservice_report.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CtrlAbsence {

    public Integer idtmatag;
    public String nomPrenom;
    public Integer typAbsc;
    public String typCong;
    public Integer sens;
    public Integer nbHeure;
    public Integer nbJours;
    public Integer refAnnee;
    public Integer refMois;

    public CtrlAbsence (AgentDTO agentDTO){
        this.idtmatag = agentDTO.getIdtmatag();
        this.nomPrenom = agentDTO.getNomPrenom();
        this.typAbsc = agentDTO.getTypAbsc();
        this.typCong = agentDTO.getTypCong();
        this.sens = agentDTO.getSens();
        this.nbHeure = agentDTO.getNbHeure();
        this.nbJours = agentDTO.getNbJours();
        this.refAnnee = agentDTO.getRefAnnee();
        this.refMois = agentDTO.getRefMois();
    }

}
