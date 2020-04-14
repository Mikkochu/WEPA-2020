
package projekti;


import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MainPageController {
    
    @Autowired
    SkillRepository skillRepository;
    
    @GetMapping("/")
    public String home(Model model) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("likes").descending());
        model.addAttribute("skills", skillRepository.findAll(pageable));
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
    
}
