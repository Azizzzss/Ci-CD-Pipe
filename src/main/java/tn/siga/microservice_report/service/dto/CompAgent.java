package tn.siga.microservice_report.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompAgent {



    private Integer idtmatag;
    private Double somPeriod1;
    private Double somPeriod2;
    private Integer annee;
    private Integer mois;
    private Integer annee2;
    private Integer mois2;
    private Integer code;
    private String indemDsg;
    private String color;


    public CompAgent (CompAgentDTO dto){
        this.code= dto.getIndemCode();
        this.somPeriod1 = 0D;
        this.somPeriod2 = 0D;
        this. indemDsg = "";
        this.color = "";
    }


}
