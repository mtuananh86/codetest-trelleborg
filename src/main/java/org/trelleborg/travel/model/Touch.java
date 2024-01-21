package org.trelleborg.travel.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.trelleborg.travel.util.json.PanSerializer;

import java.time.Instant;
import static org.trelleborg.travel.Constants.DateTime;

import static org.trelleborg.travel.Constants.Fields.*;

@Data
@Builder(toBuilder = true)
@With
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
    IdField,
    DateTimeUTCField,
    TouchTypeField,
    StopIdField,
    CompanyIdField,
    BusIdField,
    PanField
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Touch {

    @JsonProperty(IdField)
    private String id;

    @JsonProperty(DateTimeUTCField)
    @JsonFormat(pattern = DateTime.PATTERN, timezone = "UTC")
    private Instant dateTime;

    @JsonProperty(TouchTypeField)
    private TouchType touchType;

    @JsonProperty(StopIdField)
    private StopPoint stopId;

    @JsonProperty(CompanyIdField)
    private String companyId;

    @JsonProperty(BusIdField)
    private String busId;

    @JsonProperty(PanField)
    //@JsonSerialize(using = PanSerializer.class, as=String.class)
    private String pan;

    @JsonProperty(StatusField)
    private String status;

    public boolean isOn() {
        return touchType == TouchType.ON;
    }

    public boolean isOff() {
        return touchType == TouchType.OFF;
    }

}
