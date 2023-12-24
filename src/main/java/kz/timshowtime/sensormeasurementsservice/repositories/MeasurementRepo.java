package kz.timshowtime.sensormeasurementsservice.repositories;

import kz.timshowtime.sensormeasurementsservice.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepo extends JpaRepository<Measurement, Integer> {
}
