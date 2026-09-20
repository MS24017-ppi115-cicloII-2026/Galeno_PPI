package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import java.lang.reflect.ParameterizedType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DefaultDAO<T> implements DAOInterface<T> {

    @SuppressWarnings("unchecked")
    private Class<T> getEntityClass() {
        ParameterizedType tipoParametrizado = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) tipoParametrizado.getActualTypeArguments()[0];
    }

    public abstract EntityManager getEntityManger();

    @Override
    public void crear(T registro) throws IllegalArgumentException, IllegalStateException {
        if (registro != null) {
            try {
                //el persist hace que el registro nno se borre al salir de la aplicacion, hace que persista 
                getEntityManger().persist(registro);
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new IllegalStateException("Error al crear el regsitro", ex);
            }
        } else {
            throw new IllegalArgumentException("El registro no puede ser nulo");
        }
    }

    @Override
    public void actualizar(T registro) throws IllegalArgumentException, IllegalStateException {
        if (registro != null) {
            try {
                getEntityManger().persist(registro);

            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new IllegalStateException("Error al actualizar el regsitro", ex);
            }
        } else {
            throw new IllegalArgumentException("El registro no puede ser nulo");
        }
    }

    @Override
    public T buscar(UUID id) throws IllegalArgumentException, IllegalStateException {
              if (id != null) {
            try {
                return getEntityManger().find(getEntityClass(), id);
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new IllegalStateException("Error al buscar el registro", ex);
            }
        } else {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
    }

    

    @Override
    public T eliminar(UUID id) throws IllegalArgumentException, IllegalStateException {
        if (id != null) {
            try {
                T managed = getEntityManger().find(getEntityClass(), id);
                if (managed != null) {
                    getEntityManger().remove(managed);
                } else {
                    throw new IllegalArgumentException("No existe un registro con ese id");
                }
            } catch (IllegalArgumentException ex) {
                throw ex;
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new IllegalStateException("Error al eliminar el registro", ex);
            }
        } else {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return null;

    }

    @Override
    public List<T> findRange(int first, int max) throws IllegalArgumentException, IllegalStateException {
        if (first >= 0 && max > 0) {
            try {
                EntityManager em = getEntityManger();
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
                Root<T> raiz = cq.from(getEntityClass());
                cq.select(raiz);

                TypedQuery<T> q = em.createQuery(cq);
                q.setFirstResult(first);
                q.setMaxResults(max);

                return q.getResultList();
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new IllegalStateException("Error al obtener el rango de registros", ex);
            }
        } else {
            throw new IllegalArgumentException("Los parámetros de paginación (first/max) no son válidos");
        }
    }
}
