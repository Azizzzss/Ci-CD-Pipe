package tn.siga.microservice_report.service.dto;

public interface ComparatifPaieDTO {

    public Integer getMatricule();
    public String getNomPrenom();
    public String getObservation();
    public Integer getIndemCode();
    public String getLibIndem();
    public Double getMntPeriode1();
    public Double getMntPeriode2();
    public Double getEcart();
}
