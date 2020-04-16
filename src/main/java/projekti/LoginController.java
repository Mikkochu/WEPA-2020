package projekti;

import org.springframework.stereotype.Controller;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class LoginController {
    
    
    @GetMapping("/login")
    public String showLogin(Model model) {

        return "login";
    }
    
    @GetMapping("/signup")
    public String showSignup(Model model) {

        return "signup";
    }
    
    @PostMapping("/login") 
    public String login(@RequestParam String username, @RequestParam String password) {  // formparametrit otetaan vastaan requestParamina
        System.out.println(username);
        System.out.println(password);
        //skillRepository.save(new Skill(name, 1)); 
        return "redirect:/";  
    }
    
    @PostMapping("/signup") 
    public String signup(@RequestParam String name, @RequestParam String username, @RequestParam String password) {  // formparametrit otetaan vastaan requestParamina
        System.out.println(name);
        System.out.println(username);
        System.out.println(password);
        //skillRepository.save(new Skill(name, 1)); 
        return "redirect:/signup";  
    }
    
    
}
