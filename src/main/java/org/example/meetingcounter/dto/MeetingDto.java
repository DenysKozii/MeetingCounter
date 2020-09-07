package org.example.meetingcounter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public MeetingDto(String title, Long hereAmount){
        this.title = title;
        this.hereAmount = hereAmount;
    }
}
