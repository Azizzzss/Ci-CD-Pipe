package tn.siga.microservice_report.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BulletinNegatif {

    private Integer idtmatag;

    private String nomPrenom;

    private Double montant;

    private String corpAgent;

    public BulletinNegatif(DifNetDTO dto){
        this.idtmatag=dto.getIdtmatag();
        this.montant=dto.getMnt();
        this.corpAgent="";
        this.nomPrenom="";
    }

}
