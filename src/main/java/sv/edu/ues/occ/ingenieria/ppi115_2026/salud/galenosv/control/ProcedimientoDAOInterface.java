package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import java.util.List;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.Procedimiento;

public interface ProcedimientoDAOInterface extends DAOInterface<Procedimiento> {
    List<Procedimiento> buscarPorNombre(String nombre);
}