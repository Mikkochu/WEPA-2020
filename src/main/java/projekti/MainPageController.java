package projekti;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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
    CommentRepository commentRepository;

    @Autowired
    PostLikeRepository likeRepository;
    
    @Autowired
    PostRepository postRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    AccountRepository accountRepository;

    public Account currentAccount() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        return accountRepository.findByUsername(username);
    }

    
    @PostMapping("/comments")
    public String addComment(@RequestParam String commentText,@RequestParam Long postId) {

        Account account = currentAccount();
        Post post = postRepository.getOne(postId);
        Comment comment = new Comment();
        comment.setText(commentText);
        comment.setAccount(account);
        comment.setPost(post);

        commentRepository.save(comment);

        return "redirect:/posts";
    }
    
    @PostMapping("/postLikes")
    public String incrementPostLikes(@RequestParam Long id) {
        Account account = currentAccount();
        
        Post post = postRepository.getOne(id);
        
        PostLike like = new PostLike(account, post);
        if(post.getLikes().contains(like)){
            return "redirect:/posts";
        }
        likeRepository.save(like);

        return "redirect:/posts";
    }
    
    
    @GetMapping("/posts")
    public String post(Model model) {
        Account account = currentAccount();
         Pageable postPageable = PageRequest.of(0, 25, Sort.by("postDate").descending());
         Pageable commentPageable = PageRequest.of(0, 25, Sort.by("commentDate").descending());
        
         model.addAttribute("posts", postRepository.findAll(postPageable));
         model.addAttribute("comments", commentRepository.findAll(commentPageable));
         model.addAttribute("username", account.getUsername());

        return "/posts";
    }

    @PostMapping("/posts")
    public String addPost(@RequestParam String message) {  // 
        Account account = currentAccount();

        Post post = new Post();
        post.setText(message);
        post.setAccount(account);
 
        postRepository.save(post);
        return "redirect:/posts";
    }

    @GetMapping("/users")
    public String allUsers(Model model) {
        Account account = currentAccount();
        List<Account> allAccounts = accountRepository.findAll();
        model.addAttribute("accounts", allAccounts);

        return "users";
    }

    @GetMapping("/users/{username}")
    public String user(Model model, @PathVariable String username) {
        Account profileAccount = accountRepository.findByUsername(username);
        model.addAttribute("username", profileAccount.getUsername());
        model.addAttribute("name", profileAccount.getName());
        model.addAttribute("picture", profileAccount.getId());
        model.addAttribute("title", profileAccount.getTitle());

        Pageable top_pageable = PageRequest.of(0, 3, Sort.by("likes").descending());
        Pageable pageableAll = PageRequest.of(0, 15, Sort.by("likes").descending());

        List<Skill> topSkills = skillRepository.findByAccount(profileAccount, top_pageable);
        List<Skill> allMySkills = skillRepository.findByAccount(profileAccount, pageableAll);

        allMySkills.removeIf(skill -> topSkills.contains(skill));

        model.addAttribute("topskills", topSkills);
        model.addAttribute("skills", allMySkills);
        return "user";
    }

    @GetMapping("/")
    public String home(Model model) {
        Account account = currentAccount();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("likes").descending());

        model.addAttribute("skills", account.getSkills());
        model.addAttribute("username", account.getUsername());
        model.addAttribute("name", account.getName());
        model.addAttribute("picture", account.getId());
        model.addAttribute("title", account.getTitle());
        model.addAttribute("requests", account.getReceivedInvites());
        model.addAttribute("connections", account.getConnections());

        return "index";
    }

    @PostMapping("/skill")
    public String addSkill(@RequestParam String name) {
        Account account = currentAccount();
        List<Skill> mySkills = account.getSkills();

        Skill existingSkill = skillRepository.findByAccountAndName(account, name);

        if (!(existingSkill == null)) {
            return "redirect:/";
        }

        Skill newSkill = new Skill(name, 1, account);
        skillRepository.save(newSkill);
        return "redirect:/";
    }

    @PostMapping("/likes")
    public String incrementLikes(@RequestParam Long id) {
        Account owner = currentAccount();

        Skill skillToLike = skillRepository.getOne(id);
        Account account = skillToLike.getAccount();

        if (account.equals(owner)) {
            return "redirect:/users/" + account.getUsername();
        }

        skillToLike.setLikes(skillToLike.getLikes() + 1);
        skillRepository.save(skillToLike);

        return "redirect:/users/" + account.getUsername();
    }

    @PostMapping("/search")
    public String search(@RequestParam String search) {
        Account myAccount = currentAccount();

        //Toteuta joku haku, jonka perusteella näytetään tulokset. Ohjaa vaikka uuteen resurssiin
        //Account otherAccount = accountRepository.findByUsername(username);
        //accountRepository.save(myAccount);
        //accountRepository.save(otherAccount);
        return "redirect:/";
    }

    @PostMapping("/accept")
    public String accept(@RequestParam String username) {
        Account myAccount = currentAccount();
        Account otherAccount = accountRepository.findByUsername(username);

        if (myAccount.getConnections().contains(otherAccount)) {
            return "redirect:/";
        }

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
        Account myAccount = currentAccount();
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
    public String removeUser(@RequestParam String username) {
        Account myAccount = currentAccount();
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
        Account myAccount = currentAccount();
        Account otherAccount = accountRepository.findByUsername(username);

        if (myAccount.getConnections().contains(otherAccount)) {
            return "redirect:/users";
        }

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
        Account account = currentAccount();
        account.setTitle(title);
        accountRepository.save(account);
        return "redirect:/";
    }

    // CHOOSE PICTURE
    @PostMapping("/picture")
    public String save(@RequestParam("file") MultipartFile file) throws IOException {
        Account account = currentAccount();
        account.setProfilePicture(file.getBytes());
        accountRepository.save(account);

        return "redirect:/";
    }

    @GetMapping(path = "/gifs/{id}/content", produces = "image/jpg")
    @ResponseBody
    public byte[] content(@PathVariable Long id) {
        return accountRepository.getOne(id).getProfilePicture();

    }

}
