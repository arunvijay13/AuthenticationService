package com.service.authservice.controller;

import com.service.authservice.model.FlamesRequest;
import com.service.authservice.service.FlamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/games")
public class FlamesController {

    @Autowired
    FlamesService flamesService;

    @GetMapping("/form")
    public String showFlamesForm(Model model) {
        model.addAttribute("flamesRequest", new FlamesRequest());
        return "flames";
    }

    @GetMapping("/calculate")
    public String calculateLove(Principal principal, Model model) {
        FlamesRequest flamesRequest = (FlamesRequest) model.getAttribute("flamesRequest");
        model.addAttribute("username", principal.getName());
        model.addAttribute("partner name",
                flamesRequest.getName1()+"|"+flamesRequest.getName2());
        model.addAttribute("result", "love");
        return "result";
    }
}
