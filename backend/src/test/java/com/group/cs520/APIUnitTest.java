import com.group.cs520.model.User;
import com.group.cs520.service.UserService;
import com.group.cs520.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.bson.types.ObjectId;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
public class APIUnitTest {

    @Autowired
    private UserService userService;

    @Test
    public void testGetRandomUsers() {
        List<User> randomUsers = userService.getRandomUsers(5);
        assertEquals(randomUsers.size(),5);
    }
}