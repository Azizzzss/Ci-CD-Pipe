package tn.siga.microservice_report.service.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Paie_gen {


    private Integer idtmatag;
    private String nomprenom;
    private String idtcategorie;
    private String idtposte;
    private Integer idtchocode;
    private Integer idtempcode;
    private String idtdatrec;
    private String idtdatnais;
    private String ufa;
    private Integer indcode;
    private Integer montant;

    public Paie_gen(Paie_genDto paieGenDto){
        this.idtmatag=paieGenDto.getidtmatag();
        this.nomprenom=paieGenDto.getnomprenom();
        this.idtcategorie=paieGenDto.getidtcategorie();
        this.idtposte=paieGenDto.getidtposte();
        this.idtchocode=paieGenDto.getidtchocode();
        this.idtempcode=paieGenDto.getidtempcode();
        this.idtdatrec=paieGenDto.getidtdatrec();
        this.idtdatnais=paieGenDto.getidtdatnais();
        this.ufa=paieGenDto.getufa();
        this.indcode=paieGenDto.getindcode();
        this.montant=paieGenDto.getmontant();
    }


}
