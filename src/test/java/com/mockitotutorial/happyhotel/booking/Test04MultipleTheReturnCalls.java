package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Test04MultipleTheReturnCalls {
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
    void should_CountAvailablePlaces_When_MultipleRoom() {
        List<Room> roomList = Arrays.asList(new Room("Room1", 2), new Room("Room2", 5));
        when(this.roomService.getAvailableRooms())
                .thenReturn(roomList)
                .thenReturn(Collections.EMPTY_LIST);

        int expectedFirst = 7;

        int expectedSecond = 0;

        int actualFirst = bookingService.getAvailablePlaceCount();

        int actualSecond = bookingService.getAvailablePlaceCount();

        assertAll(
                () -> assertEquals(expectedFirst, actualFirst),
                () -> assertEquals(expectedSecond, actualSecond)
        );
    }

}
