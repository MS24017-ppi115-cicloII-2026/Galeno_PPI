package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    // ---------- helpers comunes ----------

    /**
     * Devuelve el EntityManager del DAO y valida que esté disponible.
     *
     * @throws IllegalStateException si el EntityManager es nulo
     */
    protected EntityManager requireEntityManager() {
        EntityManager em = getEntityManger();
        if (em == null) {
            throw new IllegalStateException(
                    "El EntityManager no está disponible en "
                    + getClass().getSimpleName());
        }
        return em;
    }

    /**
     * Valida que el valor no sea nulo.
     *
     * @throws IllegalArgumentException con el mensaje recibido
     */
    protected void validarNoNulo(Object valor, String mensaje) {
        if (valor == null) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    /**
     * Valida que el texto no sea nulo ni esté en blanco.
     *
     * @throws IllegalArgumentException con el mensaje recibido
     */
    protected void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    /**
     * Valida que las fechas del rango existan y estén en orden.
     *
     * @throws IllegalArgumentException con un mensaje descriptivo
     */
    protected void validarRangoFechas(Date desde, Date hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException(
                    "Las fechas del rango no pueden ser nulas");
        }
        if (desde.after(hasta)) {
            throw new IllegalArgumentException(
                    "El rango de fechas no es válido: la fecha inicial "
                    + "no puede ser posterior a la fecha final");
        }
    }

    /**
     * Valida los parámetros de paginación.
     *
     * @throws IllegalArgumentException con el mensaje establecido
     */
    protected void validarPaginacion(int first, int max) {
        if (first < 0 || max <= 0) {
            throw new IllegalArgumentException(
                    "Los parámetros de paginación (first/max) no son válidos");
        }
    }

    /**
     * Ejecuta una {@code @NamedQuery} con parámetros nombrados y devuelve
     * siempre una lista no nula (lista vacía si no hay resultados).
     *
     * @throws DAOException ante fallos de JPA o de la base de datos,
     *         conservando la causa original
     */
    protected <X> List<X> buscarConNamedQuery(String nombre, Class<X> tipo,
            Map<String, Object> parametros) {
        EntityManager em = requireEntityManager();
        try {
            TypedQuery<X> q = em.createNamedQuery(nombre, tipo);
            aplicarParametros(q, parametros);
            List<X> resultados = q.getResultList();
            return (resultados != null) ? resultados : List.of();
        } catch (RuntimeException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            throw new DAOException("Error al ejecutar la consulta " + nombre, ex);
        }
    }

    /**
     * Ejecuta una {@code @NamedQuery} que debe devolver un único resultado.
     * Si no hay resultados devuelve null (contrato de los métodos que lo
     * usan); si devuelve más de uno lanza {@link DAOException}.
     *
     * @throws DAOException ante fallos de JPA o de la base de datos,
     *         conservando la causa original
     */
    protected <X> X buscarUnicoConNamedQuery(String nombre, Class<X> tipo,
            Map<String, Object> parametros) {
        EntityManager em = requireEntityManager();
        try {
            TypedQuery<X> q = em.createNamedQuery(nombre, tipo);
            aplicarParametros(q, parametros);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            throw new DAOException(
                    "La consulta " + nombre + " devolvió más de un resultado", ex);
        } catch (RuntimeException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            throw new DAOException("Error al ejecutar la consulta " + nombre, ex);
        }
    }

    private <X> void aplicarParametros(TypedQuery<X> q,
            Map<String, Object> parametros) {
        if (parametros != null) {
            for (Map.Entry<String, Object> entrada : parametros.entrySet()) {
                q.setParameter(entrada.getKey(), entrada.getValue());
            }
        }
    }

    // ---------- operaciones CRUD heredadas ----------

    @Override
    public void crear(T registro) throws IllegalArgumentException, IllegalStateException {
        if (registro != null) {
            EntityManager em = requireEntityManager();
            try {
                //el persist hace que el registro nno se borre al salir de la aplicacion, hace que persista
                em.persist(registro);
            } catch (RuntimeException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new DAOException("Error al crear el regsitro", ex);
            }
        } else {
            throw new IllegalArgumentException("El registro no puede ser nulo");
        }
    }

    @Override
    public void actualizar(T registro) throws IllegalArgumentException, IllegalStateException {
        if (registro != null) {
            EntityManager em = requireEntityManager();
            try {
                em.merge(registro);
            } catch (RuntimeException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new DAOException("Error al actualizar el registro", ex);
            }
        } else {
            throw new IllegalArgumentException("El registro no puede ser nulo");
        }
    }

    @Override
    public T buscar(UUID id) throws IllegalArgumentException, IllegalStateException {
          if (id != null) {
            EntityManager em = requireEntityManager();
            try {
                return em.find(getEntityClass(), id);
            } catch (RuntimeException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new DAOException("Error al buscar el registro", ex);
            }
        } else {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
    }



     public T eliminar(UUID id) throws IllegalArgumentException, IllegalStateException {
        if (id != null) {
            EntityManager em = requireEntityManager();
            try {
                T managed = em.find(getEntityClass(), id);
                if (managed != null) {
                    em.remove(managed);
                    em.flush(); // fuerza el DELETE ahora, dentro de este try/catch
                } else {
                    throw new IllegalArgumentException("No existe un registro con ese id");
                }
            } catch (IllegalArgumentException ex) {
                throw ex;
            } catch (PersistenceException ex) {
                Logger.getLogger(getClass().getName()).log(Level.WARNING,
                        "No se pudo eliminar el registro {0}: probablemente esta en uso por otra tabla", id);
                throw new DAOException("No se puede eliminar: el registro esta siendo utilizado en otra parte del sistema", ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new DAOException("Error al eliminar el registro", ex);
            }
        } else {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        return null;
    }
    @Override
    public List<T> findRange(int first, int max) throws IllegalArgumentException, IllegalStateException {
        validarPaginacion(first, max);
        EntityManager em = requireEntityManager();
        try {
            jakarta.persistence.criteria.CriteriaBuilder cb = em.getCriteriaBuilder();
            jakarta.persistence.criteria.CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
            jakarta.persistence.criteria.Root<T> raiz = cq.from(getEntityClass());
            cq.select(raiz);

            TypedQuery<T> q = em.createQuery(cq);
            q.setFirstResult(first);
            q.setMaxResults(max);

            List<T> resultados = q.getResultList();
            return (resultados != null) ? resultados : List.of();
        } catch (RuntimeException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new DAOException("Error al obtener el rango de registros", ex);
        }
    }
}
