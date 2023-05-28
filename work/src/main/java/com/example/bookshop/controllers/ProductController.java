package com.example.bookshop.controllers;

import com.example.bookshop.repositories.ProductRepository;
import com.example.bookshop.services.CategoryService;
import com.example.bookshop.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;
    private  final ProductService productService;

    private final CategoryService categoryService;

    public ProductController(ProductRepository productRepository, ProductService productService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String getAllProduct(Model model){
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("categorys", categoryService.getAllCategory());
        return "/product/product";
    }

    @GetMapping("/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("product", productService.getProductId(id));
        return "/product/infoProduct";
    }

    @PostMapping("/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("ot") String ot, @RequestParam("do") String Do, @RequestParam(value = "price", required = false, defaultValue = "") String price, @RequestParam(value = "category", required = false, defaultValue = "") String category, Model model){
        if(!ot.isEmpty() & !Do.isEmpty()){
            if(!price.isEmpty()){
                if(price.equals("sorted_by_ascending_price")) {
                    if (!category.isEmpty()) {
                        model.addAttribute("products", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), Integer.parseInt(category)));
                    } else {
                        model.addAttribute("products", productRepository.findByTitleOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));
                    }
                } else if(price.equals("sorted_by_descending_price")){
                    if(!category.isEmpty()){
                        model.addAttribute("products", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), Integer.parseInt(category)));
                    }  else {
                        model.addAttribute("products", productRepository.findByTitleOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));
                    }
                }
            } else {
                if (!category.isEmpty()) {
                    model.addAttribute("products", productRepository.findByTitleAndCategoryAndPriceGreaterThanEqualAndPriceLessThanEqual(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), Integer.parseInt(category)));
                }else {
                    model.addAttribute("products", productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));
                }
            }
        } else {
            if (!category.isEmpty()) {
                model.addAttribute("products", productRepository.findByTitleAndCategory(search.toLowerCase(), Integer.parseInt(category)));
            }else if (!search.isEmpty()){
                model.addAttribute("products", productRepository.findByTitleContainingIgnoreCase(search));
            }
        }

        if (model.containsAttribute("products")){
            model.addAttribute("filter", true);
        }else {
            model.addAttribute("products", productService.getAllProduct());
            model.addAttribute("filter", false);
        }

        model.addAttribute("value_search", search);
        model.addAttribute("value_price_ot", ot);
        model.addAttribute("value_price_do", Do);
        model.addAttribute("value_category", category);
        model.addAttribute("categories", categoryService.getAllCategory());

        return "/product/product";

    }
}
