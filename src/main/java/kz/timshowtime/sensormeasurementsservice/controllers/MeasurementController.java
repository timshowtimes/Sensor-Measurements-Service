package kz.timshowtime.sensormeasurementsservice.controllers;

import jakarta.validation.Valid;
import kz.timshowtime.sensormeasurementsservice.dto.MeasurementDTO;
import kz.timshowtime.sensormeasurementsservice.models.Measurement;
import kz.timshowtime.sensormeasurementsservice.models.Sensor;
import kz.timshowtime.sensormeasurementsservice.services.MeasurementService;
import kz.timshowtime.sensormeasurementsservice.services.SensorService;
import kz.timshowtime.sensormeasurementsservice.util.ErrorMessageBuilder;
import kz.timshowtime.sensormeasurementsservice.util.ErrorResponse;
import kz.timshowtime.sensormeasurementsservice.util.MeasureNotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {
    private final SensorService sensorService;
    private final MeasurementService measurementService;
    private final ErrorMessageBuilder errorMessageBuilder;

    @Autowired
    public MeasurementController(SensorService sensorService, MeasurementService measurementService,
                                 ErrorMessageBuilder errorMessageBuilder) {
        this.sensorService = sensorService;
        this.measurementService = measurementService;
        this.errorMessageBuilder = errorMessageBuilder;
    }

    @PostMapping("/build-graphic")
    public void saveGraphPng(@RequestBody List<MeasurementDTO> measurementDTO) throws IOException {
        measurementService.createTempGraph(measurementDTO);
    }

    @GetMapping()
    public List<MeasurementDTO> index() {
        return measurementService.findAll()
                .stream().map(measurementService::converToMeasurementDTO)
                .toList();
    }

    @GetMapping("/rainyDaysCount")
    public long rainyDaysCount() {
        return measurementService.findAll()
                .stream()
                .filter(Measurement::getRaining)
                .count();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO,
                                          BindingResult bindingResult) {
        Sensor sensorNoId = sensorService.convertToSensor(measurementDTO.getSensor());
        Optional<Sensor> hasSensor = sensorService.findByNameIgnoreCase(sensorNoId.getName());
        if (hasSensor.isEmpty()) {
            bindingResult.rejectValue("sensor", "",
                    "Введен несуществующий сенсор");
        }
        if (bindingResult.hasErrors()) {
            String errorMessage = errorMessageBuilder.of(bindingResult);
            throw new MeasureNotCreatedException(errorMessage);
        }
        measurementService.save(
                measurementService.convertToMeasurement(measurementDTO),
                // TODO: save original sensor with ID,
                //  100% not null because skip a throwing exception
                hasSensor.get()
        );
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(MeasureNotCreatedException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
