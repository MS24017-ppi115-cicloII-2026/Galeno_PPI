package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import java.util.List;
import java.util.UUID;

public interface DAOInterface<T> {

    public void crear(T registro) throws IllegalArgumentException, IllegalStateException;

    public void actualizar(T registro) throws IllegalArgumentException, IllegalStateException;

    public T buscar(UUID id) throws IllegalArgumentException, IllegalStateException;

    public T eliminar(UUID id) throws IllegalArgumentException, IllegalStateException;

    public List<T> findRange(int first, int max) throws IllegalArgumentException, IllegalStateException;
}
