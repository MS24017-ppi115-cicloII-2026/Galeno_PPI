package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;

@Named
@ViewScoped
public class ProcedimientoModels extends AbstractModel<Procedimiento> {

    @Inject
    ProcedimientoDAO procedimientoDAO;

    @Override
    protected DAOInterface<Procedimiento> getDAO() {
        return procedimientoDAO;
    }

    @Override
    protected Procedimiento crearRegistroNuevo() {
        Procedimiento p = new Procedimiento(UUID.randomUUID());
        // Si tu entidad Procedimiento tiene un campo de estado como 'activo', 
        // puedes inicializarlo aquí, por ejemplo:
        // p.setActivo(Boolean.TRUE);
        return p;
    }

    @Override
    protected UUID obtenerId(Procedimiento registro) {
        return registro.getIdProcedimiento();
    }
}