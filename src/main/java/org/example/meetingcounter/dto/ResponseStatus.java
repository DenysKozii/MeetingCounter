package org.example.meetingcounter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
//@AllArgsConstructor
@RequiredArgsConstructor
public class ResponseStatus {
    @NonNull
    private int status;
    private String message;

    public ResponseStatus(int status,String message) {
        this.status = status;
        this.message = message;
    }
}
