package fi.livi.digitraffic.meri.domain.portnet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@DynamicInsert
public class AgentInfo {
    @Id
    @GenericGenerator(name = "SEQ_AGENT_INFO", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = @Parameter(name = "sequence_name", value = "SEQ_AGENT_INFO"))
    @GeneratedValue(generator = "SEQ_AGENT_INFO")
    private Long agentInfoId;

    private Integer role;
    private String portCallDirection;
    private String name;
    private String ediNumber;

    public Long getAgentInfoId() {
        return agentInfoId;
    }

    public void setAgentInfoId(final Long agentInfoId) {
        this.agentInfoId = agentInfoId;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(final Integer role) {
        this.role = role;
    }

    public String getPortCallDirection() {
        return portCallDirection;
    }

    public void setPortCallDirection(final String portCallDirection) {
        this.portCallDirection = portCallDirection;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEdiNumber() {
        return ediNumber;
    }

    public void setEdiNumber(final String ediNumber) {
        this.ediNumber = ediNumber;
    }
}
