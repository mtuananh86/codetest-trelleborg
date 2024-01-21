package org.trelleborg.travel.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.trelleborg.travel.util.HashUtil;

import java.io.IOException;

public class PanSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String s,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(HashUtil.hash(s));
    }
}
