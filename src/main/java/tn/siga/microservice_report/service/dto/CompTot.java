package tn.siga.microservice_report.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompTot {


    private Double somPeriod1;
    private Double somPeriod2;
    private Integer nbrAgent1;
    private Integer nbrAgent2;
    private Double  ecart;
    private Integer code;
    private String indemDsg;


    public CompTot (CompTotDTO dto){
        this.code= dto.getIndemCode();
        this.somPeriod1 = (dto.getMntPeriode1() != null) ? dto.getMntPeriode1() : 0D;
        this.somPeriod2 = (dto.getMntPeriode2() != null) ? dto.getMntPeriode2() : 0D;
        this.ecart = dto.getEcart();
        this.nbrAgent1 = dto.getNbrAgentPeriode1();
        this.nbrAgent2 = dto.getNbrAgentPeriode2();
        this. indemDsg = dto.getLibIndem();
    }

}
