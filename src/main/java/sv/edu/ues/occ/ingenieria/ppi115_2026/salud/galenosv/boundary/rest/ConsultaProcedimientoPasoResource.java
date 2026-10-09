package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.io.Serializable;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ConsultaProcedimientoPasoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ConsultaProcedimientoPaso;

@Path("consulta_procedimiento_paso")
public class ConsultaProcedimientoPasoResource extends DefaultResource<ConsultaProcedimientoPaso> implements Serializable {

    @Inject
    ConsultaProcedimientoPasoDAO consultaProcedimientoPasoDAO;

    @Override
    protected DAOInterface<ConsultaProcedimientoPaso> getDAO() {
        return consultaProcedimientoPasoDAO;
    }

    @Override
    protected UUID obtenerId(ConsultaProcedimientoPaso registro) {
        return registro.getIdConsultaProcedimientoPaso();
    }

    @Override
    protected void asignarId(ConsultaProcedimientoPaso registro, UUID id) {
        registro.setIdConsultaProcedimientoPaso(id);
    }
}