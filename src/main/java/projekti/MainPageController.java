
package projekti;


import java.io.IOException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MainPageController {
    
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    AccountRepository accountRepository;
    
    @GetMapping("/")
    public String home(Model model) {
        Account account = accountRepository.getOne(1L);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("likes").descending());
        model.addAttribute("skills", skillRepository.findAll(pageable));
        model.addAttribute("picture", 1);
        model.addAttribute("username", account.getName());
        return "index";
    }
    
    @PostMapping("/skill") 
    public String addSkill(@RequestParam String name) {  // formit parametrit otetaan vastaan requestParamina
            //System.out.println(name);
            skillRepository.save(new Skill(name, 1)); 
            return "redirect:/";  
    }
    
    @PostMapping("/") 
    public String incrementLikes(@RequestParam Long id) {  // formit parametrit otetaan vastaan requestParamina
            //System.out.println(id);
            Skill skill = skillRepository.getOne(id);
            skill.setLikes(skill.getLikes()+1);
            skillRepository.save(skill); 
            return "redirect:/";  
    }
    
    @PostMapping("/picture")
    public String save(@RequestParam("file") MultipartFile file) throws IOException {
         Account account = accountRepository.getOne(1L);
        account.setProfilePicture(file.getBytes());
	accountRepository.save(account);
	
	return "redirect:/";
    }
    
    @GetMapping(path ="/gifs/{id}/content", produces = "image/jpg")
    @ResponseBody
    public byte[] content( @PathVariable Long id){
        //System.out.println(id);
        return accountRepository.getOne(id).getProfilePicture();

    }   
    

    
}
