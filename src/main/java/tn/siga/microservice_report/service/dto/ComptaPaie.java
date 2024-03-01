package tn.siga.microservice_report.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ComptaPaie {

    private Double montant;
    private Double debit;
    private Double credit;
    private Double solde;
    private Integer compte;
    private String libellet;

    public ComptaPaie (ComptaPaieDTO dto){
        this.montant = dto.getMontant();
        this.debit = (dto.getDebit() != null) ? dto.getDebit() :0D ;
        this.credit = (dto.getCredit() != null) ? dto.getCredit() :0D ;
        this.solde = (dto.getSolde() != null) ? dto.getSolde() :0D ;
        this.compte = dto.getCompte();
        this.libellet =(dto.getLibellet() != null ) ? dto.getLibellet() : " ";
    }


}
