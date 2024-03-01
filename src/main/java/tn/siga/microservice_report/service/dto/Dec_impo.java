package tn.siga.microservice_report.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dec_impo {


    public Integer idtmatag ;

    public  String nomprenom;

    public Integer idtmatas ;

        public Dec_impo(Dec_impoDTO decImpoDTO){
            this.idtmatag=decImpoDTO.getIdtmatag();
            this.nomprenom=decImpoDTO.getNomprenom();
            this.idtmatag=decImpoDTO.getIdtmatas();
        }
}
