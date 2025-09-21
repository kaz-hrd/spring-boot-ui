package com.example.web.ui.controller;

import com.example.web.ui.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @GetMapping("/")
    public String home() {
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String userList(Model model) {
        List<User> users = createSampleUsers();
        model.addAttribute("users", users);
        return "users/list";
    }

    private List<User> createSampleUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "田中太郎", "tanaka@example.com", "開発部", "エンジニア"));
        users.add(new User(2L, "佐藤花子", "sato@example.com", "営業部", "営業"));
        users.add(new User(3L, "鈴木一郎", "suzuki@example.com", "人事部", "人事"));
        users.add(new User(4L, "高橋美香", "takahashi@example.com", "開発部", "シニアエンジニア"));
        users.add(new User(5L, "伊藤健", "ito@example.com", "営業部", "マネージャー"));
        users.add(new User(6L, "渡辺由美", "watanabe@example.com", "総務部", "総務"));
        users.add(new User(7L, "山田正男", "yamada@example.com", "開発部", "テックリード"));
        users.add(new User(8L, "中村沙織", "nakamura@example.com", "マーケティング部", "マーケター"));
        return users;
    }
}