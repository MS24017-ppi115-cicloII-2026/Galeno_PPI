package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimientoPaso;

@Named
@ViewScoped
public class ConsultaProcedimientoPasoModels extends AbstractModel<ConsultaProcedimientoPaso> {

    @Inject
    ConsultaProcedimientoPasoDAO consultaProcedimientoPasoDAO;

    @Override
    protected DAOInterface<ConsultaProcedimientoPaso> getDAO() {
        return consultaProcedimientoPasoDAO;
    }

    @Override
    protected ConsultaProcedimientoPaso crearRegistroNuevo() {
        ConsultaProcedimientoPaso cpp = new ConsultaProcedimientoPaso(UUID.randomUUID());
        return cpp;
    }

    @Override
    protected UUID obtenerId(ConsultaProcedimientoPaso registro) {
        return registro.getIdConsultaProcedimientoPaso();
    }
}