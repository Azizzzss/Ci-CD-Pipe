package tn.siga.microservice_report.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LivrePaieAff {

    private Integer idtmatag;
    private Integer ordre;
    private Integer indcode;
    private String np;
    private String inddsg;
    private Double montant;

    public LivrePaieAff(LivrePaieAffDto livpdto) {
        this.idtmatag= livpdto.getIdtmatag();
        this.ordre= livpdto.getOrdre();
        this.indcode= livpdto.getIndcode();
        this.np=livpdto.getNp();
        this.inddsg=livpdto.getInddsg();
        this.montant=livpdto.getMontant();
    }
}
