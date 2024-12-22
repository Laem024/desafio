package com.aluracursos.desafio.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return mapper.readValue(json, clase);
        }catch (JsonProcessingException e){
            System.out.println("Error al obtener datos del JSON");
            throw new RuntimeException(e);
        }
    }
}
