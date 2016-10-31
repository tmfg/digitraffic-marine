package fi.livi.digitraffic.meri.domain.portnet;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@DynamicInsert
public class CargoInfo {
    @Id
    @GenericGenerator(name = "SEQ_CARGO_INFO", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = @Parameter(name = "sequence_name", value = "SEQ_CARGO_INFO"))
    @GeneratedValue(generator = "SEQ_CARGO_INFO")

    private long cargoInfoId;

    private Integer cargoDischargeCode;
    private String cargoDescription;
    private BigDecimal cargoAmount;

    public long getCargoInfoId() {
        return cargoInfoId;
    }

    public void setCargoInfoId(long cargoInfoId) {
        this.cargoInfoId = cargoInfoId;
    }

    public Integer getCargoDischargeCode() {
        return cargoDischargeCode;
    }

    public void setCargoDischargeCode(Integer cargoDischargeCode) {
        this.cargoDischargeCode = cargoDischargeCode;
    }

    public String getCargoDescription() {
        return cargoDescription;
    }

    public void setCargoDescription(String cargoDescription) {
        this.cargoDescription = cargoDescription;
    }

    public BigDecimal getCargoAmount() {
        return cargoAmount;
    }

    public void setCargoAmount(BigDecimal cargoAmount) {
        this.cargoAmount = cargoAmount;
    }

    public boolean isNotEmpty() {
        return cargoDischargeCode != null && StringUtils.isNotEmpty(cargoDescription) && cargoAmount != null;
    }
}
