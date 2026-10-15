package es.cristichi.util;

public record Color(int red, int green, int blue) {
    @Override
    public final String toString() {
        return "Color %d,%d,%d".formatted(red, green, blue);
    }
}
