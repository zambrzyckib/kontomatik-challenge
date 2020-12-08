package pl.zambrzyckib.pko.response.body;

import com.google.gson.annotations.SerializedName;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponseBody {
  public final String token;

  @SerializedName("flow_id")
  public final String flowId;

  public Response response;

  @Getter
  public class Response {
    Fields fields;

    @Getter
    public class Fields {
      Login login;

      @Getter
      public class Login {
        Errors errors;

        @Getter
        public class Errors {
          String hint;
        }
      }
    }
  }
}
