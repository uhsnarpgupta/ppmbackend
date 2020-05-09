package io.agileintelligence.ppmt.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "User")
public class UserBO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "username is required")
    @Size(max = 5, message = "Username can be at most 5 characters")
    @Column(unique = true)
    private String username;

    @Email(message = "Username needs to be an email")
    private String email;

    @NotBlank(message = "Please enter your full name")
    private String fullName;

    @NotBlank(message = "Password field is required")
    private String password;

    @Transient
    private String confirmPassword;

    private Date create_At;
    private Date update_At;

    //OneToMany with ProjectBO
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "userBO", orphanRemoval = true)
    private List<ProjectBO> projectBOS = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private ImageBO image;

    private String mobile;

    public UserBO() {
    }

    public UserBO(String fullName, String username, String email, String password) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Date getCreate_At() {
        return create_At;
    }

    public Date getUpdate_At() {
        return update_At;
    }

    public List<ProjectBO> getProjectBOS() {
        return projectBOS;
    }

    public void setProjectBOS(List<ProjectBO> projectBOS) {
        this.projectBOS = projectBOS;
    }

    public ImageBO getImage() {
        return image;
    }

    public void setImage(ImageBO image) {
        this.image = image;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @PrePersist
    protected void onCreate() {
        this.create_At = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.update_At = new Date();
    }

    @Override
    public String toString() {
        return "UserBO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", create_At=" + create_At +
                ", update_At=" + update_At +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}