package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.control;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPaso;
import sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenosv.entity.ProcedimientoPasoSecuencia;

/** Orden topológico estable, con el mismo desempate que FlujoPasos.ordenar. */
public final class OrdenPasos {
    private OrdenPasos() {}

    public static List<ProcedimientoPaso> ordenar(List<ProcedimientoPaso> pasos,
                                                 List<ProcedimientoPasoSecuencia> secuencias) {
        Map<UUID, Set<UUID>> siguientes = new HashMap<>();
        Map<UUID, Integer> dependencias = new HashMap<>();
        for (var paso : pasos) {
            siguientes.put(paso.getIdProcedimientoPaso(), new LinkedHashSet<>());
            dependencias.put(paso.getIdProcedimientoPaso(), 0);
        }
        for (var secuencia : secuencias) {
            if (secuencia == null || secuencia.getIdProcedimientoPaso() == null) {
                continue;
            }
            UUID paso = secuencia.getIdProcedimientoPaso().getIdProcedimientoPaso();
            UUID referencia = secuencia.getIdProcedimientoPasoReferencia();
            String tipo = Normalizer.normalize(Objects.toString(secuencia.getTipoSecuencia(), "")
                    .toLowerCase(Locale.ROOT), Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
            boolean despues = tipo.matches(".*(after|despues|posterior).*")
                    || (!tipo.contains("before") && tipo.matches(".*(ant|prev).*"));
            UUID origen = despues ? referencia : paso;
            UUID destino = despues ? paso : referencia;
            if (origen != null && destino != null && !origen.equals(destino)
                    && siguientes.containsKey(origen) && dependencias.containsKey(destino)
                    && siguientes.get(origen).add(destino)) {
                dependencias.compute(destino, (id, total) -> total + 1);
            }
        }
        List<ProcedimientoPaso> pendientes = new ArrayList<>(pasos);
        List<ProcedimientoPaso> orden = new ArrayList<>(pasos.size());
        while (!pendientes.isEmpty()) {
            int indice = 0;
            for (int i = 0; i < pendientes.size(); i++) {
                if (dependencias.get(pendientes.get(i).getIdProcedimientoPaso()) == 0) {
                    indice = i;
                    break;
                }
            }
            var paso = pendientes.remove(indice);
            orden.add(paso);
            for (UUID destino : siguientes.get(paso.getIdProcedimientoPaso())) {
                dependencias.compute(destino, (id, total) -> total - 1);
            }
        }
        return orden;
    }
}
