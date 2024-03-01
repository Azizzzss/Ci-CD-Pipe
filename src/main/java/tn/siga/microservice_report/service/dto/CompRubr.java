package tn.siga.microservice_report.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompRubr {
    private Integer idtmatag;
    private String nomPrenom;
    private Double somPeriod1;
    private Double somPeriod2;
    private Double ecartValue;
    private String corpAgent;

    private Integer annee;
    private Integer mois;
    private Integer annee1;
    private Integer mois1;
    private Integer code;
    public CompRubr(CompRubrDTO dto){
        this.idtmatag=dto.getIdtmatag();
        this.somPeriod1=0D;
        this.somPeriod2=0D;
        this.ecartValue=0D;
        this.corpAgent="";
        this.nomPrenom="";
    }


}
