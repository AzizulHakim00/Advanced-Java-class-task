package bd.edu.seu.product_shop;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/add")
    public String showProductForm(Model model) {
        model.addAttribute("name", "Hi, hello");
        model.addAttribute("product", new Product());
        return "form";
    }

    @PostMapping("/add")
    public String submit(
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "form";
        }

        log.info("Product added: {}", product);
        System.out.println("Product added: " + product);
        return "redirect:/product/add";
    }
}