package threads;

import functions.Function;
import functions.Functions;

// Простой интегратор, реализующий интерфейс Runnable
public class SimpleIntegrator implements Runnable {
    private final Task task;
    
    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        try {
            // Обработка заданий в цикле
            for (int i = 0; i < task.getTasksCount(); i++) {

                synchronized (task) {
                    while(!task.taskReady()){
                        task.wait();
                    }

                    // Ждем, пока появится задание
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
                    
                    // Помечаем задание как выполненное
                    task.resetTask();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Ошибка интегрирования: " + e.getMessage());
        }
    }
}