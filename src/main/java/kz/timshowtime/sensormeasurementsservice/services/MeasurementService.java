package kz.timshowtime.sensormeasurementsservice.services;

import kz.timshowtime.sensormeasurementsservice.dto.MeasurementDTO;
import kz.timshowtime.sensormeasurementsservice.models.Measurement;
import kz.timshowtime.sensormeasurementsservice.models.Sensor;
import kz.timshowtime.sensormeasurementsservice.repositories.MeasurementRepo;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {
    private final MeasurementRepo measurementRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementService(MeasurementRepo measurementRepo, ModelMapper modelMapper) {
        this.measurementRepo = measurementRepo;
        this.modelMapper = modelMapper;
    }

    public List<Measurement> findAll() {
        return measurementRepo.findAll();
    }

    public Measurement findOne(int id) {
        return measurementRepo.findById(id).orElse(null);
    }

    @Transactional
    public void save(Measurement measurement, Sensor sensor) {
        measurement.setSensor(sensor);
        measurement.setTimestamp(LocalDateTime.now());
        measurementRepo.save(measurement);
    }

    public Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    public MeasurementDTO converToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    public void createTempGraph(List<MeasurementDTO> measurementDTOs) throws IOException {
        List<Double> temps = measurementDTOs.stream().map(MeasurementDTO::getValue).toList();
        double[] xData = new double[temps.size()];
        double[] yData = new double[temps.size()];

        for (int i = 0; i < temps.size(); i++) {
            xData[i] = i;
            yData[i] = temps.get(i);
        }

        XYChart chart = new XYChartBuilder().width(600).height(400)
                .title("График температуры")
                .xAxisTitle("Измерения сенсора")
                .yAxisTitle("Температура")
                .build();

        XYSeries series = chart.addSeries("Темпа", xData, yData);

        setGraphStyles(chart, series);

        String path = "C:\\JavaProjects\\SensorMeasurementsConsumer\\src" +
                "\\main\\resources\\static\\graphChart\\chart.png";
        BitmapEncoder.saveBitmap(chart, path, BitmapEncoder.BitmapFormat.PNG);
    }

    private void setGraphStyles(XYChart chart, XYSeries series) {
        // Цвет линии графика
        series.setLineColor(Color.decode("#E6C105"));
        // Цвет точек на графике
        series.setMarkerColor(Color.decode("#FFDAB9"));
        // Цвет фона графика
        chart.getStyler().setPlotBackgroundColor(Color.decode("#110717"));
        // Цвет краев графика
        chart.getStyler().setChartBackgroundColor(Color.decode("#39393966"));
        // Цвет сетки внутри графика
        chart.getStyler().setPlotGridLinesColor(Color.decode("#3C1254"));
        // Цвет слов
        chart.getStyler().setChartFontColor(Color.WHITE);
        // Цвет легенды (прямоугольник справа)
        chart.getStyler().setLegendBackgroundColor(Color.decode("#110717"));
        // Цвет чисел по осям X и Y
        chart.getStyler().setXAxisTickLabelsColor(Color.decode("#FFDAB9"));
        chart.getStyler().setYAxisTickLabelsColor(Color.decode("#FFDAB9"));
    }
}
