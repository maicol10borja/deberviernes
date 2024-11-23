package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import services.LoginService;
import services.LoginServiceImplement;
import services.LoginServiceSessionImplement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;

@WebServlet({"/login", "/login.html"})
public class LoginServlet  extends HttpServlet {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "12345";

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
            resp.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = resp.getWriter()) {
                // Plantilla HTML para usuarios autenticados
                out.print("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset=\"utf-8\">");
                out.println("<title>Hola " + cookieOptional.get() + "</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Hola " + cookieOptional.get() + ", bienvenido  ya has iniciado sesión !</h1>");
                out.println("<p><a href='" + req.getContextPath() + "/index.html'>Volver al inicio</a></p>");
                out.println("<p><a href='" + req.getContextPath() + "/logout'>Cerrar sesión</a></p>");  // Enlace para cerrar sesión
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            // Redirigir al formulario de login si no hay cookies
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            // Crear una cookie para almacenar el nombre de usuario
            Cookie usernameCookie = new Cookie("username", username);
            resp.addCookie(usernameCookie);

            // Redirigir a login.html tras el login exitoso
            resp.sendRedirect(req.getContextPath() + "/login.html");
        } else {
            // Retornar error 401 si las credenciales no son correctas
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Lo sentimos, usted no tiene acceso.");
        }
    }
}