package com.aluracursos.desafio.principal;

import com.aluracursos.desafio.model.Datos;
import com.aluracursos.desafio.model.DatosLibros;
import com.aluracursos.desafio.service.ConsumoAPI;
import com.aluracursos.desafio.service.ConvierteDatos;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner scanner = new Scanner(System.in);

    public void muestraElMenu(){
        var json = consumoAPI.optenerDatos(URL_BASE);
        System.out.println(json);

        var datos = conversor.obtenerDatos(json, Datos.class);

        System.out.println(datos);

        //Top 10 libros mas descargados
        System.out.println("Top 10 libros mas descargados");
        datos.resultadosLibros().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);

        //Busqueda de libros por nombre
        System.out.println("Ingrese el nombre del libro a buscar");
        var tituloLibro = scanner.nextLine();
        json = consumoAPI.optenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBusqueda = datosBusqueda.resultadosLibros().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if (libroBusqueda.isPresent()) {
            System.out.println("Libro Encontrado ");
            System.out.println(libroBusqueda.get());
        }else {
            System.out.println("No se encontro el libro");
        }

        //Trabajando con estadisticas
        DoubleSummaryStatistics est = datos.resultadosLibros().stream()
                .filter(d -> d.numeroDeDescargas() > 0)
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));

        System.out.println("Cantidad media de descargas: " + est.getAverage());
        System.out.println("cantidad maxima de descargas: " + est.getMax());
        System.out.println("cantidad minima de descargas: " + est.getMin());
        System.out.println("La cantidad de registros evaluados: " + est.getCount());
    }
}
