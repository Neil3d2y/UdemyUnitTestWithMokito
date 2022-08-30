package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Test03ReturningCustomValues {
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
    void should_CountAvailablePlaces_When_OneRoom() {
        when(this.roomService.getAvailableRooms())
                .thenReturn(Collections.singletonList(new Room("Room 1", 2)));

        int expected = 2;

        int actual = bookingService.getAvailablePlaceCount();

        assertEquals(expected, actual);
    }

    @Test
    void should_CountAvailablePlaces_When_MultipleRoom() {
        List<Room> roomList = Arrays.asList(new Room("Room1", 2), new Room("Room2", 5));
        when(this.roomService.getAvailableRooms())
                .thenReturn(roomList);

        int expected = 7;

        int actual = bookingService.getAvailablePlaceCount();

        assertEquals(expected, actual);
    }
}
