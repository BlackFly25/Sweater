package ru.blackfly.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.blackfly.models.Message;
import ru.blackfly.repos.MessageRepos;
import ru.blackfly.security.PersonDetails;

import java.util.List;

@Controller
public class MainController {

    private final MessageRepos messageRepos;

    @Autowired
    public MainController(MessageRepos messageRepos) {
        this.messageRepos = messageRepos;
    }


    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/showUserInfo")
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getUser());

        return "greeting";
    }

    @GetMapping("/main")
    public String main(Model model) {
        Iterable<Message> messages = messageRepos.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    @PostMapping("/main")
    public String addMessage(@RequestParam String text, @RequestParam String tag, Model model) {
        messageRepos.save(new Message(text, tag));
        Iterable<Message> messages = messageRepos.findAll();
        model.addAttribute("messages", messages);

        return "main";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String filter, Model model) {
        Iterable<Message> messages;
        if(filter != null && !filter.isEmpty()){
            messages= messageRepos.findByTag(filter);}
        else {
            messages= messageRepos.findAll();
        }
       model.addAttribute("messages", messages);
        return "main";
    }


}
