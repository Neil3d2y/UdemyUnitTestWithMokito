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

### Advanced Topics

1. Argument Captors

```java
private ArgumentCaptor<Double> argumentCaptor;

\\ use to capture arg passed in 
\\ ^ is matcher type, so need to wrap other inputs to `eq()`

        verify(paymentService, times(1)).pay(eq(bookingRequest), argumentCaptor.capture());
        double capturedArg = argumentCaptor.getValue();

        assertEquals(capturedArg, 800.0);
```


2. Annotations

```java
@Mock

@InjectMocks

@Spy

@Captor

@ExtendWith(MockitoExtension.class)

// In Junit4, replace above with @RunWith
```

3. Mockito BDD

Behaviour-Driven-Development

`import org.mockito.BDDMockito.*;`

New Alias:

| Old Name           | New Name                 |
|--------------------|--------------------------|
| `when().return()`  | `give().willreturn()`    |
| `verify().method()` | then().should().method() |


4. Strict Stubbing

Whenever use the `MockitoExtension` introduced alongside Mockito annotations, it turns on the `strict stubbing` feature by default.


```java
when(paymentService.pay(any(), anyDouble())).thenReturn("1");

    ...org.mockito.exceptions.misusing.UnnecessaryStubbingException: 
    Unnecessary stubbings detected.
    Clean & maintainable test code requires zero unnecessary code.

// To allow the stubbing
lenient().when(paymentService.pay(any(), anyDouble())).thenReturn("1");
```

5. Mock Static Method (experimental and turnoff by default)

```java
public class CurrencyConverter {

	private static final double USD_TO_EUR_RATE = 0.85;
	
	public static double toEuro(double dollarAmount) {
		return dollarAmount * USD_TO_EUR_RATE; 
	}
	
}
```

Because the Converter is a static method, so our `BookingService` doesn't have a dependency to it directly, 
how to mock that the method use `toEuro` return a specific value?

```java
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
```

Use try to make the `MockedStatic` a temp variable.

6. Make static method mock better

```java
// Use `thenAnswer` when need dynamic values from runtime

Mockito.when(mock.getName() ).thenReturn(“Mockito”)

// return Data every time when run it
Mockito.when (mock.getCurrentTime() ).thenAnswer(I -> new Date() );


```

https://stacktraceguru.com/unittest/thenreturn-vs-thenanswer

When we should use thenReturn and when thenAnswer?

The simplest answer is - if you need fixed return value on method call then we should use thenReturn(…)

If you need to perform some operation or the value need to be computed at run time then we should use thenAnswer(…)

7. Mocking `Final` and `Private` methods

Private: *Cannot*

Final: Use `Mockito-Inline` (as it's experimental)