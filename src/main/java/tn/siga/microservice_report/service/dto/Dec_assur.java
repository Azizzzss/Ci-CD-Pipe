package tn.siga.microservice_report.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dec_assur {

    private Integer idtmatag ;

    private String nomprenom ;

    private Integer idtmatas ;


        public Dec_assur(Dec_assurDTO decAssurDTO){
            this.idtmatag= decAssurDTO.getidtmatag();
            this.nomprenom= decAssurDTO.getnomprenom();
            this.idtmatas=decAssurDTO.getidtmatas();
        }

}
