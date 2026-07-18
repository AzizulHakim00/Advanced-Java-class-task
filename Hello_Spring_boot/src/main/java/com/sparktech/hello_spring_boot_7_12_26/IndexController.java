package com.sparktech.hello_spring_boot_7_12_26;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class IndexController {

    @GetMapping("/")
    public String indexPage()
    {
        return "index";
    }

    @GetMapping("/contact")
    public String contactPage()
    {
        return "contact";
    }

    @PostMapping("/submit-form")
    public String submitForm(@ModelAttribute Contact contact)
    {
        log.info("Contact Form Submitted {}", contact);
        return "redirect:/form";
    }
    @Controller
    public class StudentController {

        @GetMapping("/form")
        public String StudentForm() {
            return "studentForm";
        }

        @PostMapping("/submit")
        public String submitForm(@ModelAttribute StudentForm studentForm) {
            log.info("Student Form Submitted {}", studentForm);
            return "redirect:/contact";
        }
    }
}

