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

```java
when(this.roomService.findAvailableRoomId(bookingRequest))
                .thenThrow(BusinessException.class);
```

5. Argument Matchers

Use `any()` for objects;

Use `anyDouble()` for primitive specific values

> When we use argument matchers, then all the arguments should use matchers. If we want to use a specific value for an argument, then we can use `eq()` method.

Use `eq(800)` for specific value.

Note: `eq(800)` != `eq(800.0); former is integer, second is double

> `anyString()` will not match a `Null`

#### Match Rules

1) Use `any()` for objects, use `anyDouble()` `anyBoolean()` for primitive
2) Use `eq()` to mix matches and concrete values `method(any(), eq(400.0))`
3) For nullable Strings, use `any()`

6. Verifying Behaviour

Verify that method is called, called how many times..
Check side effects with Mokito

```java
verify(paymentService).pay(bookingRequest, 800.0);

// check if any other methods called
verifyNoMoreInteractions(paymentService);

// verify called how many times
verify(paymentService, times(1)).pay(bookingRequest, 800.0);

// verify never called
verify(paymentService, never()).pay(any(), anyDouble());

```

7. Spies (AKA: *Partial Mocks*)

|name| note                                           |
|---|------------------------------------------------|
|mock| dummy object with no real logic                |
|spy| real object with real logic that we can modify |


```java
        // Modify the spy behaviour
        doReturn(bookingRequest).when(bookingDAO).get(bookingId);
```

:+1::+1::+1: `doReturn` vs `whenReturn`

http://sangsoonam.github.io/2019/02/04/mockito-doreturn-vs-thenreturn.html

```java
List list = new LinkedList();
List spy = spy(list);

// Impossible: real method is called so spy.get(0)
// throws IndexOutOfBoundsException (the list is yet empty)
when(spy.get(0)).thenReturn("foo");

// You have to use doReturn() for stubbing
doReturn("foo").when(spy).get(0);
```

A spied object is linked to an actual object. So, there is a real method invocation when you call a method.

> doReturn doesn't have real method call. No side effect!


8. Mocking Void Methods

`whenReturn` cannot use for methods have `void` return type.

```java
doThrow(new BusinessException()).when(mailSender).sendBookingConfirmation(any());
```

Test no Exception is thrown

```java
doNothing()
```
