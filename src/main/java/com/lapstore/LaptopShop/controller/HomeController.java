package com.lapstore.LaptopShop.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import com.lapstore.LaptopShop.model.Category;
import com.lapstore.LaptopShop.model.Product;
import com.lapstore.LaptopShop.model.UserDtls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lapstore.LaptopShop.service.CartService;
// import com.lapstore.LaptopShop.repository.ProductRepository;
import com.lapstore.LaptopShop.service.CategoryService;
import com.lapstore.LaptopShop.service.ProductService;
import com.lapstore.LaptopShop.service.UserService;
import com.lapstore.LaptopShop.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            m.addAttribute("user", userDtls);
            Integer countCart = cartService.getCountCart(userDtls.getId());
            m.addAttribute("countCart", countCart);
        }

        List<Category> allActiveCategory = categoryService.getAllActiveCategories();
        m.addAttribute("categories", allActiveCategory);

    }

    @GetMapping("/")
    public String index(Model m) {
        List<Product> products = productService.getAllActiveProducts();
        m.addAttribute("products", products);
        return "home";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/products")
    public String products(Model m, @RequestParam(value = "category", defaultValue = "") String category,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        List<Category> categories = categoryService.getAllActiveCategories();
        m.addAttribute("categories", categories);
        m.addAttribute("paramValue", category);

        // List<Product> products = productService.getAllActiveProducts(category);
        // m.addAttribute("products", products);

        Page<Product> page = productService.getAllActiveProductPagination(pageNo, pageSize, category);
        List<Product> products = page.getContent();
        m.addAttribute("products", products);
        m.addAttribute("productsSize", products.size());
        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());

        return "products";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable int id, Model m) {

        Product productById = productService.getProductById(id);
        m.addAttribute("product", productById);
        return "view_product";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session)
            throws IOException {

        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);
        UserDtls saveUser = userService.saveUser(user);

        if (!ObjectUtils.isEmpty(saveUser)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
                        + file.getOriginalFilename());

                System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg", "Register successfully");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/register";
    }

    // forgot password

    @GetMapping("/forgot-password")
    public String showForgotPassword() {

        return "forgot_password.html";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {
        UserDtls userByEmail = userService.getUserByEmail(email);

        if (ObjectUtils.isEmpty(userByEmail)) {
            session.setAttribute("errorMsg", "Invalid email");
        } else {

            String resetToken = UUID.randomUUID().toString();
            userService.updateUserResetToken(email, resetToken);

            // generate URL : http://localhost:8080/reset-password?token=sdfasfsgasg

            String url = CommonUtil.generateUrl(request) + "/reset-password?token=" + resetToken;

            Boolean sendMail = commonUtil.sendMail(url, email);

            if (sendMail) {
                session.setAttribute("succMsg", "Please check your email.. Password reset link sent");
            } else {
                session.setAttribute("errorMsg", "Something wrong on server! Mail not sent");
            }
        }

        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPassword(@RequestParam String token, HttpSession session, Model m) {

        UserDtls userByToken = userService.getUserByToken(token);

        if (userByToken == null) {
            m.addAttribute("msg", "Your link is invalid or expired !!");
            return "message";
        }
        m.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password, HttpSession session,
            Model m) {

        UserDtls userByToken = userService.getUserByToken(token);

        if (userByToken == null) {
            m.addAttribute("errorMsg", "Your link is invalid or expired !!");
            return "message";
        } else {
            userByToken.setPassword(bCryptPasswordEncoder.encode(password));
            userByToken.setResetToken(null);
            userService.updateUser(userByToken);
            session.setAttribute("succMsg", "Password change successfully");
            m.addAttribute("msg", "Password change successfully");
            return "message";
        }
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam String ch, Model m) {
        List<Product> searchProdcts = productService.searchProduct(ch);
        m.addAttribute("products", searchProdcts);
        return "products";
    }
}
