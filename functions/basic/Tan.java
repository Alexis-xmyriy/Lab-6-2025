package functions.basic;

public class Tan extends TrigonometricFunction {
    @Override
    public double getFunctionValue(double x) {
        // Тангенс не определен при x = π/2 + πk
        double cos = Math.cos(x);
        if (Math.abs(cos) < 1e-9) {
            return Double.NaN;
        }
        return Math.tan(x);
    }
}