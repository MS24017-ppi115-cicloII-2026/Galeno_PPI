package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaProcedimientoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimiento;

@Named
@ViewScoped
public class ConsultaProcedimientoModels extends AbstractModel<ConsultaProcedimiento> {

    @Inject
    ConsultaProcedimientoDAO consultaProcedimientoDAO;

    @Override
    protected DAOInterface<ConsultaProcedimiento> getDAO() {
        return consultaProcedimientoDAO;
    }

    @Override
    protected ConsultaProcedimiento crearRegistroNuevo() {
        ConsultaProcedimiento cp = new ConsultaProcedimiento(UUID.randomUUID());
        return cp;
    }

    @Override
    protected UUID obtenerId(ConsultaProcedimiento registro) {
        return registro.getIdConsultaProcedimiento();
    }
}