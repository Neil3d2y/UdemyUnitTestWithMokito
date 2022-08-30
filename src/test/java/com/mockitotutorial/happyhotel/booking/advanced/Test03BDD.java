package com.mockitotutorial.happyhotel.booking.advanced;

import com.mockitotutorial.happyhotel.booking.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Test03BDD {

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
    void should_CountAvailablePlaces_When_OneRoom() {
        given(this.roomService.getAvailableRooms())
                .willReturn(Collections.singletonList(new Room("Room 1", 2)));

        int expected = 2;

        int actual = bookingService.getAvailablePlaceCount();

        assertEquals(expected, actual);
    }

    @Test
    void should_InvokePayment_when_prepaid() {
        // Correct pay = 800
        BookingRequest bookingRequest = new BookingRequest("2", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 9), 2, true);

        bookingService.makeBooking(bookingRequest);

        // verify the paymentService is called
        then(paymentService).should(times(1)).pay(bookingRequest, 800.0);

        // check if any other methods called
        verifyNoMoreInteractions(paymentService);
    }
}

