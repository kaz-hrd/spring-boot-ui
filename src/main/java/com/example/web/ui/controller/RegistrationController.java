package com.example.web.ui.controller;

import com.example.web.ui.model.User;
import com.example.web.ui.model.UserRegistrationForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    
    // サンプル用のユーザーリスト（実際の実装ではデータベースを使用）
    private static List<User> users = new ArrayList<>();
    private static Long nextId = 1L;
    
    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("userForm", new UserRegistrationForm());
        model.addAttribute("departments", getDepartmentOptions());
        model.addAttribute("roles", getRoleOptions());
        return "register/form";
    }
    
    @PostMapping
    public String processRegistration(
            @Valid @ModelAttribute("userForm") UserRegistrationForm userForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        // パスワード確認のカスタムバリデーション
        if (!userForm.isPasswordMatching()) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "パスワードが一致しません");
        }
        
        // メールアドレスの重複チェック（サンプル実装）
        if (isEmailExists(userForm.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "このメールアドレスは既に登録されています");
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", getDepartmentOptions());
            model.addAttribute("roles", getRoleOptions());
            return "register/form";
        }
        
        // ユーザー登録処理（サンプル実装）
        User newUser = new User();
        newUser.setId(nextId++);
        newUser.setName(userForm.getName());
        newUser.setEmail(userForm.getEmail());
        newUser.setDepartment(userForm.getDepartment());
        newUser.setRole(userForm.getRole());
        
        users.add(newUser);
        
        redirectAttributes.addFlashAttribute("successMessage", "ユーザー登録が完了しました");
        return "redirect:/register/success";
    }
    
    @GetMapping("/success")
    public String showSuccessPage() {
        return "register/success";
    }
    
    private List<String> getDepartmentOptions() {
        List<String> departments = new ArrayList<>();
        departments.add("営業部");
        departments.add("開発部");
        departments.add("人事部");
        departments.add("経理部");
        departments.add("総務部");
        departments.add("マーケティング部");
        return departments;
    }
    
    private List<String> getRoleOptions() {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        roles.add("MANAGER");
        roles.add("ADMIN");
        return roles;
    }
    
    private boolean isEmailExists(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }
}