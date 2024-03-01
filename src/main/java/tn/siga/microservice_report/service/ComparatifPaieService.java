package tn.siga.microservice_report.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.siga.microservice_report.repository.IdtagentRepository;
import tn.siga.microservice_report.service.dto.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComparatifPaieService {

    private final Logger log = LoggerFactory.getLogger(DiffNetRepService.class);
    private final IdtagentRepository idtagentRepository;

    public ComparatifPaieService(IdtagentRepository idtagentRepository) {
        this.idtagentRepository = idtagentRepository;
    }

    public Map<KeyMap, List<ComparatifPaieDTO>> groupByMultipleAttributes(List<ComparatifPaieDTO> comparatifPaieDTOList)
    {
        return comparatifPaieDTOList.stream()
                .collect(Collectors.groupingBy(
                        dto -> new KeyMap(dto.getMatricule(), dto.getNomPrenom(), dto.getObservation())
                    ));
    }

    public List<ComparatifPaieResult> getComparatif_Paie(Integer p_annee, Integer p_mois, Integer p_annee2, Integer p_mois2) {
        List<ComparatifPaieDTO> comparatifPaieDTOList = idtagentRepository.getComparatifPaie(p_annee, p_mois, p_annee2, p_mois2);

        log.info("group by ..");
        Map<KeyMap, List<ComparatifPaieDTO>> objects= this.groupByMultipleAttributes(comparatifPaieDTOList);
        log.info("restreaming objects");
     return   objects.keySet().stream().map(key -> new ComparatifPaieResult(key,objects.get(key)))
             .sorted(Comparator.comparing(ComparatifPaieResult::getMatricule))
             .toList();


/*
        List<ComparatifPaieDTO> details = comparatifPaieDTOList.stream().filter(comparatifPaieDTO -> Objects.equals(comparatifPaieDTO.getMatricule(), 6758)).toList();

        log.info("sikze test {} {}",6758,details.size());
        List<Integer> distinctMatricules = comparatifPaieDTOList.stream()
                .map(ComparatifPaieDTO::getMatricule)
                .distinct().toList();
        log.info("distinctMatricules {} ", distinctMatricules.size());
        // distinct value of matricule
          return  distinctMatricules.stream().map(idtmatag -> this.createComparatif(idtmatag,comparatifPaieDTOList)).toList();
*/
    }

   /* private ComparatifPaieResult createComparatif (Integer idtMatag,  List<ComparatifPaieDTO> comparatifPaieDTOList){
        ComparatifPaieResult result = new ComparatifPaieResult();
        *//*init ojbect grouped by*//*
        // filter result by idtmatag
        List<ComparatifPaieDTO> details = comparatifPaieDTOList.stream().filter(comparatifPaieDTO -> Objects.equals(comparatifPaieDTO.getMatricule(), idtMatag)).toList();

            ComparatifPaieDTO firstDetail = details.get(0);
            result.setMatricule(firstDetail.getMatricule());
            result.setNomPrenom(firstDetail.getNomPrenom());
            result.setObservation(firstDetail.getObservation());
            result.setIndemnityDetails(details);

        return result;
    }*/


}


