package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenTipoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenTipoExamen;

@Named
@ViewScoped
public class ExamenTipoExamenModels extends AbstractModel<ExamenTipoExamen> {

    @Inject
    ExamenTipoExamenDAO examenTipoExamenDAO;

    @Override
    protected DAOInterface<ExamenTipoExamen> getDAO() {
        return examenTipoExamenDAO;
    }

    @Override
    protected ExamenTipoExamen crearRegistroNuevo() {
        ExamenTipoExamen ete = new ExamenTipoExamen(UUID.randomUUID());
        return ete;
    }

    @Override
    protected UUID obtenerId(ExamenTipoExamen registro) {
        return registro.getIdExamenTipoExamen();
    }
}