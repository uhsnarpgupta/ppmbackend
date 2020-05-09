package io.agileintelligence.ppmt.domain;

import javax.persistence.*;

@Entity(name = "Image")
public class ImageBO {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserBO userBO;

    private String type;

    @Lob
    private Byte[] image;

    public ImageBO(UserBO userBO, String type, Byte[] image) {
        this.userBO = userBO;
        this.type = type;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserBO getUserBO() {
        return userBO;
    }

    public void setUserBO(UserBO userBO) {
        this.userBO = userBO;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }
}