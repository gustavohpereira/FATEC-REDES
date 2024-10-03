package projeto_redes.projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import projeto_redes.projeto.entities.MensagemEntity;

public interface MensagemRepository extends JpaRepository<MensagemEntity, Long> {
}