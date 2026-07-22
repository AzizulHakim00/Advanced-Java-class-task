package bd.edu.seu.product_shop;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
@RequestMapping("/product")
public class ProductController {

    private final List<Product> products = new ArrayList<>();

    @GetMapping("/add")
    public String showProductForm(Model model) {

        model.addAttribute("name", "Add Product");
        model.addAttribute("product", new Product());
        model.addAttribute("editMode", false);

        return "form";
    }

    @PostMapping("/add")
    public String addProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("name", "Add Product");
            model.addAttribute("editMode", false);

            return "form";
        }

        products.add(product);

        log.info("Product added: {}", product);

        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public String showListProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            Model model) {

        List<Product> filteredProducts = products.stream()
                .filter(product -> {

                    boolean searchMatch = keyword == null
                            || keyword.isBlank()
                            || product.getId().toString()
                            .contains(keyword)
                            || product.getName().toLowerCase()
                            .contains(keyword.toLowerCase());

                    boolean categoryMatch = category == null
                            || category.isBlank()
                            || product.getCategory()
                            .equalsIgnoreCase(category);

                    return searchMatch && categoryMatch;
                })
                .toList();

        model.addAttribute("products", filteredProducts);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);

        return "list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Integer id,
            Model model) {

        Product existingProduct = products.stream()
                .filter(product ->
                        Objects.equals(product.getId(), id))
                .findFirst()
                .orElse(null);

        if (existingProduct == null) {
            return "redirect:/product/list";
        }

        model.addAttribute("name", "Edit Product");
        model.addAttribute("product", existingProduct);
        model.addAttribute("editMode", true);
        model.addAttribute("originalId", id);

        return "form";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(
            @PathVariable Integer id,
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("name", "Edit Product");
            model.addAttribute("editMode", true);
            model.addAttribute("originalId", id);

            return "form";
        }

        for (int i = 0; i < products.size(); i++) {

            if (Objects.equals(products.get(i).getId(), id)) {

                products.set(i, product);

                log.info("Product updated: {}", product);

                break;
            }
        }

        return "redirect:/product/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {

        products.removeIf(product ->
                Objects.equals(product.getId(), id));

        log.info("Product deleted. ID: {}", id);

        return "redirect:/product/list";
    }
}