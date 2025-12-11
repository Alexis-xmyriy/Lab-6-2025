package functions;

import java.io.*;

public class TabulatedFunctions {
    // Приватный конструктор, чтобы нельзя было создать экземпляр класса
    private TabulatedFunctions() {}
    
    // Задание 6: метод табулирования функции
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        // Проверка, что интервал табулирования находится в области определения функции
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал табулирования находится за пределами области определения функции");
        }
        
        // Создание массива значений функции
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        // Вычисление значений функции в точках
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        
        // Используем ArrayTabulatedFunction по умолчанию
        return new ArrayTabulatedFunction(leftX, rightX, values);
    }
    
    // Задание 7: методы ввода/вывода
    
    // Метод для записи табулированной функции в байтовый поток
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        
        // Записываем количество точек
        dos.writeInt(function.getPointsCount());
        
        // Записываем координаты всех точек
        for (int i = 0; i < function.getPointsCount(); i++) {
            FunctionPoint point = function.getPoint(i);
            dos.writeDouble(point.getX());
            dos.writeDouble(point.getY());
        }
        
        dos.flush();
    }
    
    // Метод для чтения табулированной функции из байтового потока
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        
        // Читаем количество точек
        int pointsCount = dis.readInt();
        
        // Читаем координаты всех точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dis.readDouble();
            double y = dis.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
        
        // Используем ArrayTabulatedFunction по умолчанию
        return new ArrayTabulatedFunction(points);
    }
    
    // Метод для записи табулированной функции в символьный поток
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        
        // Записываем количество точек
        writer.print(function.getPointsCount());
        
        // Записываем координаты всех точек через пробелы
        for (int i = 0; i < function.getPointsCount(); i++) {
            FunctionPoint point = function.getPoint(i);
            writer.print(" " + point.getX() + " " + point.getY());
        }
        
        writer.flush();
    }
    
    // Метод для чтения табулированной функции из символьного потока
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.parseNumbers(); // Указываем, что нужно парсить числа
        
        // Читаем количество точек
        tokenizer.nextToken();
        int pointsCount = (int)tokenizer.nval;
        
        // Читаем координаты всех точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        
        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;
            
            tokenizer.nextToken();
            double y = tokenizer.nval;
            
            points[i] = new FunctionPoint(x, y);
        }
        
        // Используем ArrayTabulatedFunction по умолчанию
        return new ArrayTabulatedFunction(points);
    }
}