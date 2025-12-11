package functions;
import java.io.*;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable {
    private FunctionPoint[] points;
    private int pointsCount;

    public ArrayTabulatedFunction() {
        // Инициализация по умолчанию
    }
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pointsCount = in.readInt();
        points = new FunctionPoint[pointsCount + 10];
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX || pointsCount < 2) 
            throw new IllegalArgumentException("Invalid domain or points count");
        
        this.points = new FunctionPoint[pointsCount + 10];
        this.pointsCount = pointsCount;
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX || values.length < 2) 
            throw new IllegalArgumentException("Invalid domain or points count");
        
        this.points = new FunctionPoint[values.length + 10];
        this.pointsCount = values.length;
        double step = (rightX - leftX) / (values.length - 1);
        
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] pointsArray) {
        if (pointsArray.length < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }
        
        for (int i = 0; i < pointsArray.length - 1; i++) {
            if (pointsArray[i].getX() >= pointsArray[i + 1].getX()) {
                throw new IllegalArgumentException("Points must be ordered by X");
            }
        }
        
        this.points = new FunctionPoint[pointsArray.length + 10];
        this.pointsCount = pointsArray.length;
        
        for (int i = 0; i < pointsArray.length; i++) {
            this.points[i] = new FunctionPoint(pointsArray[i]);
        }
    }

    @Override
    public double getLeftDomainBorder() { return points[0].getX(); }
    
    @Override
    public double getRightDomainBorder() { return points[pointsCount - 1].getX(); }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) 
            return Double.NaN;

        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(points[i].getX() - x) < 1e-9) 
                return points[i].getY();
        }

        for (int i = 0; i < pointsCount - 1; i++) {
            if (x >= points[i].getX() && x <= points[i + 1].getX()) {
                double x1 = points[i].getX(), y1 = points[i].getY();
                double x2 = points[i + 1].getX(), y2 = points[i + 1].getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }
        return Double.NaN;
    }

    @Override
    public int getPointsCount() { return pointsCount; }

    @Override
    public FunctionPoint getPoint(int index) {
        checkIndex(index);
        return new FunctionPoint(points[index]);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        checkIndex(index);
        
        if ((index > 0 && point.getX() <= points[index - 1].getX()) || 
            (index < pointsCount - 1 && point.getX() >= points[index + 1].getX())) {
            throw new InappropriateFunctionPointException("Invalid X order");
        }
        
        points[index] = new FunctionPoint(point);
    }

    @Override
    public double getPointX(int index) {
        checkIndex(index);
        return points[index].getX();
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        checkIndex(index);
        
        if ((index > 0 && x <= points[index - 1].getX()) || 
            (index < pointsCount - 1 && x >= points[index + 1].getX())) {
            throw new InappropriateFunctionPointException("Invalid X order");
        }
        
        points[index].setX(x);
    }

    @Override
    public double getPointY(int index) {
        checkIndex(index);
        return points[index].getY();
    }

    @Override
    public void setPointY(int index, double y) {
        checkIndex(index);
        points[index].setY(y);
    }

    @Override
    public void deletePoint(int index) {
        checkIndex(index);
        if (pointsCount < 3) 
            throw new IllegalStateException("Minimum 2 points required");
        
        for (int i = index; i < pointsCount - 1; i++) 
            points[i] = points[i + 1];
        
        points[pointsCount - 1] = null;
        pointsCount--;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int i = 0;
        while (i < pointsCount && points[i].getX() < point.getX()) i++;
        
        if (i < pointsCount && Math.abs(points[i].getX() - point.getX()) < 1e-9)
            throw new InappropriateFunctionPointException("Duplicate X coordinate");
        
        if (pointsCount == points.length) {
            FunctionPoint[] newArray = new FunctionPoint[pointsCount + 10];
            for (int j = 0; j < pointsCount; j++) newArray[j] = points[j];
            points = newArray;
        }
        
        for (int j = pointsCount; j > i; j--) 
            points[j] = points[j - 1];
        
        points[i] = new FunctionPoint(point);
        pointsCount++;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Index: " + index);
    }
    
    // Задание 2: Переопределение методов Object
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < pointsCount; i++) {
            if (i > 0) sb.append(", ");
            sb.append(points[i].toString());
        }
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        
        if (o instanceof TabulatedFunction) {
            TabulatedFunction other = (TabulatedFunction) o;
            
            if (this.getPointsCount() != other.getPointsCount()) {
                return false;
            }
            
            if (o instanceof ArrayTabulatedFunction) {
                ArrayTabulatedFunction arrayOther = (ArrayTabulatedFunction) o;
                
                for (int i = 0; i < pointsCount; i++) {
                    if (!this.points[i].equals(arrayOther.points[i])) {
                        return false;
                    }
                }
                return true;
            } else {
                for (int i = 0; i < pointsCount; i++) {
                    FunctionPoint thisPoint = this.getPoint(i);
                    FunctionPoint otherPoint = other.getPoint(i);
                    
                    if (!thisPoint.equals(otherPoint)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = pointsCount;
        for (int i = 0; i < pointsCount; i++) {
            hash ^= points[i].hashCode();
        }
        return hash;
    }
    
    @Override
    public Object clone() {
        try {
            ArrayTabulatedFunction clone = (ArrayTabulatedFunction) super.clone();
            
            clone.points = new FunctionPoint[this.points.length];
            for (int i = 0; i < this.pointsCount; i++) {
                clone.points[i] = (FunctionPoint) this.points[i].clone();
            }
            
            clone.pointsCount = this.pointsCount;
            
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}