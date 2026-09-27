package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ClinicaDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Clinica;

@Named
@ViewScoped
public class ClinicaModels extends AbstractModel<Clinica> {

    @Inject
    ClinicaDAO clinicaDAO;

    @Override
    protected DAOInterface<Clinica> getDAO() {
        return clinicaDAO;
    }

    @Override
    protected Clinica crearRegistroNuevo() {
        Clinica c = new Clinica(UUID.randomUUID());
        c.setActivo(Boolean.TRUE);
        return c;
    }

    @Override
    protected UUID obtenerId(Clinica registro) {
        return registro.getIdClinica();
    }
}
