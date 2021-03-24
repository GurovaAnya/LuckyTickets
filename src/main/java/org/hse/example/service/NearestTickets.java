package org.hse.example.service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Находит минимальное расстояние между счастливыми билетами
 */
public class NearestTickets implements TicketService {
    private int maxNumber;
    private int[] digits;
    private boolean done = false;
    private int ticket = 0;
    private int distance;
    private Optional<Predicate<Integer>> condition= Optional.empty();


    /**
     * Конструктор
     * @param digitsQnty количество цифр в билете
     * @param condition условие слияние
     */
    public NearestTickets(int digitsQnty, Predicate<Integer> condition) {

        if (digitsQnty <= 0 || digitsQnty % 2 != 0) {
            throw new IllegalArgumentException("Передан некорректный параметр! " + digitsQnty);
        }

        this.maxNumber = (int) (Math.pow(10, digitsQnty) - 1);
        this.digits = new int[digitsQnty];
        this.distance = this.maxNumber;
        this.condition = Optional.of(condition);
    }

    /**
     * Выполняет необходимые вычисления
     *
     * @return экземпляр {@link TicketService}
     */
    @Override
    public TicketService doWork() {
        if(done){
            throw new IllegalStateException("Уже выполнено!");
        }

        IntStream
            .rangeClosed(1, maxNumber)
                // пользовательский фильтр
            .filter(number ->
                    this.condition
                        .map(value -> value.test(number))
                        .orElse(true))
            .filter(this::isLucky)
            .forEach(this::getDistance);

        done = true;
        return this;
    }

    private void getDistance(int currentTicket) {
        int currentDistance = currentTicket - ticket;
        if(currentDistance < distance) {
            distance = currentDistance;
            ticket = currentTicket;
        }
    }

    /**
     * @param ticket номер проверяемого билета
     * @return true, если билет счастливый
     */
    private boolean isLucky(int ticket) {
        Arrays.fill(this.digits, 0);
        for (int i = 0, nextNumber = ticket; nextNumber > 0; nextNumber /= 10, i++) {
            this.digits[i] = nextNumber % 10;
        }
        int firstSum = IntStream
                            .range(0, digits.length/2)
                            .map(i -> digits[i])
                            .sum();

        int lastSum = IntStream
                            .range(digits.length/2, digits.length)
                            .map(i -> digits[i])
                            .sum();

        return lastSum == firstSum;
    }

    /**
     * Выводит результат работы объекта
     */
    @Override
    public void printResult() {
        if (!done) {
            throw new IllegalStateException("Нечего выводить!");
        }

        String formattedTicket = "%0" + this.digits.length + "d\t";
        System.out.printf("Минимальное расстояние %d\t" + formattedTicket + formattedTicket,
                this.distance,
                this.ticket,
                (this.ticket - this.distance));

        // Всем привет, это Аня
    }
}
