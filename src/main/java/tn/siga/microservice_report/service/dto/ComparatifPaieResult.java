package tn.siga.microservice_report.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ComparatifPaieResult {

    private Integer matricule;
    private String nomPrenom;
    private String observation;
    private Integer indemCode;
    private String libIndem;
    private Double somPeriod1;
    private Double somPeriod2;
    private Double ecart;
    //private List<ComparatifPaieResult> indemnityDetails;
    //collection
    private JRBeanCollectionDataSource indemnityDetails;

    public ComparatifPaieResult(KeyMap key, List<ComparatifPaieDTO> indemnityDetails){
        // Convert List to JRBeanCollectionDataSource
        this.indemnityDetails = new JRBeanCollectionDataSource(indemnityDetails);
        this.matricule= key.getMatricule();
        this.nomPrenom= key.getNomPrenom();
        this.observation= key.getObservation();

    }

}
