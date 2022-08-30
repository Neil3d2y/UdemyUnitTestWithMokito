package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class Test09MockVoidMethod {
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
    void should_throwException_when_noAvailability() {
        BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 9), 2, false);

        doThrow(new BusinessException()).when(mailSender).sendBookingConfirmation(any());

        Executable executable = () -> bookingService.makeBooking(bookingRequest);

        assertThrows(BusinessException.class, executable);
    }

    @Test
    void should_Not_throwException_when_Availability() {
        BookingRequest bookingRequest = new BookingRequest("1", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 9), 2, false);

        doNothing().when(mailSender).sendBookingConfirmation(any());
    }
}
