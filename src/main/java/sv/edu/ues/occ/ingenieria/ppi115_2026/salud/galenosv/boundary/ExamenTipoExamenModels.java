package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.ExamenTipoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.TipoExamenDAO;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Examen;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ExamenTipoExamen;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoExamen;

@Named
@ViewScoped
public class ExamenTipoExamenModels extends AbstractModel<ExamenTipoExamen> {

    @Inject
    ExamenTipoExamenDAO examenTipoExamenDAO;

    @Inject
    ExamenDAO examenDAO;

    @Inject
    TipoExamenDAO tipoExamenDAO;

    @Override
    protected DAOInterface<ExamenTipoExamen> getDAO() {
        return examenTipoExamenDAO;
    }

    @Override
    protected ExamenTipoExamen crearRegistroNuevo() {
        return new ExamenTipoExamen(UUID.randomUUID());
    }

    @Override
    protected UUID obtenerId(ExamenTipoExamen registro) {
        return registro.getIdExamenTipoExamen();
    }

    /**
     * Prepara un nuevo vínculo para el Examen que se está configurando.
     */
    public void prepararNuevoParaExamen(UUID idExamen) {

        this.registro = crearRegistroNuevo();

        if (idExamen != null) {
            this.registro.setIdExamen(new Examen(idExamen));
        }

        this.estado = Estado_CRUD.CREAR;
    }

    /**
     * Devuelve únicamente los tipos de examen asociados
     * al Examen seleccionado.
     */
    public List<ExamenTipoExamen> getRegistrosPorExamen(UUID idExamen) {

        if (idExamen == null) {
            return List.of();
        }

        return examenTipoExamenDAO.buscarPorExamen(idExamen);
    }

    public List<Examen> getExamenes() {
        return examenDAO.findRange(0, 100);
    }

    public List<TipoExamen> getTiposExamen() {
        return tipoExamenDAO.findRange(0, 100);
    }
}

