package bd.edu.seu.product_shop;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProuductService prouductService;

    /// dip ///
//    public ProductController(ProuductService prouductService) {
//        this.prouductService = prouductService;
//    }

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

        prouductService.saveProduct(product);

        log.info("Product added: {}", product);

        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public String showListProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            Model model) {

        List<Product> products = prouductService.getAll();

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

        Product existingProduct = prouductService.getById(id);

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

        Product existingProduct = prouductService.getById(id);

        if (existingProduct == null) {
            return "redirect:/product/list";
        }

        product.setId(id);

        prouductService.saveProduct(product);

        log.info("Product updated: {}", product);

        return "redirect:/product/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {

        Product existingProduct = prouductService.getById(id);

        if (existingProduct != null) {
            prouductService.deleteById(id);

            log.info("Product deleted. ID: {}", id);
        }

        return "redirect:/product/list";
    }
}