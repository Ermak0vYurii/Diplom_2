import pojo.User;
import com.github.javafaker.Faker;

public class UserGenerator {

    public static User getRandomUser() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().firstName();
        return new User(email, password, name);
    }
}