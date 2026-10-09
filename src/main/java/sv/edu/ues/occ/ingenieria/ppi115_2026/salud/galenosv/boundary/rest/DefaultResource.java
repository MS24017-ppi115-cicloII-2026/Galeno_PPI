package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.boundary.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOException;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control.DAOInterface;

// T es la entidad (TipoDocumento, Documento, Rol...). Igual que en DefaultDAO<T>.
// Estas dos anotaciones aplican a todos los métodos: reciben y devuelven JSON.
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class DefaultResource<T> {

    // ---- Lo que cada hijo debe decir (por eso la clase es abstract) ----
    protected abstract DAOInterface<T> getDAO();          // qué DAO usar
    protected abstract UUID obtenerId(T registro);        // leer el id de la entidad
    protected abstract void asignarId(T registro, UUID id); // ponerle el id a la entidad

    // Gancho opcional: el hijo lo sobrescribe si necesita preparar el registro antes de guardarlo
    protected void antesDeCrear(T nuevo) {
    }

    // GET .../recurso?primero=0&max=50  -> lista paginada
    // @QueryParam lee el valor de la URL; @DefaultValue lo usa si el cliente no manda nada
    @GET
    public List<T> listar(@QueryParam("primero") @DefaultValue("0") int primero,
                          @QueryParam("max") @DefaultValue("50") int max) {
        return getDAO().findRange(primero, max);
    }

    // GET .../recurso/{id}  -> un registro
    // @PathParam toma el {id} de la URL; JAX-RS lo convierte solo a UUID
    @GET
    @Path("{id}")
    public Response buscar(@PathParam("id") UUID id) {
        T registro = getDAO().buscar(id);
        if (registro == null) {
            return Response.status(Response.Status.NOT_FOUND).build();   // 404
        }
        return Response.ok(registro).build();                            // 200 + JSON
    }

    // POST .../recurso  -> crear. El JSON del cuerpo llega convertido en el objeto "nuevo"
    @POST
    public Response crear(T nuevo) {
        if (nuevo == null) {
            return Response.status(Response.Status.BAD_REQUEST).build(); // 400
        }
        asignarId(nuevo, UUID.randomUUID());   // el servidor decide el id, no el cliente
        antesDeCrear(nuevo);                   // gancho del hijo (ej. activo = true)
        getDAO().crear(nuevo);
        return Response.status(Response.Status.CREATED).entity(nuevo).build(); // 201
    }

    // PUT .../recurso/{id}  -> modificar
    @PUT
    @Path("{id}")
    public Response actualizar(@PathParam("id") UUID id, T datos) {
        if (datos == null) {
            return Response.status(Response.Status.BAD_REQUEST).build(); // 400
        }
        if (getDAO().buscar(id) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();   // 404
        }
        asignarId(datos, id);                  // el id de la URL manda sobre el del JSON
        getDAO().actualizar(datos);
        return Response.ok(datos).build();
    }

    // DELETE .../recurso/{id}  -> eliminar
    @DELETE
    @Path("{id}")
    public Response eliminar(@PathParam("id") UUID id) {
        try {
            getDAO().eliminar(id);
            return Response.noContent().build();                          // 204
        } catch (DAOException e) {
            // tu DAO lanza esto cuando el registro está en uso por otra tabla
            return Response.status(Response.Status.CONFLICT)
                    .entity(e.getMessage()).build();                      // 409
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).build();   // 404, no existe
        }
    }
}