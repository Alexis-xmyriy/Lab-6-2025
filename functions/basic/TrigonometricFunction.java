package functions.basic;

import functions.Function;

public abstract class TrigonometricFunction implements Function {
    @Override
    public double getLeftDomainBorder() {
        return -Double.MAX_VALUE;
    }
    
    @Override
    public double getRightDomainBorder() {
        return Double.MAX_VALUE;
    }
    
    @Override
    public abstract double getFunctionValue(double x);
}