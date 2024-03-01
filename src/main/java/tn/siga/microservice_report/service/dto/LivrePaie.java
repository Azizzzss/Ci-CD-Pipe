package tn.siga.microservice_report.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LivrePaie {

    private String famille;
    private Double indcode;
    private Double nmmcode;
    private String inddsg;
    private String ddt;
    private Double montant;

    public LivrePaie(LivrePaieDTO livdto) {

        this.famille= livdto.getFamille();
        this.indcode=livdto.getIndcode();
        this.nmmcode=livdto.getNmmcode();
        this.inddsg=livdto.getInddsg();
        this.ddt= livdto.getDdt();
        this.montant= livdto.getMontant();

    }



}
