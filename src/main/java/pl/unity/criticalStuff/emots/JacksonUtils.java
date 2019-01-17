package pl.unity.criticalStuff.emots;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

public class JacksonUtils {
	public static ObjectMapper fieldBasedMapper() {
		return fieldBasedMapper(new ObjectMapper());
	}
	
	public static ObjectMapper fieldBasedMapper(ObjectMapper objectMapper) {
		return objectMapper
			.registerModule(new Jdk8Module())
			.registerModule(new JavaTimeModule())
			.registerModule(new GuavaModule())
			.registerModule(new KotlinModule())
			.setVisibility(new ObjectMapper().getSerializationConfig().getDefaultVisibilityChecker()
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
				.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
			)
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
	
	public static final ObjectWriter PRETTY_WRITER = JacksonUtils.fieldBasedMapper().writerWithDefaultPrettyPrinter();
}
