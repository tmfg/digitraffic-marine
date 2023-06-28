package fi.livi.digitraffic.meri.domain.portnet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import jakarta.persistence.SequenceGenerator;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
public class AgentInfo {
    @Id
    @SequenceGenerator(name = "SEQ_AGENT_INFO", sequenceName = "SEQ_AGENT_INFO", allocationSize = 1)
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
