package com.service.authservice.controller;

import com.service.authservice.model.UserCreationRequest;
import com.service.authservice.service.DAOUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
public class FormController {

    @Autowired
    DAOUserService daoUserService;

    @GetMapping("/signup")
    public String showAccountForm(Model model) {
        model.addAttribute("userCreationRequest", new UserCreationRequest());
        return "create-account";
    }

    @GetMapping("/signin")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/signup")
    public String saveUserAccount(@ModelAttribute("userCreationRequest") UserCreationRequest userCreationRequest) {
        daoUserService.createAccount(userCreationRequest);
        return "login";
    }
}