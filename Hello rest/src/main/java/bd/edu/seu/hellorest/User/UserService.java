package bd.edu.seu.hellorest.User;


import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor




public class UserService {

    private final UserRepository userRepository;


    public List<User> getAll(){
        IO.println("UserService getAll");
        String Url = "http://localhost:8080/users";
        RestTemplate restTemplate = new RestTemplate();
        User[] users = restTemplate.getForObject(Url, User[].class);
    return List.of(users);
    }
    @Cacheable(value = "users" , key = "#id")

    public User get(int id){
        IO.println("UserService get");

        String Url = "http://localhost:8080/users/"+id;
        return new User();
    }





}

