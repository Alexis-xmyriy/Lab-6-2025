package functions;
import functions.meta.*;

public class Functions {
    // Приватный конструктор, чтобы нельзя было создать экземпляр
    private Functions() {}

    // Задание 1: Метод для вычисления интеграла
    public static double integrate(Function f, double left, double right, double step) {
        // Проверка, что интервал интегрирования входит в область определения
        if (left < f.getLeftDomainBorder() || right > f.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал интегрирования выходит за границы области определения функции");
        }
        
        if (left >= right) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой границы");
        }
        
        if (step <= 0) {
            throw new IllegalArgumentException("Шаг должен быть положительным числом");
        }
        
        double integral = 0.0;
        double currentX = left;
        
        while (currentX < right) {
            double nextX = Math.min(currentX + step, right);
            double f1 = f.getFunctionValue(currentX);
            double f2 = f.getFunctionValue(nextX);
            
            // Если какое-то значение NaN, возвращаем NaN
            if (Double.isNaN(f1) || Double.isNaN(f2)) {
                return Double.NaN;
            }
            
            // Метод трапеций
            integral += (f1 + f2) * (nextX - currentX) / 2.0;
            currentX = nextX;
        }
        
        return integral;
    }
    
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }
    
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }
    
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }
    
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }
    
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }
    
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }
}