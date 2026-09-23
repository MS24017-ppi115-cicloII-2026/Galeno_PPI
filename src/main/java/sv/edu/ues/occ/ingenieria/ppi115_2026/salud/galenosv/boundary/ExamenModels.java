package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;

@Named
@ViewScoped
public class ExamenModels extends AbstractModel<Examen> {

    @Inject
    ExamenDAO examenDAO;

    @Override
    protected DAOInterface<Examen> getDAO() {
        return examenDAO;
    }

    @Override
    protected Examen crearRegistroNuevo() {
        Examen e = new Examen(UUID.randomUUID());
        // Inicializamos el campo activo que mostraste en tu entidad Examen
        e.setActivo(Boolean.TRUE); 
        return e;
    }

    @Override
    protected UUID obtenerId(Examen registro) {
        return registro.getIdExamen();
    }
}