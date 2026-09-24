package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenResultadoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenResultado;

@Named
@ViewScoped
public class ExamenResultadoModels extends AbstractModel<ExamenResultado> {

    @Inject
    ExamenResultadoDAO examenResultadoDAO;

    @Override
    protected DAOInterface<ExamenResultado> getDAO() {
        return examenResultadoDAO;
    }

    @Override
    protected ExamenResultado crearRegistroNuevo() {
        ExamenResultado er = new ExamenResultado(UUID.randomUUID());
        return er;
    }

    @Override
    protected UUID obtenerId(ExamenResultado registro) {
        return registro.getIdExamenResultado();
    }
}