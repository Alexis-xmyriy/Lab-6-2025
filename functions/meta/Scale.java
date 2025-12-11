package functions.meta;

import functions.Function;

public class Scale implements Function {
    private Function f;
    private double scaleX;
    private double scaleY;
    
    public Scale(Function f, double scaleX, double scaleY) {
        this.f = f;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    @Override
    public double getLeftDomainBorder() {
        if (scaleX > 0) {
            return f.getLeftDomainBorder() * scaleX;
        } else {
            return f.getRightDomainBorder() * scaleX;
        }
    }
    
    @Override
    public double getRightDomainBorder() {
        if (scaleX > 0) {
            return f.getRightDomainBorder() * scaleX;
        } else {
            return f.getLeftDomainBorder() * scaleX;
        }
    }
    
    @Override
    public double getFunctionValue(double x) {
        return f.getFunctionValue(x / scaleX) * scaleY;
    }
}