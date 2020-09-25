package com.denyskozii.meetingcounter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ResponseStatus {
    @NonNull
    private int status;
    private String message;
}
