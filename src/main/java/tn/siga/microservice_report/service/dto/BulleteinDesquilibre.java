package tn.siga.microservice_report.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BulleteinDesquilibre {

    private Integer idtmatag;

    private Double montant;

    private Double credit;

    private Double difference;

    public BulleteinDesquilibre(DifNetDTO dto){
        this.idtmatag=dto.getIdtmatag();
        this.montant=dto.getMnt();
    }
}
