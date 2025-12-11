package threads;

import functions.Function;
import functions.basic.Log;
import java.util.concurrent.Semaphore;

// Генератор заданий, расширяющий класс Thread
public class Generator extends Thread {
    private final Task task;
    private final Semaphore dataReady;
    private final Semaphore dataProcessed;

    public Generator(Task task, Semaphore dataReady, Semaphore dataProcessed){
        this.task = task;
        this.dataReady = dataReady;
        this.dataProcessed = dataProcessed;
    }
    
    @Override
    public void run() {
        try {
            // Генерация заданий в цикле
            for (int i = 0; i < task.getTasksCount(); i++) {

                
                // Генерируем случайные параметры
                double base = 1 + Math.random() * 9;   // основание логарифма от 1 до 10
                double left = Math.random() * 100;     // левая граница от 0 до 100
                double right = 100 + Math.random() * 100; // правая граница от 100 до 200
                double step = Math.random();           // шаг от 0 до 1
                dataProcessed.acquire();
                
                Function logFunc = new Log(base);
                task.setFunction(logFunc);
                task.setLeft(left);
                task.setRight(right);
                task.setStep(step);
                
                
                System.out.println("Создано задание: левая граница = " + String.format("%.2f", left) + 
                                 ", правая граница = " + String.format("%.2f", right) + 
                                 ", шаг = " + String.format("%.4f", step) + 
                                 "   [Основание логарифма: " + String.format("%.2f", base) + "]");
                dataReady.release();
            }

        } catch (InterruptedException e) {
            System.out.println("Поток генератора Generator прерван");
        }
    }
}