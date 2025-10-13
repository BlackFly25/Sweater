package ru.blackfly.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.blackfly.models.User;
import ru.blackfly.repos.MessageRepos;
import ru.blackfly.repos.UserRepo;
import ru.blackfly.services.PersonDetailsService;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class UserController {

    private final PersonDetailsService personDetailsService;
    private final UserRepo userRepo;
    private final MessageRepos messageRepo;


    public UserController(PersonDetailsService personDetailsService, UserRepo userRepo, MessageRepos messageRepo) {
        this.personDetailsService = personDetailsService;
        this.userRepo = userRepo;
        this.messageRepo = messageRepo;
    }

    /**
     * Ниже админская страничка, с ограниченным доступом
     */
    @GetMapping()
    public String admin() {
        return "admin";
    }

    @GetMapping("/")
    public String allUsers(@RequestParam(required = false) String name, Model model) {
        if (name != null && !name.isEmpty()) {
            model.addAttribute("users", userRepo.findByUsernameContainingIgnoreCase(name));
        } else {
            model.addAttribute("users", userRepo.findAll());
        }

        model.addAttribute("filter", name); // чтобы в форме отображалось текущее значение фильтра

        return "admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            model.addAttribute("user", optionalUser.get()); // <- берем сам объект
            return "edit";
        } else {
            // обработка если пользователь не найден
            return "redirect:/error";
        }
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit";}
        personDetailsService.update(user.getId(), user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {

        personDetailsService.delete(id);
        return "redirect:/admin";
    }

    /**
     * Немного расширим админку, и выведем все сообщения от пользователя.
     *
     */

    @GetMapping("/messages/{id}")
    public String userMessages(@PathVariable("id") Long id, Model model) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
            model.addAttribute("messages", messageRepo.findByAuthor(user)); // <- используем author
            return "messages";
        } else {
            return "redirect:/error";
        }
    }






}
