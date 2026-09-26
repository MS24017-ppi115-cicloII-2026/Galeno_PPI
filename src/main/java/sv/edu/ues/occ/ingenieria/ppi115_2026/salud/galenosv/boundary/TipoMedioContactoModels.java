package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoMedioContactoDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoMedioContacto;

@Named("tipoMedioContactoModel")
@ViewScoped
public class TipoMedioContactoModels extends AbstractModel<TipoMedioContacto> {

    @Inject
    private TipoMedioContactoDAO tipoMedioContactoDAO;

    @Override
    protected DAOInterface<TipoMedioContacto> getDAO() {
        return tipoMedioContactoDAO;
    }

    @Override
    protected TipoMedioContacto crearRegistroNuevo() {
        TipoMedioContacto tipoMedioContacto = new TipoMedioContacto();
        tipoMedioContacto.setIdTipoMedioContacto(UUID.randomUUID());
        tipoMedioContacto.setActivo(true);
        return tipoMedioContacto;
    }

    @Override
    protected UUID obtenerId(TipoMedioContacto registro) {
        return registro.getIdTipoMedioContacto();
    }
}