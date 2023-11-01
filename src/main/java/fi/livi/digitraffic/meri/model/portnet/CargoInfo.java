package fi.livi.digitraffic.meri.model.portnet;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@DynamicInsert
public class CargoInfo {
    @Id
    @SequenceGenerator(name = "SEQ_CARGO_INFO", sequenceName = "SEQ_CARGO_INFO", allocationSize = 1)
    @GeneratedValue(generator = "SEQ_CARGO_INFO")
    private Long cargoInfoId;

    private Integer cargoDischargeCode;
    private String cargoDescription;
    private BigDecimal cargoAmount;

    public Long getCargoInfoId() {
        return cargoInfoId;
    }

    public void setCargoInfoId(final Long cargoInfoId) {
        this.cargoInfoId = cargoInfoId;
    }

    public Integer getCargoDischargeCode() {
        return cargoDischargeCode;
    }

    public void setCargoDischargeCode(final Integer cargoDischargeCode) {
        this.cargoDischargeCode = cargoDischargeCode;
    }

    public String getCargoDescription() {
        return cargoDescription;
    }

    public void setCargoDescription(final String cargoDescription) {
        this.cargoDescription = cargoDescription;
    }

    public BigDecimal getCargoAmount() {
        return cargoAmount;
    }

    public void setCargoAmount(final BigDecimal cargoAmount) {
        this.cargoAmount = cargoAmount;
    }

    public boolean isNotEmpty() {
        return cargoDischargeCode != null && StringUtils.isNotEmpty(cargoDescription) && cargoAmount != null;
    }
}
