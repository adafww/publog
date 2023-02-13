package main;

import main.api.request.ProfileWithPhotoRequest;
import main.api.response.ErrorResponse;
import main.repository.UserRepository;
import main.service.ProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование RestoreService")
public class ProfileServiceTest {

    @Mock
    private UserRepository userRepo;

    @Test
    @DisplayName("Профиль обновлен")
    void editProfileTest(){

        String userEmail = "emailTest";
        String userName = "тестовоеИмя";
        String userPass = "testPass";
        String userEmailForChange = "emailTest1";
        ProfileWithPhotoRequest request = new ProfileWithPhotoRequest(
                userName,
                userEmailForChange,
                userPass,
                0);

        MultipartFile file = new MockMultipartFile("photo", Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAMAElEQVR4Xu2X+1uUZ3rH51/pD73aq427Mc216V4xW2PUqCAMMMwwDHM+z3CGYWYYYGBgOJ9BTnIUBQURFOSgYIzGJGo0rppEN5pkNdmcVo3xkGjafnpDststpm3avXq1P/SH7/u+877P+zyf93vfz32D4pWvv+W/rHs/Uavf+wlSrL7xk7V68dVaPf4n6r8PtKzVEH8mzLIUx+Twf0n/D/SfSbEkhz/V6gH/01q9/hNA/9tSLN2Viz/Vjwz6s/THOR/9+Pyr1lcsyuHHtHrgE1o98Y/ph7HHHv4Txx/+8795f/V6yzoqUhy98w2LXz/58D8CW7m//MV/0L0/6PG/6u4jFr+S2vLwO6bfOs3wKycF6h9l/OMn1lgGObJ8LXMoFr54yNLDJ0H+XQn8wq2HTF6+xeSFL0Wfc+D8pxw49wkTb4nOis58zMy1W7LAd7zyQAAWRtnZ2cLwm+c5dv87lr56xNHb36zMc0TOxwXytIw7J9eKmRsPmfvkW+a/+J508d4P+sG1Fevv/aD735+Pfv0NMx/cZvLSpyIBuHCT/edvsP/cbxk/+wFjr19j5uZdlr64yyvHpzk82EC4opm2mROcuiMfdO0rxs9/wYHLt5m9fp8Tnz/i1ZsPmLl6B8XgsY8YfeP3TL1zn7mbAvbp4xUt/P4RC58/Zva3jzn8odz7WCaSF+c+e8S8aEE0ff0uE5e/5MC74ta7d5h87w5Tl75kz/H3GVq6zuRbH7Jv7igDXTvo2jvFwu37LN16wNHf3efIR/eZvXKPqYt32X/pLpNXHjAnpij8mfmURVqoaR+nZeQU3Ycu0zd3heHjH3Lg4idMf3SLmY/vc/iGfMEH33Lo/W+YuvqAsbdu07HvAnV9J+g+fJWhkx8zMH+V1u45qmpHqW7cR9v+84wevcLYcD8N7TvpHp9n4OBrTJ37kolTn7Fbxu+evcjw3EW6p84xOHcZRcqmTZhiNmNQKknTmrHZ0snOzCO3MIw/Wkt5Rx81AxM0jxymc+4N+o9dYOfsWTrH3qC59xhNA6+y+8wNJq7cZs+hMzRGd1JRsmNFZdEBaisHGWhtIhyuoTBYT1l5F009h+jtGWdqcJy5oRH62nZSUFBFRVkzCnWSGn1CIrrtAqRMwKZOIseUgkFvQOfKJD3Xj17vwmDOQJdXSm6gnHxflIJQm+TFAJXtBxl67TqDJ65R3byPHH+9jGmgbBmouIPicBftjfK7rJEiuVdc3ES6rwxLhp+6shrmWjvprW4lkB3Bn1ctQCoNibEqtPEarNoUMoxairxGPBYLjpwCApEKLGYvKUYvsRlBrM4cTLYcjOlF2HOrcQfbqe6epaH/KA4BSXQF0XrF3UALZeFOhlvamO6rY7C1ls7aBpoqopSEikjPyMeXHWCwrIqB8lqqcyNEQxUoTDo1yu0qLElJOJKT8KSq8Dn1uGxWTHYvvsIiAfKgNrh52ZRJvM5FrFzHCGCiNR9nfg1N7aO090xgL6gmwRHEkVlOJNRKpTiy0FzD2e4IF/rKOD9YyumhEk4OlrDYU8RMWyH7G4KM1wTpDQbYE/WhSE5KwJycgDMhhrSt27ColGRbUrAbzXgkl0LhMCajgwSNlQ3JyzKzSWtns2hLqotUj4/q+g76+/dSXNEqIa4gVBClKlxPXWU74/W17I2UMhMt4XhtIeeaC7ncHeTKkGhPkHfHglwaD/D2viBnd/tRbIuNx6FNIi12OxqRXa2kwKbGY0zD6s6U5A6RqrUQq0xjo9bBiyojW7RWYvXuFaBkey7l0Rr2DY8wMrSHmkgdYX+EQn8p9RKK+fYou+uKGKwooStYyHikmL78LA6X5vPrRoFrLuB6VwHX+vO5OiJAsXGJJMclo1FqsGjVuLXx5FpUOExp6PRWcvJ96A12tsal8pLAxKjN6Cwe9PZMMvJ8+EIRWuqqmdjVy+T4OLt6emmprCc7I4QrK5vsXAfeTDNNxZkcaCpmb3UpfruLnlABJ1ujDPqyGc508UZZLu9U5UrIkpNJjFeRFq9Evy0GszIWb5oSu0GL0eYkPRDEZHURn6TjBYGKVZkwmtwkqS0rsIEicaKyhMGOekZ2jzIxMsxA+w56ujqId+h5UZfESxIBlezcsoCDg3UB2oIZDJYWcqyjlppsyVOzkb31kmfRfBQegwyOS8AhOaTcsIXkmBi8qXG4TVocbjfenBw0Oou4ZUGVbBAwPWaLG5XGjs6agzXdT05uLqVFQTraO5na3UlnfZTCigCbTWrWqRJ4QXJ0+ZySpmWyPJMLw4W8PlDKYksV5V43WQYTexoivF0uSZ24PU5CJfUnOR6V5FNqXAzpungyLKk4bXacDidqnQGTxYbL5cbp9uIXZypKQgRlB+bkhWipKmSoLcJgz04Ojo3R0liLym1kvTqRF5KUrEsUSY3botasAMyGsznfnM+ZrkJGKzNozHczKeE7HxGHNmyMQadarkNxGDVJWFKT/whkt9ux22w4nA6sZhsetwePJx2rxcqu9hwa6sO40n3sag0xO1RB745WDk9M0NPWhM5pZWOymueV8ayTorsuVsmv4hOJU2upspl5NZTL5cYCfjNawPmxIk72VX4fshc3bEUjQHp1MqkygSFFg1OfjEXqk1Yv4TGZyPA4VyA8LpdAukmR8Fnd6fhLysjI9jGyI8jS7jI6G2uYnTrA3v4+qstL5VkmKoOZmBQd21PTMInDdqeTXJl3pMjH2xUFvCuJ/M5AAVd3F4pD2bLtt2xFKds9JSkRq1GPUafDkJpCikaFUkDTUtRkeB0rYDaDEYPJRqpB4LLzKJQaVSQFbX4ozIm9pRK6Mg7s3cPUvhEJYTV7u+tprY0SKSmUnhamrSZCeUD6pMnIjpwMjvizOBPI5GJ5DjfbfJwo8qJIMm9gy+aXUW7aIqETKEOaQKWhlzZi1olbNpOUeQ8mgwGD3E/VG0kzWCgoDJLvD0gzdkl7yGNxOCSNtYTh3i4WpqeZ7CuWMOZTFSzAJeWjICsDn+SP02wmRZ1CqXSCbrkespg45vNws8rHvM+JYl3a3/PLF9exbdNLJMUKVLLUIKsZk9hql1qUnu7C5XGt/E7VpqIRWWwOqhtqcHozVtzaUZXOoV4fTQLUWlvB6VOvsbC/m5H2PKpDfoqyc8nzeMlxOHCYrayPScKk0dCs09OuSWXUY+ajynwO5TgESP0LnjU8za/0z7Fp+3riYuOwpqXitqTh8bpIz/Ris1owm02oU1JRa74vB1UNtXjTM8jPyeVwv58z40Haq0uoi0iCLi0wMzFGVXGA+rIQu1pqmO7fQY0Uw0y7tCGtnvXSoprzzZyIZjBf6ORGtY+JLAHaaHuWZx0/F6i1rN30HJu3voxVk0im2yJJmY7LvZzQFlLT0lCJ1eoULXlBP6HSEkK+XHGllJOjIS4dDNBZF6a2NMiu7jZeP74o9aiShrJC2qVwLo320N9QTmleFkPtdVRG8zjen82tuSLe687j8wY/+9JtKJ6z/4w1CT9nzda1PL39GTbq16O3aAXGs+KOVdzRS7gSBUYlMFqjkSZZsER2UZ48rysv5sRIkPdmCuiqF4ekAnc1RPndzRsc2DNIfWmApkiIoeZKuqrCklN5nJoY5L35Yd6fqmBPq5OJGg+fNQbY5TajWKN6iqe2/oy1cc/wi7S1rDP+kpf1W/AGbOxoiTLUWc/O9hp622vJlsTUCFyhwNgkbHrZdblZmSwM5vGbOR89DcUredRcEWJxdoqxXTtpFIeaK4qoCwfwywfkuJx0yG7c19nMruYIiQY1sSlKRsMOwi4dir96aQ1/s+VveSpOnIqT0KX8Hevdz5OQtY2GmhAzwz0cGu1eUUGGG09uDqU1UWknOpHUKaudgz1ZfHAkn97G0ApQY6mfcYH59bk3aasOyx9lISqLClZ2WqUkeXOkhDYJdXt1hGAwn00maV9eJXHGBBR/8fwa/vIfnhKt4a+3reHppKd5RrWW571r2eh6AbVFiVl6UI40yrqwhfqOVspqq0jWSK0SIIsAHdqZyY3FXAaailaAmiokj+T/sDu3bzE/OSbv5dMcLV5xarKvhTPTuzl9aJg3Raenh+nqlg/0atio2s6/AOdmndcGaTZ2AAAAAElFTkSuQmCC"));
        request.setPhoto(file);
        when(userRepo.existsByEmail(userEmailForChange)).thenReturn(false);
        when(userRepo.existsNameByEmail(userEmail, userName)).thenReturn(false);

        ErrorResponse response = new ProfileService(userRepo).editProfile(request, userEmail);

        verify(userRepo, times(1)).passUpdate(anyString(), anyString());
        verify(userRepo, times(1)).emailUpdate(anyString(), anyString());
        verify(userRepo, times(1)).nameUpdate(anyString(), anyString());
        verify(userRepo, times(1)).addPhoto(anyString(), anyString());

        Assertions.assertTrue(response.isResult());
    }

    @Test
    @DisplayName("Ошибки при редактироовании профиля")
    void editProfileErrorsTest(){

        String userEmail = "emailTest";
        String userName = "тестовоеИмя1";
        String userPass = "test";
        String userEmailForChange = "emailTest1";
        ProfileWithPhotoRequest request = new ProfileWithPhotoRequest(
                userName,
                userEmailForChange,
                userPass,
                0);

        when(userRepo.existsByEmail(userEmailForChange)).thenReturn(true);
        when(userRepo.existsNameByEmail(userEmail, userName)).thenReturn(false);

        ErrorResponse response = new ProfileService(userRepo).editProfile(request, userEmail);

        Assertions.assertFalse(response.isResult());
        Assertions.assertEquals(3, response.getErrors().size());
    }
}
