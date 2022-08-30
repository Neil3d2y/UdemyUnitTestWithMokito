package com.mockitotutorial.happyhotel.booking.advanced;

import com.mockitotutorial.happyhotel.booking.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Test04StrictStubbing {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RoomService roomService;

    @Spy
    private BookingDAO bookingDAO;

    @Mock
    private MailSender mailSender;

    @Captor
    private ArgumentCaptor<Double> argumentCaptor;

    @Test
    void should_InvokePayment_when_prepaid() {
        // Correct pay = 800
        BookingRequest bookingRequest = new BookingRequest("2", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 9), 2, false);

        /**
         * if (bookingRequest.isPrepaid()) {
         * 			paymentService.pay(bookingRequest, price);
         *  }
         */
        // Won't invoke at all, Mockito will detect the stubbing and throw exceptions
        lenient().when(paymentService.pay(any(), anyDouble())).thenReturn("1");
        bookingService.makeBooking(bookingRequest);

    }
}


