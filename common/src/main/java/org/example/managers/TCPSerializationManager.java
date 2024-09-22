package org.example.managers;

import com.google.gson.*;
import org.example.TCP_components.Request;
import org.example.TCP_components.Response;
import org.example.model.Person;
import org.example.user.User;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TCPSerializationManager {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public String serialize(Serializable a){
        return gson.toJson(a);
    }
    public Response response(String stringResponse) throws JsonSyntaxException {
        return gson.fromJson(stringResponse,Response.class);
    }
    public Request request(String stringRequest) throws JsonSyntaxException {
        return gson.fromJson(stringRequest,Request.class);
    }
    public Person person(String stringPerson) throws JsonSyntaxException {
        return gson.fromJson(stringPerson,Person.class);
    }
    public User user(String stringUser) throws JsonSyntaxException{
        return gson.fromJson(stringUser,User.class);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime date, Type typeOfSrc,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); // "yyyy-mm-dd"
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type type,
                                         JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString());
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
