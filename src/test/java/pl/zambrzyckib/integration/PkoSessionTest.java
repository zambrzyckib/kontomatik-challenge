package pl.zambrzyckib.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.zambrzyckib.integration.PkoIntegrationTestSpec.loadCredentials;

import org.junit.jupiter.api.Test;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.exception.InvalidCredentials;
import pl.zambrzyckib.model.Credentials;
import pl.zambrzyckib.pko.PkoSession;

public class PkoSessionTest {

  private final PkoSession pkoSession = new PkoSession();
  private final Credentials pkoTestCredentials = loadCredentials();

  @Test
  void whenPasswordIsWrongExceptionShouldBeThrown() {
    var wrongPassword = "test";
    Response loginResponse = pkoSession.sendLoginRequest(pkoTestCredentials.login);
    pkoSession.setSessionId(loginResponse.headers.get("X-Session-Id"));
    assertThrows(
        InvalidCredentials.class,
        () -> pkoSession.sendPasswordRequest(loginResponse, wrongPassword));
  }

  @Test
  void whenLoginIsWrongExceptionShouldBeThrown() {
    var wrongLogin = "test";
    assertThrows(InvalidCredentials.class, () -> pkoSession.sendLoginRequest(wrongLogin));
  }
}
