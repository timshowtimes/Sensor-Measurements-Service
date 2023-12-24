package kz.timshowtime.sensormeasurementsservice.util;

import kz.timshowtime.sensormeasurementsservice.models.Sensor;
import kz.timshowtime.sensormeasurementsservice.services.SensorService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@Getter
@Setter
public class SensorNameValidator implements Validator {
    private final SensorService sensorService;

    @Autowired
    public SensorNameValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sensor sensor = (Sensor) target;

        if (sensorService.findByNameIgnoreCase(sensor.getName()).isPresent()) {
            errors.rejectValue("name", "",
                    "Сенсор с таким именем уже существует");
        }
    }
}
