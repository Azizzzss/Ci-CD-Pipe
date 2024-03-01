package tn.siga.microservice_report.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
public class KeyMap {
    private final Integer matricule;
    private final String nomPrenom;
    private final String observation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyMap keyMap)) return false;
        return Objects.equals(matricule, keyMap.matricule) && Objects.equals(nomPrenom, keyMap.nomPrenom) && Objects.equals(observation, keyMap.observation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricule, nomPrenom, observation);
    }
}
