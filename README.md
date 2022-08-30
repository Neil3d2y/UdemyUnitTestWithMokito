# Unit Testing with Mockito

### Mockito Basics

1. Create Mock

```java
import static org.mockito.Mockito.*;

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
```

2. Return Custom Value

```java
when(this.roomService.getAvailableRooms())
                .thenReturn(Collections.singletonList(new Room("Room 1", 2)));
```

3. Multiple `thenReturn` Calls
```java
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
```

Append the `thenReturn` and call the mock multiple times


4. Throwing Exceptions 
