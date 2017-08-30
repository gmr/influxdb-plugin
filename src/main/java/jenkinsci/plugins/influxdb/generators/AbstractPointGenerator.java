package jenkinsci.plugins.influxdb.generators;

import hudson.model.Run;
import jenkinsci.plugins.influxdb.renderer.MeasurementRenderer;
import org.influxdb.dto.Point;

import java.util.Objects;

public abstract class AbstractPointGenerator implements PointGenerator {

    public static final String PROJECT_NAME = "project_name";
    public static final String BUILD_NUMBER = "build_number";

    private MeasurementRenderer projectNameRenderer;

    public AbstractPointGenerator(MeasurementRenderer projectNameRenderer) {
        this.projectNameRenderer = Objects.requireNonNull(projectNameRenderer);
    }

    @Override
    public Point.Builder buildPoint(String name, String customPrefix, Run<?, ?> build) {
        final String renderedProjectName = projectNameRenderer.render(build);
        return Point
                .measurement(name)
                .tag(PROJECT_NAME, renderedProjectName)
                .addField(BUILD_NUMBER, build.getNumber());
    }

    protected String measurementName(String measurement) {
        //influx discourages "-" in measurement names.
        return measurement.replaceAll("-", "_");
    }


}
