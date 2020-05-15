
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MainPageController {
    
    @Autowired
    SkillRepository skillRepository;
    
    @Autowired
    AccountRepository accountRepository;
    
    public Account currentAccount() {
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user.getUsername(); 

            return accountRepository.findByUsername(username);
    }
    
    @GetMapping("/users")
    public String allUsers(Model model) {
        model.addAttribute("accounts",accountRepository.findAll());
        return "users";
    }
    
    @GetMapping("/users/{username}")
    public String user(Model model, @PathVariable String username) {
        Account profileAccount = accountRepository.findByUsername(username);
        model.addAttribute("username", profileAccount.getUsername());
        model.addAttribute("name", profileAccount.getName());
        model.addAttribute("picture", profileAccount.getId());
        model.addAttribute("title", profileAccount.getTitle());
        
        Pageable pageable = PageRequest.of(0, 10, Sort.by("likes").descending());
        model.addAttribute("skills", skillRepository.findAll(pageable));
        
        return "user";
    }
    
    @GetMapping("/")
    public String home(Model model) {
        Account account= currentAccount();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("likes").descending());
        model.addAttribute("skills", skillRepository.findAll(pageable));
        model.addAttribute("username", account.getUsername());
        model.addAttribute("name", account.getName());
        model.addAttribute("picture", account.getId());
        model.addAttribute("title", account.getTitle());
        model.addAttribute("requests", account.getReceivedInvites());
        model.addAttribute("connections", account.getConnections());
        
        System.out.println(account.getSkills());
        

        return "index";
    }
    
    @PostMapping("/search") 
    public String search(@RequestParam String search) {  
            Account myAccount= currentAccount();
            
            //Toteuta joku haku, jonka perusteella näytetään tulokset. Ohjaa vaikka uuteen resurssiin
            //Account otherAccount = accountRepository.findByUsername(username);

               
            //accountRepository.save(myAccount);
            //accountRepository.save(otherAccount);
            return "redirect:/";  
    }
    
    @PostMapping("/accept") 
    public String accept(@RequestParam String username) {  
            Account myAccount= currentAccount();
            Account otherAccount = accountRepository.findByUsername(username);
            
            // tarkistus ollaanko jo kavereita
            
            List<Account> connections = myAccount.getConnections();
            connections.add(otherAccount);
            myAccount.setConnections(connections);
            
            List<Account> otherConnections = otherAccount.getConnections();
            otherConnections.add(myAccount);
            otherAccount.setConnections(otherConnections);
            
            List<Account> received = myAccount.getReceivedInvites();
            received.remove(otherAccount);
            myAccount.setReceivedInvites(received);
            
            List<Account> sent = otherAccount.getSentInvites();
            sent.remove(myAccount);
            otherAccount.setSentInvites(sent);
               
            accountRepository.save(myAccount);
            accountRepository.save(otherAccount);
            return "redirect:/";  
    }
    
    @PostMapping("/decline") 
    public String decline(@RequestParam String username) {  
            Account myAccount= currentAccount();
            Account otherAccount = accountRepository.findByUsername(username);
            
            //Toistoa acceptin kanssa. Siirra serviceen
            List<Account> received = myAccount.getReceivedInvites();
            received.remove(otherAccount);
            myAccount.setReceivedInvites(received);
            
            List<Account> sent = otherAccount.getSentInvites();
            sent.remove(myAccount);
            otherAccount.setSentInvites(sent);
            
            accountRepository.save(myAccount);
            accountRepository.save(otherAccount);
            
            return "redirect:/";  
    }
    @PostMapping("/remove")   
    public String remove(@RequestParam String username) {  
            Account myAccount= currentAccount();
            Account otherAccount = accountRepository.findByUsername(username);
            
            List<Account> myConnections = myAccount.getConnections();
            myConnections.remove(otherAccount);
            myAccount.setConnections(myConnections);
            
            List<Account> otherConnections = otherAccount.getConnections();
            otherConnections.remove(myAccount);
            otherAccount.setConnections(otherConnections);
            
            accountRepository.save(myAccount);
            accountRepository.save(otherAccount);
            
            return "redirect:/";  
    }
    
    
    
    @PostMapping("/connect") 
    public String connect(@RequestParam String username) {  
            Account myAccount= currentAccount();
            Account otherAccount = accountRepository.findByUsername(username);
            
               // Tarkista ollaanko jo kavereita
            
            List<Account> sent = myAccount.getSentInvites();
            sent.add(otherAccount);
            myAccount.setSentInvites(sent);
            
            List<Account> received = otherAccount.getReceivedInvites();
            received.add(myAccount);
            otherAccount.setReceivedInvites(received);
            
            accountRepository.save(myAccount);
            accountRepository.save(otherAccount);
            return "redirect:/users";  
    }
    
    @PostMapping("/title") 
    public String addTitle(@RequestParam String title) {  // formit parametrit otetaan vastaan requestParamina
            Account account= currentAccount();
            account.setTitle(title);
            accountRepository.save(account);
            return "redirect:/";  
    }
    
    @PostMapping("/skill") 
    public String addSkill(@RequestParam String name) {  // formit parametrit otetaan vastaan requestParamina
            Account account= currentAccount();
           // List<Skill> skills = account.getSkills();
           //skills.add(arg0)
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
        Account account= currentAccount();
        account.setProfilePicture(file.getBytes());
	accountRepository.save(account);
	
	return "redirect:/";
    }
    
    @GetMapping(path ="/gifs/{id}/content", produces = "image/jpg")
    @ResponseBody
    public byte[] content( @PathVariable Long id){
        return accountRepository.getOne(id).getProfilePicture();

    }   
    

    
}
