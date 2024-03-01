package tn.siga.microservice_report.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recap_virement {

         private Integer bnkcode ;

         private String bnkdsg ;

         private String ribbng ;

         private Double montant ;

         private Integer  nb ;

         public Recap_virement(Recap_virementDTO re_vir_dto){

             this.bnkcode = re_vir_dto.getbnkcode();
             this.bnkdsg = re_vir_dto.getbnkdsg();
             this.ribbng = re_vir_dto.getribbnq();
             this.montant = re_vir_dto.getmontant() ;
             this.nb=re_vir_dto.getnb();

         }


}
