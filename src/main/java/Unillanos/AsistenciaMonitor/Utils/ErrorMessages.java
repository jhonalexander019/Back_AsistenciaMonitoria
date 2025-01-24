package Unillanos.AsistenciaMonitor.Utils;

public class ErrorMessages {
    public static final String MONITOR_NOT_FOUND = "Monitor no encontrado";
    public static final String INVALID_MONITOR = "Monitor fuera de su semestre asignado";
    public static final String CREATE_INVALID_SEMESTER = "Crear este semestre no es posible, ya existe un semestre para esas fechas";
    public static final String EDIT_INVALID_SEMESTER = "Editar este semestre no es posible";
    public static final String DELETE_INVALID_SEMESTER = "Eliminar este semestre no es posible";
    public static final String DELETE_SUCCESS_SEMESTER = "Semestre eliminado correctamente";
    public static final String SEMESTER_NOT_FOUND = "Semestre no encontrado";
    public static final String INVALID_SEMESTER = "Te encuentras fuera del semestre vigente";
    public static final String INVALID_UPDATE_PROFILE = "Error al actualizar el perfil";
    public static final String ROL_NOT_FOUND = "Rol no encontrado";
    public static final String MONITOR_DUPLICATE_EMAIL = "Ya existe un monitor registrado con este correo para el semestre actual.";
    public static final String INVALID_DAY = "Día no válido";
    public static final String INVALID_REQUEST = "Solicitud no válida";
    public static final String INVALID_SESSION = "Código inválido o usuario no encontrado";
    public static final String MONITORS_NOT_FOUND_PER_DAY = "Monitores no encontrados para este dia";
    public static final String MONITORS_NOT_FOUND_PER_SEMESTER = "Monitores no encontrados para este semestre";
    public static final String INVALID_REGISTER_ATTENDANCE = "Hora actual invalida para registrar turno";
    public static final String ATTENDANCE_NOT_FOUND = "Monitor cuenta sin horas por recuperar";
    public static final String REPEATED_ATTENDANCE = "Registro de asistencia invalido, ya se ha registrado en esta jornada";
}
