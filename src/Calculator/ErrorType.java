package Calculator;
public class ErrorType {
    public enum Type {
        TOO_FEW_ARGUMENTS(1, "Too few arguments"),
        TOO_MANY_ARGUMENTS(2, "Too many arguments"),
        DIVIDED_BY_ZERO(3, "Divided by zero"),
        INVALID_OPERATION(4, "Invalid operation");

        private final int code;
        private final String description;

        Type(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static String getDescriptionFromCode(int code) {
            for (Type error : values()) {
                if (error.code == code) {
                    return error.description;
                }
            }
            return null;
        }
    }
}

