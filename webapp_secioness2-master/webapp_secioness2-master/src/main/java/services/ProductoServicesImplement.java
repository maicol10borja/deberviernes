package services;

import model.Productos;

import java.util.Arrays;
import java.util.List;

public class ProductoServicesImplement implements ProductoService{

    @Override
    public List<Productos> list() {
        return Arrays.asList(new Productos(1L, "laptop", "tecnología", 650.25),
                new Productos(2L, "cocina","hogar", 452.15),
                new Productos(3L, "cama", "dormitorio",254.20));
    }

}
