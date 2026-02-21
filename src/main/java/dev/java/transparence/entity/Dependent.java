package dev.java.transparence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "dependent", uniqueConstraints = {
        @UniqueConstraint(name = "uk_dependent_cpf", columnNames = "cpf")
})
public class Dependent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "phone", nullable = false, length = 11)
    private String phone;
    @Column(name = "adress", nullable = false)
    private String adress;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "state", nullable = false, length = 2)
    private String state;
    @Column(name = "zipCode", nullable = false, length = 8)
    private String zipCode;

    public Dependent() {
    }

    public Dependent(String cpf, String name, String phone, String adress, String city,
            String state, String zipCode) {
        this.cpf = cpf;
        this.name = name;
        this.phone = phone;
        this.adress = adress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Long getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getphone() {
        return phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }

    public String getadress() {
        return adress;
    }

    public void setadress(String adress) {
        this.adress = adress;
    }

    public String getcity() {
        return city;
    }

    public void setcity(String city) {
        this.city = city;
    }

    public String getstate() {
        return state;
    }

    public void setstate(String state) {
        this.state = state;
    }

    public String getzipCode() {
        return zipCode;
    }

    public void setzipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Dependent other = (Dependent) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "dependent [id=" + id + ", name=" + name + "]";
    }

}
