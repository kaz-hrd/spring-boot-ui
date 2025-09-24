package com.example.web.ui.model;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UserRegistrationForm {
    
    @NotBlank(message = "名前は必須項目です")
    @Size(max = 100, message = "名前は100文字以内で入力してください")
    private String name;
    
    @NotBlank(message = "メールアドレスは必須項目です")
    @Email(message = "有効なメールアドレスを入力してください")
    private String email;
    
    @NotBlank(message = "パスワードは必須項目です")
    @Size(min = 8, max = 100, message = "パスワードは8文字以上100文字以内で入力してください")
    private String password;
    
    @NotBlank(message = "パスワード確認は必須項目です")
    private String confirmPassword;
    
    @Size(max = 100, message = "部署名は100文字以内で入力してください")
    private String department;
    
    private String role = "USER"; // デフォルト値
    
    @Size(max = 15, message = "電話番号は15文字以内で入力してください")
    private String phone;
    
    @Size(max = 500, message = "備考は500文字以内で入力してください")
    private String notes;
    
    // パスワードと確認パスワードが一致するかチェック
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}