package pl.sda.restdemo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

// Adnotacja oznacza że jest encja czyli klase którą będziemy mapowac
@Entity
public class User {

    // adnotacja oznacza że ta zmienna jest naszym Id
    @Id
    // adnotacja oznacza że automatycznie generuje nasze id i nie trzeba go określać
    @GeneratedValue
    private long id;
    private String lastname;
    private String firstname;

    public User() {
    }

    public User(String firstname, String lastname ) {
        this.lastname = lastname;
        this.firstname = firstname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public long getId() {
        return id;
    }
}
