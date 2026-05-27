package model.avance;

public class EstrategiaAvanceFactory {
    private EstrategiaAvanceFactory() {}

    public static EstrategiaAvance crear(String tipo) {
        return switch (tipo) {
            case "VELOCISTA" -> new AvanceVelocista();
            case "RESISTENTE" -> new AvanceResistente();
            default -> new AvanceEquilibrado();
        };
    }
}
