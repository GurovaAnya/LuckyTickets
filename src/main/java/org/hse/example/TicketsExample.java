package org.hse.example;

import org.hse.example.service.*;

import java.util.Optional;

/**
 * Реализация примера со счастливыми билетами
 */
public class TicketsExample {

    /**
     * Точка входа
     *
     * @param args строка аргументов. В настоящее время не используется
     */
    public static void main(String[] args) {
        System.out.println("Hello world!");
        NearestTicketsBuilder builder = () -> 6;

        //builder.build().doWork().printResult();


        NearestTickets nearest = new NearestTickets(6, sum -> sum % 3 == 0);
        nearest.doWork().printResult();
    }

}
