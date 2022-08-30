package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class Test07VerifyingBehaviour {
    private BookingService bookingService;

    private PaymentService paymentService;

    private RoomService roomService;

    private BookingDAO bookingDAO;

    private MailSender mailSender;

    @BeforeEach
    void setup() {
        this.paymentService = mock(PaymentService.class);
        this.roomService = mock(RoomService.class);
        this.bookingDAO = mock(BookingDAO.class);
        this.mailSender = mock(MailSender.class);
        this.bookingService = new BookingService(paymentService, roomService, bookingDAO, mailSender);
    }

    @Test
    void should_InvokePayment_when_prepaid() {
        // Correct pay = 800
        BookingRequest bookingRequest = new BookingRequest("2", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 9), 2, true);

        bookingService.makeBooking(bookingRequest);

        // verify the paymentService is called
        verify(paymentService).pay(bookingRequest, 800.0);

        // check if any other methods called
        verifyNoMoreInteractions(paymentService);
    }

    @Test
    void should_Not_InvokePayment_when_NOT_prepaid() {
        // Correct pay = 800
        BookingRequest bookingRequest = new BookingRequest("2", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 9), 2, false);

        bookingService.makeBooking(bookingRequest);

        // verify the paymentService is called
        verify(paymentService, never()).pay(any(), anyDouble());
    }
}
