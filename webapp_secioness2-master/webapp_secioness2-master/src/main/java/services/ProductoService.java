package services;

import model.Productos;

import java.util.List;

public interface ProductoService  {
    //Implementamos un método para listar los productos
    List<Productos> list();
}
