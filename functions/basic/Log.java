package functions.basic;

import functions.Function;

public class Log implements Function {
    private double base;
    
    public Log(double base) {
        // Проверка корректности основания логарифма
        if (base <= 0 || Math.abs(base - 1) < 1e-9) {
            throw new IllegalArgumentException("Основание должно быть положительным и не равным 1");
        }
        this.base = base;
    }
    
    @Override
    public double getLeftDomainBorder() {
        // Логарифм определен для x > 0
        return 0;
    }
    
    @Override
    public double getRightDomainBorder() {
        // Логарифм определен до +бесконечности
        return Double.MAX_VALUE;
    }
    
    @Override
    public double getFunctionValue(double x) {
        // Проверка аргумента
        if (x <= 0) return Double.NaN;
        // Формула перехода к другому основанию: log_b(x) = ln(x) / ln(b)
        return Math.log(x) / Math.log(base);
    }
    
    // Геттер для основания логарифма
    public double getBase() {
        return base;
    }
}