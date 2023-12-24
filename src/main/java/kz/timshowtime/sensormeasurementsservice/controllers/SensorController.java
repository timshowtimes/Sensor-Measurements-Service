package kz.timshowtime.sensormeasurementsservice.controllers;

import jakarta.validation.Valid;
import kz.timshowtime.sensormeasurementsservice.dto.SensorDTO;
import kz.timshowtime.sensormeasurementsservice.models.Sensor;
import kz.timshowtime.sensormeasurementsservice.services.SensorService;
import kz.timshowtime.sensormeasurementsservice.util.ErrorMessageBuilder;
import kz.timshowtime.sensormeasurementsservice.util.ErrorResponse;
import kz.timshowtime.sensormeasurementsservice.util.SensorNameValidator;
import kz.timshowtime.sensormeasurementsservice.util.SensorNotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/sensors")
public class SensorController {
    private final SensorService sensorService;
    private final SensorNameValidator sensorNameValidator;

    private final ErrorMessageBuilder errorMessageBuilder;

    @Autowired
    public SensorController(SensorService sensorService, SensorNameValidator sensorNameValidator,
                            ErrorMessageBuilder errorMessageBuilder) {
        this.sensorService = sensorService;
        this.sensorNameValidator = sensorNameValidator;
        this.errorMessageBuilder = errorMessageBuilder;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid SensorDTO sensorDTO,
                                             BindingResult bindingResult) {
        Sensor sensor = sensorService.convertToSensor(sensorDTO);
        sensorNameValidator.validate(sensor, bindingResult);
        if (bindingResult.hasErrors()) {
            String errorMessage = errorMessageBuilder.of(bindingResult);
            throw new SensorNotCreatedException(errorMessage);
        }

        sensorService.save(sensor);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(SensorNotCreatedException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                new Date()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
