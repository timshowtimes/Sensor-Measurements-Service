package kz.timshowtime.sensormeasurementsservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeasurementDTO {
    @NotNull(message = "Значение измерений не должно быть пустым")
    @Min(value = -100, message = "Минимальное значение не должно быть меньше -100")
    @Max(value = 100, message = "Максимальное значение не должно быть больше 100")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
    private Double value;

    @NotNull(message = "Значение дождливости не должно быть пустым")
    @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
    private Boolean raining;

    @NotNull(message = "Не указано имя сенсора, от которого пришли измерения")
    @Valid
    private SensorDTO sensor;
}
