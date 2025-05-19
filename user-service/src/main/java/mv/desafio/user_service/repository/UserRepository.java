package mv.desafio.user_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mv.desafio.user_service.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNome(String nome);
    List<User> findByNomeContaining(String parteNome);
    boolean existsByEmail(String email);
    Page<User> findAll(Pageable pageable);
    Optional<User> findById(Long userId);
    
}
