public class Utils {

    // Valida si una IP tiene formato correcto
    public static boolean validarIP(String ip) {
        return ip.matches(
            "^((25[0-5]|2[0-4][0-9]|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?\\d\\d?)$"
        );
    }
}