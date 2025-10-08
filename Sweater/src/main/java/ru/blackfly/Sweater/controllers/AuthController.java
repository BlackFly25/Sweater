package ru.blackfly.Sweater.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.blackfly.Sweater.models.User;
import ru.blackfly.Sweater.services.RegistrationService;
import ru.blackfly.Sweater.util.PersonValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidator personValidator;
    private final RegistrationService registrationService;

    public AuthController(PersonValidator personValidator, RegistrationService registrationService) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        personValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }
        registrationService.registerUser(user);
        return "redirect:/auth/login";
    }
}
