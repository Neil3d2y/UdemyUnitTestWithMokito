package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class Test08Spies {
    private BookingService bookingService;

    private PaymentService paymentService;

    private RoomService roomService;

    private BookingDAO bookingDAO;

    private MailSender mailSender;

    @BeforeEach
    void setup() {
        this.paymentService = mock(PaymentService.class);
        this.roomService = mock(RoomService.class);
        this.bookingDAO = spy(BookingDAO.class);
        this.mailSender = mock(MailSender.class);
        this.bookingService = new BookingService(paymentService, roomService, bookingDAO, mailSender);
    }

    @Test
    void should_InvokePayment_when_input_OK() {
        // Correct pay = 800
        BookingRequest bookingRequest = new BookingRequest("2", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 9), 2, true);

        String bookingId = bookingService.makeBooking(bookingRequest);

        verify(bookingDAO).save(bookingRequest);
        System.out.println("bookingId: " + bookingId);
    }


    @Test
    void should_cancel_when_input_OK() {
        // Correct pay = 800
        BookingRequest bookingRequest = new BookingRequest("2", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 9), 2, true);

        bookingRequest.setRoomId("1.3");
        String bookingId = "1";

        // Modify the spy behaviour
        doReturn(bookingRequest).when(bookingDAO).get(bookingId);

        bookingService.cancelBooking(bookingId);
    }
}
