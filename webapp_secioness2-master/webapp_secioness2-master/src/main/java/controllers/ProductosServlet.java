package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Productos;
import services.LoginService;
import services.LoginServiceSessionImplement;
import services.ProductoService;
import services.ProductoServicesImplement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet("/productos")
public class ProductosServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Obtener cookies, manejando el caso donde no haya cookies
        Cookie[] cookies = req.getCookies() != null ? req.getCookies() : new Cookie[0];

        // Buscar una cookie con el nombre "username"
        Optional<String> cookieOptional = Arrays.stream(cookies)
                .filter(c -> "username".equals(c.getName()))
                .map(Cookie::getValue)
                .findAny();

        if (cookieOptional.isPresent()) {
            // Si el usuario está autenticado, mostrar productos
            ProductoService productoService = new ProductoServicesImplement();
            List<Productos> productos = productoService.list();

            resp.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = resp.getWriter()) {
                out.print("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset=\"utf-8\">");
                out.println("<title>Lista de Productos</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Lista de Productos</h1>");
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Nombre</th><th>Categoría</th><th>Precio</th></tr>");
                for (Productos producto : productos) {
                    out.println("<tr>");
                    out.println("<td>" + producto.getIdProducto() + "</td>");
                    out.println("<td>" + producto.getNombreProducto() + "</td>");
                    out.println("<td>" + producto.getCategoria() + "</td>");
                    out.println("<td>" + producto.getPrecioProducto() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                out.println("<p><a href='" + req.getContextPath() + "/logout'>Cerrar sesión</a></p>");
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            // Si el usuario no está autenticado, redirigir al login
            resp.sendRedirect(req.getContextPath() + "/login.html");
        }
    }
}