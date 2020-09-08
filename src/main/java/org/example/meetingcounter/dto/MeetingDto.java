package org.example.meetingcounter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MeetingDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    private String title;

    private Long hereAmount;

    private Double longitude;

    private Double latitude;

    public MeetingDto(String title, Long hereAmount, Double longitude, Double latitude) {
        this.title = title;
        this.hereAmount = hereAmount;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
