package kz.timshowtime.sensormeasurementsservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorDTO {

    @NotEmpty(message = "Имя сенсора не должно быть пустым")
    @Size(min = 3, max = 30,
            message = "Имя сенсора должно быть в диапозоне между 3 и 30 символами")
    private String name;
}
