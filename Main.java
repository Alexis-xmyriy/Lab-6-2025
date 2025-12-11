import functions.*;
import functions.basic.*;
import java.util.concurrent.Semaphore;
import threads.*;

public class Main {
    public static void main(String[] args) {      
        System.out.println("\n=== Лабораторная работа №6 ===");
        
        System.out.println("\n--- Задание 1: Тестирование численного интегрирования экспоненты ---");
        testIntegration();
        
        //System.out.println("\n--- Задание 2: Последовательная версия программы ---");
        //nonThread();
        
        //System.out.println("\n--- Задание 3: Простая многопоточная версия с синхронизацией ---");
        //simpleThreads();
        
        System.out.println("\n--- Задание 4: Усовершенствованная версия с семафорами ---");
        complicatedThreads();
    }

    // Задание 1: Тестирование интегрирования
    private static void testIntegration() {
        try {
            Function exp = new Exp();
            
            System.out.println("Интегрирование функции exp(x) на интервале [0, 1]:");
            // Теоретическое значение интеграла exp(x) от 0 до 1 = e - 1 ≈ 1.718281828
            double theoretical = Math.E - 1;
            System.out.println("Теоретическое значение интеграла: " + theoretical + " (e - 1)");
            
            System.out.println("\nВычисление с разными шагами дискретизации:");
            double[] steps = {0.001, 0.0001, 0.00001};
            
            for (double step : steps) {
                double result = Functions.integrate(exp, 0, 1, step);
                double error = Math.abs(result - theoretical);
                System.out.printf("   Шаг %8.5f: результат = %.10f, ошибка = %.10f", 
                    step, result, error);
                
                // Проверяем 7 знак после запятой
                if (error < 1e-7) {
                    System.out.println("\nВывод: Шаг " + step + " обеспечивает точность до 7 знака после запятой");
                    break;
                }
            }  
        } catch (Exception e) {
            System.out.println("Ошибка при тестировании интегрирования: " + e.getMessage());
        }
    }
    
    // Задание 2: Последовательная версия
    private static void nonThread() {
        System.out.println("\nПоследовательная версия программы (без потоков):");
        
        Task task = new Task();
        task.setTasksCount(100); // Для демонстрации 100 заданий
        
        for (int i = 0; i < task.getTasksCount(); i++) {
            System.out.println("\n--- Задание " + (i+1) + " ---");
            
            // Генерируем случайные параметры
            double base = 1 + Math.random() * 9;   // основание логарифма от 1 до 10
            double left = Math.random() * 100;     // левая граница от 0 до 100
            double right = 100 + Math.random() * 100; // правая граница от 100 до 200
            double step = Math.random();           // шаг от 0 до 1
            
            System.out.println("Параметры: log_" + String.format("%.2f", base) + 
                             "(x) на [" + String.format("%.2f", left) + 
                             ", " + String.format("%.2f", right) + 
                             "] с шагом " + String.format("%.4f", step));
            
            // Создаем логарифмическую функцию
            Function logFunc = new Log(base);
            
            // Устанавливаем задание
            task.setFunction(logFunc);
            task.setLeft(left);
            task.setRight(right);
            task.setStep(step);
            
            // Выводим сообщение о задании
            System.out.println("Сообщение: Source " + 
                String.format("%.2f", left) + " " + 
                String.format("%.2f", right) + " " + 
                String.format("%.4f", step));
            
            try {
                // Вычисляем интеграл
                double result = Functions.integrate(logFunc, left, right, step);
                
                // Выводим результат
                System.out.println("Результат: Result " + 
                    String.format("%.2f", left) + " " + 
                    String.format("%.2f", right) + " " + 
                    String.format("%.4f", step) + " " + 
                    String.format("%.6f", result));
                
            } catch (Exception e) {
                System.out.println("Ошибка вычисления: " + e.getMessage());
            }
        }
    }
    
    // Задание 3: Простая многопоточная версия
    private static void simpleThreads() {
        System.out.println("\nПростая многопоточная версия:");
        System.out.println("Один поток генерирует задания, другой - решает их");
        
        Task task = new Task();
        task.setTasksCount(100); // 100 заданий для демонстрации
        
        System.out.println("\nСоздаем два потока:");
        System.out.println("1. SimpleGenerator - генерирует задания");
        System.out.println("2. SimpleIntegrator - решает задания");
        
        Thread generatorThread = new Thread(new SimpleGenerator(task));
        Thread integratorThread = new Thread(new SimpleIntegrator(task));
        
        // Установка приоритетов
        generatorThread.setPriority(Thread.MIN_PRIORITY);
        integratorThread.setPriority(Thread.MAX_PRIORITY);
        System.out.println("Установлены нормальные приоритеты для потоков");
        
        System.out.println("\nЗапускаем потоки...");
        System.out.println("Ожидаемые сообщения:");
        System.out.println("  'Создано задание ...' - от генератора");
        System.out.println("  'Результат ...' - от интегратора");
        
        // Запуск потоков
        integratorThread.start();
        generatorThread.start();

        System.out.println("\nПростая многопоточная версия завершена");
        System.out.println("Примечание: Из-за синхронизации сообщения могут выводиться в разном порядке");
    }
    
    // Задание 4: Усовершенствованная многопоточная версия
    private static void complicatedThreads() {
        System.out.println("\nУсовершенствованная многопоточная версия с семафорами:");
        System.out.println("Используется семафор для более тонкой синхронизации");
        
        Task task = new Task();
        task.setTasksCount(100); // 100 заданий для демонстрации
        Semaphore dataReady = new Semaphore(0);
        Semaphore dataProcessed = new Semaphore(1);
        
        Generator generator = new Generator(task, dataReady, dataProcessed);
        Integrator integrator = new Integrator(task, dataReady, dataProcessed);       
        // Запуск потоков
        generator.start();
        integrator.start();
        
        try {
            // Ждем 50 миллисекунд
            Thread.sleep(50);
            // Прерываем потоки
            generator.interrupt();
            integrator.interrupt();   
            // Ожидаем завершения потоков
            generator.join();
            integrator.join();
            
        } catch (InterruptedException e) {
            System.out.println("Основной поток был прерван");
        }
        
        System.out.println("\nУсовершенствованная многопоточная версия завершена");
        System.out.println("Семафор позволил более эффективно управлять доступом к общему ресурсу");
    }
}