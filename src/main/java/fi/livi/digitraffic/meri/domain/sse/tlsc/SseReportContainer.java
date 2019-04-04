package fi.livi.digitraffic.meri.domain.sse.tlsc;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonCreator;

import fi.livi.digitraffic.meri.config.postgres.SSEReportUserType;
import fi.livi.digitraffic.meri.util.StringUtil;

@TypeDef(name = "SSEReportUserType", typeClass = SSEReportUserType.class)
@Entity
@DynamicUpdate
@Table(name = "SSE_REPORT_CONTAINER")
public class SseReportContainer {

    @Id
    @GenericGenerator(name = "SEQ_SSE_REPORT_CONTAINER", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
                      parameters = @Parameter(name = "sequence_name", value = "SEQ_SSE_REPORT_CONTAINER"))
    @GeneratedValue(generator = "SEQ_SSE_REPORT_CONTAINER")
    private Long id;


    @Column
    private ZonedDateTime created;

    @Column
    private ZonedDateTime handled;

    @Column
    @Type(type = "SSEReportUserType")
    private SseReport report;

    public SseReportContainer() {
    }

    public SseReportContainer(final SseReport report) {
        this.report = report;
        created = ZonedDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setReport(SseReport report) {
        this.report = report;
    }

    public SseReport getReport() {
        return report;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setHandled(ZonedDateTime handled) {
        this.handled = handled;
    }

    public ZonedDateTime getHandled() {
        return handled;
    }

    @Override
    public String toString() {
        return StringUtil.toJsonString(this);
    }



}
