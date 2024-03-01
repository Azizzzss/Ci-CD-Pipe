package tn.siga.microservice_report.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ComparatifPaieDetails {
    private Integer matricule;
    private String nomPrenom;
    private String observation;
    private Integer indemCode;
    private String libIndem;
    private Double somPeriod1;
    private Double somPeriod2;
    private Double ecart;
    private List<ComparatifPaieDTO> indemnityDetails;


    public ComparatifPaieDetails(ComparatifPaieResult result, ComparatifPaieDTO dto){
        this.matricule = result.getMatricule();
        this.nomPrenom = result.getNomPrenom();
        this.observation = result.getObservation();
        this.indemCode = result.getIndemCode();
        this.libIndem = result.getLibIndem();
        this.somPeriod1 = result.getSomPeriod1();
        this.somPeriod2 = result.getSomPeriod2();
        this.ecart = result.getEcart();
      //  this.indemnityDetails = result.getIndemnityDetails();

    }

}
