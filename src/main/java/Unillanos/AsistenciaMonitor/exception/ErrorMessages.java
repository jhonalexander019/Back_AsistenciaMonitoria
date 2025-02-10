package Unillanos.AsistenciaMonitor.exception;

public class ErrorMessages {
    public static final String MONITOR_NOT_FOUND = "Monitor no encontrado";
    public static final String INVALID_MONITOR = "Monitor fuera de su semestre asignado";
    public static final String CREATE_INVALID_SEMESTER = "No es posible crear el semestre, ya existe uno en esas fechas.";
    public static final String EDIT_INVALID_SEMESTER = "No se puede editar este semestre.";
    public static final String DELETE_INVALID_SEMESTER = "No se puede eliminar este semestre.";
    public static final String DELETE_SUCCESS_SEMESTER = "Semestre eliminado correctamente.";
    public static final String SEMESTER_NOT_FOUND = "Semestre no encontrado.";
    public static final String INVALID_SEMESTER = "Estás fuera del semestre vigente.";
    public static final String INVALID_UPDATE_PROFILE = "Error al actualizar el perfil.";
    public static final String ROL_NOT_FOUND = "Rol no encontrado.";
    public static final String MONITOR_DUPLICATE_EMAIL = "Ya existe un monitor con este correo para el semestre actual.";
    public static final String INVALID_DAY = "Día no válido.";
    public static final String INVALID_REQUEST = "Solicitud no válida.";
    public static final String INVALID_SESSION = "Código inválido o usuario no encontrado.";
    public static final String MONITORS_NOT_FOUND_PER_DAY = "No se encontraron monitores para este día.";
    public static final String MONITORS_NOT_FOUND_PER_SEMESTER = "No se encontraron monitores para este semestre.";
    public static final String INVALID_REGISTER_ATTENDANCE = "Hora inválida para registrar asistencia.";
    public static final String ATTENDANCE_NOT_FOUND = "El monitor no tiene horas por recuperar.";
    public static final String REPEATED_ATTENDANCE = "Registro de asistencia inválido, ya se ha registrado en esta jornada.";
    public static final String INTERNAL_SERVER_ERROR = "Error interno del servidor.";
}
