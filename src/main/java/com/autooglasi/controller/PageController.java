package com.autooglasi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** Pomocne staticne strane. */
@Controller
public class PageController {

    // RequestMapping (sve metode) - Spring Security forward-uje POST zahtev na ovu
    // stranu pri 403, pa mora da prihvati i GET i POST.
    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
}
