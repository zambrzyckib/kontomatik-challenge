package pl.zambrzyckib.pko;

import io.vavr.collection.List;
import pl.zambrzyckib.UserInterface;
import pl.zambrzyckib.connection.Response;
import pl.zambrzyckib.model.AccountSummary;
import pl.zambrzyckib.model.Credentials;

public class PkoScraper {

  private final PkoSession pkoSession;
  private final UserInterface userInterface;

  public PkoScraper(UserInterface userInterface) {
    this.userInterface = userInterface;
    this.pkoSession = new PkoSession();
  }

  public void getAndDisplayAccountsInfo(Credentials credentials) {
    List<AccountSummary> accountsSummaries = fetchAccountSummaries(credentials);
    userInterface.displayAccountSummaries(accountsSummaries);
  }

  private List<AccountSummary> fetchAccountSummaries(Credentials credentials) {
    PkoSession.AuthenticatedPkoSession authenticatedPkoSession = login(credentials);
    return authenticatedPkoSession.fetchAccounts();
  }

  private PkoSession.AuthenticatedPkoSession login(Credentials credentials) {
    Response loginResponse = pkoSession.sendLoginRequest(credentials.login);
    return pkoSession.sendPasswordRequest(loginResponse, credentials.password);
  }
}
