package functions;

public interface TabulatedFunction extends Function, Cloneable {
    // Задание 4: Добавляем метод clone() в интерфейс
    Object clone();
    
    int getPointsCount();
    FunctionPoint getPoint(int index);
    void setPoint(int index, FunctionPoint point) 
        throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException;
    double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;
    void setPointX(int index, double x) 
        throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException;
    double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;
    void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;
    
    void deletePoint(int index) 
        throws FunctionPointIndexOutOfBoundsException, IllegalStateException;
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
}