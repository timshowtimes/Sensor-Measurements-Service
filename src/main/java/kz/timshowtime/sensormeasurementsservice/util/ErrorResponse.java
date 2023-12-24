package kz.timshowtime.sensormeasurementsservice.util;


import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private String timestamp;

    public ErrorResponse(String message, Date timestamp) {
        this.message = message;
        this.timestamp = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss").format(timestamp);
    }
}
