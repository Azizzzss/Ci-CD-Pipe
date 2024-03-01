package tn.siga.microservice_report.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Diff_agent {


        private String nomPrenom ;
        private String matricule ;

        public Diff_agent(Diff_agentDTO diffAgentDTO){
            this.nomPrenom = diffAgentDTO.getnomPrenom();
            this.matricule= diffAgentDTO.getmatricule();
        }

}
