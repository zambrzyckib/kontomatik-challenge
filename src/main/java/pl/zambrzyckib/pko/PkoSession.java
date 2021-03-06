package pl.zambrzyckib.pko;

import io.vavr.collection.List;
import pl.zambrzyckib.connection.HttpAgent;
import pl.zambrzyckib.connection.JsoupConnection;
import pl.zambrzyckib.connection.Request;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.pko.request.PkoRequests;
import pl.zambrzyckib.pko.response.PkoResponseParser;
import pl.zambrzyckib.pko.response.body.LoginResponseBody;
import pl.zambrzyckib.pko.response.body.PasswordResponseBody;

public class PkoSession {

  private final HttpAgent httpAgent;

  private String sessionId;

  public PkoSession() {
    String homeUrl = "https://www.ipko.pl/";
    this.httpAgent = new JsoupConnection(homeUrl, true);
  }

  Response sendLoginRequest(String login) {
    Response loginResponse = httpAgent.send(PkoRequests.userLoginPostRequest(login));
    LoginResponseBody loginResponseBody =
        PkoResponseParser.deserializeLoginResponse(loginResponse.body);
    PkoResponseParser.assertLoginCorrect(loginResponseBody);
    saveSessionId(loginResponse);
    return loginResponse;
  }

  AuthenticatedPkoSession sendPasswordRequest(Response sendLoginResponse, String password) {
    Response passwordResponse =
        httpAgent.send(PkoRequests.userPasswordPostRequest(password, sessionId, sendLoginResponse));
    PasswordResponseBody passwordResponseBody =
        PkoResponseParser.deserializePasswordResponse(passwordResponse.body);
    PkoResponseParser.assertPasswordCorrect(passwordResponseBody);
    PkoResponseParser.assertSignedIn(passwordResponseBody);
    return new PkoSession.AuthenticatedPkoSession(httpAgent, sessionId);
  }

  private void saveSessionId(Response response) {
    this.sessionId = response.headers.get("X-Session-Id");
  }

  public static class AuthenticatedPkoSession {

    private final HttpAgent httpAgent;
    private final String sessionId;

    private AuthenticatedPkoSession(HttpAgent httpAgent, String sessionId) {
      this.httpAgent = httpAgent;
      this.sessionId = sessionId;
    }

    List<AccountSummary> fetchAccounts() {
      Response accountsInfoResponse =
          httpAgent.send(PkoRequests.accountsInfoPostRequest(sessionId));
      return PkoResponseParser.parseAccountSummaries(accountsInfoResponse);
    }
  }
}
