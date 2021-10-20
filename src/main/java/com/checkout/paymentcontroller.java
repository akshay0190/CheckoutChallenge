package com.checkout;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
public class paymentcontroller {

        @GetMapping("/")
        public String index(Model model) {
            return "cart";
        }

        @GetMapping("/checkout")
        public String checkout(Model model) {
            return "checkout";
        }

        @GetMapping("/completed/{type}")
        public String result(@PathVariable String type, Model model) {
            model.addAttribute("type", type);
            return "ordercompletion";
        }

}
