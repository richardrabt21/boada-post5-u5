package com.ejemplo.servlet;

import com.ejemplo.model.Tarea;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DetalleTareaServlet", urlPatterns = {"/tareas/detalle"})
public class DetalleTareaServlet extends HttpServlet {

    /** GET /tareas/detalle?id=X — muestra la información completa de una tarea */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            // id ausente o no numérico: se vuelve al listado con un aviso
            resp.sendRedirect(req.getContextPath() + "/tareas?error=id-invalido");
            return;
        }

        // La lista se lee de applicationScope: TareasServlet la publicó en
        // su init(), así no se duplica el estado entre los dos Servlets
        @SuppressWarnings("unchecked")
        List<Tarea> tareas = (List<Tarea>) getServletContext().getAttribute("tareas");

        Tarea tarea = tareas.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);

        if (tarea == null) {
            resp.sendRedirect(req.getContextPath() + "/tareas?error=no-encontrada");
            return;
        }

        // Se usa forward (no redirect): el objeto Tarea viaja en el request
        // hacia detalle.jsp sin generar una nueva petición del navegador
        req.setAttribute("tarea", tarea);
        req.getRequestDispatcher("/WEB-INF/views/detalle.jsp")
           .forward(req, resp);
    }
}