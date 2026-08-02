package com.sparktech.shop.products;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    // Add / Edit
    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("name", "Mr. Java");
        model.addAttribute("product", new Product());
        return "form";
    }

    @PostMapping("/add")
    public String submit(@Valid @ModelAttribute Product product, BindingResult bindingResult) {
        log.info("Product {} has been submitted", product);

        if (bindingResult.hasErrors()) {
            return "form";
        }

        productService.saveProduct(product);
        log.info("Product {} has been saved", product);

        return "redirect:/product/add";
    }


    // List
    @GetMapping("list")
    public String list(Model model) {
        model.addAttribute("products", productService.getAll());
        return "list";
    }
}
