package kz.timshowtime.sensormeasurementsservice.services;

import kz.timshowtime.sensormeasurementsservice.dto.SensorDTO;
import kz.timshowtime.sensormeasurementsservice.models.Sensor;
import kz.timshowtime.sensormeasurementsservice.repositories.SensorRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepo sensorRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorService(SensorRepo sensorRepo, ModelMapper modelMapper) {
        this.sensorRepo = sensorRepo;
        this.modelMapper = modelMapper;
    }

    public List<Sensor> findAll() {
        return sensorRepo.findAll();
    }

    public Optional<Sensor> findOne(int id) {
        return sensorRepo.findById(id);
    }

    @Transactional
    public void save(Sensor sensor) {
        sensorRepo.save(sensor);
    }

    public Optional<Sensor> findByNameIgnoreCase(String name) {
        return sensorRepo.findByNameIgnoreCase(name);
    }

    public Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    public SensorDTO converToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }
}
