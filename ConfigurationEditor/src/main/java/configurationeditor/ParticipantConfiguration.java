package configurationeditor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Created by SenorPez on 10/20/2016.
 */
@JsonPropertyOrder(alphabetic = true)
public class ParticipantConfiguration {
//    @JsonDeserialize(using = ParticipantConfiguration.CarDeserializer.class)
//    @JsonProperty(value = "car")
//    @JsonSerialize(using = ParticipantConfiguration.CarSerializer.class)
//    private Car car;

    @JsonProperty(value = "car")
    private String car;
    @JsonProperty(value = "display")
    private String displayName;
    @JsonProperty(value = "points")
    private Integer points;
    @JsonProperty(value = "short_display")
    private String shortDisplayName;
    @JsonProperty(value = "team")
    private String team;

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getShortDisplayName() {
        return shortDisplayName;
    }

    public void setShortDisplayName(String shortDisplayName) {
        this.shortDisplayName = shortDisplayName;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    private static class CarDeserializer extends StdDeserializer<Car> {
        public CarDeserializer() {
            this(null);
        }

        public CarDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Car deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);
            return new Car(node.asText());
        }
    }

    private static class CarSerializer extends StdSerializer<Car> {
        public CarSerializer() {
            this(null);
        }

        public CarSerializer(Class<Car> t) {
            super(t);
        }

        @Override
        public void serialize(Car value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.getCarName());
        }
    }
}
