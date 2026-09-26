package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.RolDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Rol;

@Named("rolModel")
@ViewScoped
public class RolModels extends AbstractModel<Rol> {

    @Inject
    private RolDAO rolDAO;

    @Override
    protected DAOInterface<Rol> getDAO() {
        return rolDAO;
    }

    @Override
    protected Rol crearRegistroNuevo() {
        Rol rol = new Rol();
        rol.setIdRol(UUID.randomUUID());
        rol.setActivo(true);
        return rol;
    }

    @Override
    protected UUID obtenerId(Rol registro) {
        return registro.getIdRol();
    }
}