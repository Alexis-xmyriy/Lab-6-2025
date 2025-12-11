package threads;

import functions.Function;
import functions.Functions;
import java.util.concurrent.Semaphore;

// Интегратор, расширяющий класс Thread
public class Integrator extends Thread {
    private final Task task;
    private final Semaphore dataReady;
    private final Semaphore dataProcessed;

    public Integrator(Task task, Semaphore dataReady, Semaphore dataProcessed){
        this.task = task;
        this.dataReady = dataReady;
        this.dataProcessed = dataProcessed;
    }
    
    @Override
    public void run() {
        try {
            // Обработка заданий в цикле
            for (int i = 0; i < task.getTasksCount(); i++) {
                // Проверка прерывания потока

                // Начинаем чтение (могут быть несколько читателей одновременно)
                dataReady.acquire();
                // Получаем параметры задания
                Function func = task.getFunction();
                double left = task.getLeft();
                double right = task.getRight();
                double step = task.getStep();
                
                // Вычисляем интеграл
                double result = Functions.integrate(func, left, right, step);
                
                // Выводим результат с комментариями
                System.out.println("Результат интегрирования: левая граница = " + String.format("%.2f", left) + 
                                 ", правая граница = " + String.format("%.2f", right) + 
                                 ", шаг = " + String.format("%.4f", step) + 
                                 ", интеграл = " + String.format("%.6f", result));
                dataProcessed.release();
                
            }
        } catch (InterruptedException e) {
            System.out.println("Поток интегратора Integrator прерван");
        } catch (Exception e) {
            System.out.println("Ошибка интегрирования: " + e.getMessage());
        }
    }
}