package br.com.neurotech.challenge.dtos;

import org.springframework.hateoas.RepresentationModel;

public class CreditCheckResponseDto extends RepresentationModel<CreditCheckResponseDto> {

    private Long clientId;
    private String clientName;
    private String vehicleModel;
    private boolean isEligible;
    private String message;

    // Construtor padrão
    public CreditCheckResponseDto() {}

    // Construtor com todos os campos
    public CreditCheckResponseDto(Long clientId, String clientName, String vehicleModel, boolean isEligible, String message) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.vehicleModel = vehicleModel;
        this.isEligible = isEligible;
        this.message = message;
    }

    // Getters e Setters
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public boolean isEligible() {
        return isEligible;
    }

    public void setEligible(boolean eligible) {
        isEligible = eligible;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CreditCheckResponseDto{" +
                "clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", isEligible=" + isEligible +
                ", message='" + message + '\'' +
                '}';
    }
}
