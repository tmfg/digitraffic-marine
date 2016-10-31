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
    private long agentInfoId;

    private Integer role;
    private String portCallDirection;
    private String name;
    private String editNumber;

    public long getAgentInfoId() {
        return agentInfoId;
    }

    public void setAgentInfoId(long agentInfoId) {
        this.agentInfoId = agentInfoId;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getPortCallDirection() {
        return portCallDirection;
    }

    public void setPortCallDirection(String portCallDirection) {
        this.portCallDirection = portCallDirection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEditNumber() {
        return editNumber;
    }

    public void setEditNumber(String editNumber) {
        this.editNumber = editNumber;
    }
}
