package tn.siga.microservice_report.service.dto;

public interface ComptaPaieDTO {
    public Double getMontant();
    public Integer getCompte();
    public String getLibellet();
    public Double getDebit();
    public Double getCredit();
    public Double getSolde();
}
