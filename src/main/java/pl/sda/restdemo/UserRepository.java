package pl.sda.restdemo;

import org.springframework.data.repository.CrudRepository;

// interfejs który rozszerza klase CrudRepository podajemy w nawiasach User czyli nasz obiekt i Long czyli typ naszego Id
// dzieki tej klasie możemy używać takich metod jak  findAll() , findById(), save() , deleteById() - patrz UserController
public interface UserRepository extends CrudRepository<User, Long> {
}
