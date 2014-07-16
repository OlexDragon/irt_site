package irt.data;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Jackson {

	private static final ObjectMapper mapper = new ObjectMapper();

	public static String objectToJsonString(Object o) throws JsonGenerationException, JsonMappingException, IOException{
		return mapper.writeValueAsString(o);
	}

	public static <T extends Object> T jsonStringToObject(Class<T> clss, String jsonStr) throws JsonParseException, JsonMappingException, IOException {
		return jsonStr!=null ? mapper.readValue(jsonStr, clss) : null;
	}
}
