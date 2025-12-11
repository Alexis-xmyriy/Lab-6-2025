package threads;

import functions.Function;
import functions.basic.Log;

// Простой генератор заданий, реализующий интерфейс Runnable
public class SimpleGenerator implements Runnable {
    private final Task task;
    
    public SimpleGenerator(Task task) {
        this.task = task;
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
                    
                    // Создаем логарифмическую функцию
                    Function logFunc = new Log(base);
                synchronized (task) {
                    // Устанавливаем задание
                    task.setFunction(logFunc);
                    task.setLeft(left);
                    task.setRight(right);
                    task.setStep(step);
                    // Выводим сообщение о созданном задании с комментариями
                    System.out.println("Создано задание: левая граница = " + String.format("%.2f", left) + 
                                     ", правая граница = " + String.format("%.2f", right) + 
                                     ", шаг = " + String.format("%.4f", step) + 
                                     "   [Основание логарифма: " + String.format("%.2f", base) + "]");
                }
                Thread.sleep(2);
            }
        } catch (InterruptedException e) {
            System.out.println("Ошибка генерации: " + e.getMessage());
        }
    }
}