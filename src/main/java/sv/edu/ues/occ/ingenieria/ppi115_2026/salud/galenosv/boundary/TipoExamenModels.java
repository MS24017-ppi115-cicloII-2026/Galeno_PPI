package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoExamen;

@Named
@ViewScoped
public class TipoExamenModels extends AbstractModel<TipoExamen> {

    @Inject
    TipoExamenDAO tipoExamenDAO;

    @Override
    protected DAOInterface<TipoExamen> getDAO() {
        return tipoExamenDAO;
    }

    @Override
    protected TipoExamen crearRegistroNuevo() {
        TipoExamen te = new TipoExamen(UUID.randomUUID());
        te.setActivo(Boolean.TRUE);
        return te;
    }

    @Override
    protected UUID obtenerId(TipoExamen registro) {
        return registro.getIdTipoExamen();
    }
}