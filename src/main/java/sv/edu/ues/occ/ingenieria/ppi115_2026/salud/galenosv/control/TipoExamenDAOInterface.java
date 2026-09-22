package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import java.util.List;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.TipoExamen;


public interface TipoExamenDAOInterface extends DAOInterface<TipoExamen> {
    List<TipoExamen> buscarPorNombre(String nombre);
}