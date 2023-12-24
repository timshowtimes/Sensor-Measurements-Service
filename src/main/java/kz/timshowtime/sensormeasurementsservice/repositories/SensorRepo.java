package kz.timshowtime.sensormeasurementsservice.repositories;

import kz.timshowtime.sensormeasurementsservice.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorRepo extends JpaRepository<Sensor, Integer> {
    Optional<Sensor> findByNameIgnoreCase(String name);
}
