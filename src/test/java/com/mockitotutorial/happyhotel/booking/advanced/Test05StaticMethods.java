package com.mockitotutorial.happyhotel.booking.advanced;

import com.mockitotutorial.happyhotel.booking.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class Test05StaticMethods {

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
    void should_Calculate_Correct_Price() {
        try (MockedStatic<CurrencyConverter> mockedConverter = mockStatic(CurrencyConverter.class)) {
            // Correct pay = 800
            BookingRequest bookingRequest = new BookingRequest("2", LocalDate.of(2022, 1, 1),
                    LocalDate.of(2022, 1, 9), 2, false);

            double expected = 800.0;

            mockedConverter.when(() -> CurrencyConverter.toEuro(anyDouble())).thenReturn(800.0);

            double actual = bookingService.calculatePriceEuro(bookingRequest);

            assertEquals(expected, actual);
        }
    }
}
