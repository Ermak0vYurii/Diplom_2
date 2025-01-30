import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseAuth {
    private Boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;
}
