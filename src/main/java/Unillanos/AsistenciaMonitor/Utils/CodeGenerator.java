package Unillanos.AsistenciaMonitor.Utils;

import java.util.Random;

public class CodeGenerator {
    public static Integer generarCodigoUnico(Random random, java.util.function.Predicate<Integer> existeCodigo) {
        Integer codigo;
        do {
            codigo = 1000 + random.nextInt(9000); // Genera un número entre 1000 y 9999
        } while (existeCodigo.test(codigo));
        return codigo;
    }
}
