package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
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

    /**
     * Devuelve exámenes activos más el actual del registro.
     */
    public List<Examen> getExamenes() {
        List<Examen> lista = new ArrayList<>(examenDAO.findRange(0, 1000).stream()
                .filter(e -> Boolean.TRUE.equals(e.getActivo()))
                .toList());
        if (registro != null && registro.getIdExamen() != null
                && lista.stream().noneMatch(e -> e.getIdExamen().equals(registro.getIdExamen().getIdExamen()))) {
            Examen actual = examenDAO.buscar(registro.getIdExamen().getIdExamen());
            if (actual != null) {
                lista.add(actual);
            }
        }
        return lista;
    }

    /**
     * Devuelve tipos de examen activos más el actual del registro.
     */
    public List<TipoExamen> getTiposExamen() {
        List<TipoExamen> lista = new ArrayList<>(tipoExamenDAO.findRange(0, 1000).stream()
                .filter(t -> Boolean.TRUE.equals(t.getActivo()))
                .toList());
        if (registro != null && registro.getIdTipoExamen() != null
                && lista.stream().noneMatch(t -> t.getIdTipoExamen().equals(registro.getIdTipoExamen().getIdTipoExamen()))) {
            TipoExamen actual = tipoExamenDAO.buscar(registro.getIdTipoExamen().getIdTipoExamen());
            if (actual != null) {
                lista.add(actual);
            }
        }
        return lista;
    }

    private boolean vinculoValido() {
        Examen ex = (registro.getIdExamen() != null)
                ? examenDAO.buscar(registro.getIdExamen().getIdExamen()) : null;
        TipoExamen te = (registro.getIdTipoExamen() != null)
                ? tipoExamenDAO.buscar(registro.getIdTipoExamen().getIdTipoExamen()) : null;

        boolean crear = estado == Estado_CRUD.CREAR;

        if (crear && ex != null && !Boolean.TRUE.equals(ex.getActivo())) {
            rechazar("El examen \"" + ex.getNombre() + "\" está inactivo.");
            return false;
        }
        if (crear && te != null && !Boolean.TRUE.equals(te.getActivo())) {
            rechazar("El tipo de examen \"" + te.getNombre() + "\" está inactivo.");
            return false;
        }
        if (ex != null && te != null) {
            boolean duplicado = getRegistrosPorExamen(ex.getIdExamen()).stream()
                    .anyMatch(x -> x.getIdTipoExamen().getIdTipoExamen().equals(te.getIdTipoExamen())
                            && !x.getIdExamenTipoExamen().equals(registro.getIdExamenTipoExamen()));
            if (duplicado) {
                rechazar("El examen \"" + ex.getNombre() + "\" ya tiene asignado el tipo \"" + te.getNombre() + "\".");
                return false;
            }
        }
        return true;
    }

    private void rechazar(String detalle) {
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "No se puede guardar", detalle));
        fc.validationFailed(); // evita que el diálogo se cierre
    }

    @Override
    public void btnCrearhandler(ActionEvent ae) {
        if (registro != null && !vinculoValido()) {
            return;
        }
        super.btnCrearhandler(ae);
    }

    @Override
    public void btnModificarHandler() {
        if (registro != null && !vinculoValido()) {
            return;
        }
        super.btnModificarHandler();
    }
}