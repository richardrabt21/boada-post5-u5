package com.ejemplo.servlet;

import com.ejemplo.model.Tarea;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "TareasServlet", urlPatterns = {"/tareas"})
public class TareasServlet extends HttpServlet {

    // Lista en memoria (solo para este laboratorio).
    // Es variable de instancia porque es estado compartido de toda la
    // aplicación (todas las peticiones deben ver la misma lista), no un
    // dato de una petición individual.
    private final List<Tarea> tareas = new ArrayList<>();
    private int contadorId = 1;

    @Override
    public void init() throws ServletException {
        // Cargar datos de ejemplo al iniciar
        tareas.add(new Tarea(contadorId++, "Leer documentación de Servlets"));
        tareas.add(new Tarea(contadorId++, "Implementar ciclo GET/POST"));
    }

    /** GET /tareas — mostrar lista */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("tareas", tareas);
        req.getRequestDispatcher("/WEB-INF/views/tareas.jsp")
           .forward(req, resp);
    }

    /** POST /tareas — agregar o eliminar tarea */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String accion = req.getParameter("accion");

        if ("agregar".equals(accion)) {
            // "titulo" es variable local: es un dato de esta petición,
            // no debe guardarse en un campo compartido entre hilos
            String titulo = req.getParameter("titulo");
            if (titulo == null || titulo.isBlank()) {
                // Validación en servidor: se responde con forward (no redirect)
                // para conservar el mensaje de error en el request
                req.setAttribute("error", "El título no puede estar vacío");
                req.setAttribute("tareas", tareas);
                req.getRequestDispatcher("/WEB-INF/views/tareas.jsp")
                   .forward(req, resp);
                return;
            }
            tareas.add(new Tarea(contadorId++, titulo.trim()));
        } else if ("eliminar".equals(accion)) {
            int id = Integer.parseInt(req.getParameter("id"));
            tareas.removeIf(t -> t.getId() == id);
        }

        // Patrón PRG: redirigir después de POST para que recargar la página
        // no reenvíe el formulario
        resp.sendRedirect(req.getContextPath() + "/tareas");
    }
}