package com.mockitotutorial.happyhotel.booking.advanced;

import com.mockitotutorial.happyhotel.booking.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class Test01ArgumentCaptors {

    private BookingService bookingService;

    private PaymentService paymentService;

    private RoomService roomService;

    private BookingDAO bookingDAO;

    private MailSender mailSender;

    private ArgumentCaptor<Double> argumentCaptor;

    @BeforeEach
    void setup() {
        this.paymentService = mock(PaymentService.class);
        this.roomService = mock(RoomService.class);
        this.bookingDAO = spy(BookingDAO.class);
        this.mailSender = mock(MailSender.class);
        this.bookingService = new BookingService(paymentService, roomService, bookingDAO, mailSender);

        this.argumentCaptor = ArgumentCaptor.forClass(Double.class);
    }


    @Test
    void should_PayCorrectPrice() {
        // Correct pay = 800
        BookingRequest bookingRequest = new BookingRequest("2", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 9), 2, true);

        bookingService.makeBooking(bookingRequest);

        verify(paymentService, times(1)).pay(eq(bookingRequest), argumentCaptor.capture());
        double capturedArg = argumentCaptor.getValue();

        assertEquals(capturedArg, 800.0);
    }

    @Test
    void should_PayCorrectPrice_Multiple_Args() {
        // Correct pay = 800
        BookingRequest bookingRequest = new BookingRequest("2", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 9), 2, true);
        BookingRequest bookingRequest2 = new BookingRequest("2", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 9), 2, true);
        bookingService.makeBooking(bookingRequest);
        bookingService.makeBooking(bookingRequest2);

        verify(paymentService, times(2)).pay(any(), argumentCaptor.capture());
        List<Double> capturedArgs = argumentCaptor.getAllValues();

        List<Double> expected = Arrays.asList(800.0, 800.0);
        assertEquals(expected, capturedArgs);
    }

}
