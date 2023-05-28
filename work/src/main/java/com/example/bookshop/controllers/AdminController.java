package com.example.bookshop.controllers;

import com.example.bookshop.enumm.Status;
import com.example.bookshop.models.*;
import com.example.bookshop.services.CategoryService;
import com.example.bookshop.services.OrderService;
import com.example.bookshop.services.PersonService;
import com.example.bookshop.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${upload.path}")
    private String uploadPath;

    private final ProductService productService;

    private final OrderService orderService;

    private final PersonService personService;

    private final CategoryService categoryService;


    public AdminController(ProductService productService, OrderService orderService, PersonService personService, CategoryService categoryService) {
        this.productService = productService;
        this.orderService = orderService;
        this.personService = personService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String admin(Model model){
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("orders", orderService.getAllOrder());
        model.addAttribute("persons", personService.getAllPerson());
        return "admin/index";
    }

    // PRODUCT
    //
    @GetMapping("/product/add")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        model.addAttribute("category", categoryService.getAllCategory());
        return "product/addProduct";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult,
                             @RequestParam("file_one")MultipartFile file_one,
                             @RequestParam("file_two")MultipartFile file_two,
                             @RequestParam("file_three")MultipartFile file_three,
                             @RequestParam("file_four")MultipartFile file_four,
                             @RequestParam("file_five")MultipartFile file_five,
                             @RequestParam("category") int category, Model model) throws IOException {
        Category category_db = (Category) categoryService.getCategoryId(category);
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryService.getAllCategory());
            return "product/addProduct";
        }

        if(file_one != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);

        }

        if(file_two != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_three != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_four != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_five != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five .getOriginalFilename();
            file_five.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        productService.saveProduct(product, category_db);
        return "redirect:/admin";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, Model model){
        try {
            //Удаляем файлы изображений товара
            Product product = productService.getProductId(id);
            List<Image> imageList = product.getImageList();
            for (Image image : imageList) {
                File file = new File(uploadPath + "/" + image.getFileName());
                if (!file.exists()) {
                    file.delete();
                }
            }
            productService.deleteProduct(id);
        } catch (Exception e) {

        }

        return "redirect:/admin";
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id){
        model.addAttribute("product", productService.getProductId(id));
        model.addAttribute("category", categoryService.getAllCategory());
        return "product/editProduct";
    }

    @PostMapping("/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult,
                              @PathVariable("id") int id,
                              Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryService.getAllCategory());
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }

    //статус заказа

    @GetMapping("/order/edit/{id}")
    public String editOrder(Model model, @PathVariable("id") String id){
        model.addAttribute("order", orderService.getOrderByNumber(id));
        return "order/editOrder";
    }

    @PostMapping("/order/edit/{id}")
    public String editOrder(@PathVariable("id") String id, @RequestParam("status") Status status){
        List<Order> order = orderService.getOrderByNumber(id);
        for(Order item : order){
            orderService.saveOrder(item, status);
        }
        return "redirect:/admin";
    }

    @GetMapping("/order/search")
    public String searchOrder(@RequestParam(value = "search", required = false, defaultValue = "") String search, Model model){
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("persons", personService.getAllPerson());
        if(!search.isEmpty()) {
            model.addAttribute("orders", orderService.findByNumberEndingWithIgnoreCaseOrderByDateTimeDesc(search));
        }else{
            model.addAttribute("orders", orderService.getAllOrder());
        }

        model.addAttribute("value_search", search);

        return "admin/index";
    }

    //смена роли

    @GetMapping("/user/edit/{id}")
    public String editUser(Model model, @PathVariable("id") int id){
        model.addAttribute("person", personService.getPersonId(id));
        return "person/editPerson";
    }

    @PostMapping("/user/edit/{id}")
    public String editUser(@PathVariable("id") int id, @RequestParam("role") String role){
        Person person = personService.getPersonId(id);
        personService.setRole(person, role);
        return "redirect:/admin";
    }
}
