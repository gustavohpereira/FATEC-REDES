package projeto_redes.projeto.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projeto_redes.projeto.Repository.MensagemRepository;
import projeto_redes.projeto.entities.MensagemEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mensagem")
public class MensagemController {

    @Autowired
    private MensagemRepository mensagemRepository;

    // GET: listar todas as mensagens
    @GetMapping()
    public List<MensagemEntity> getAllMensagens() {
        return mensagemRepository.findAll();
    }

    // GET: obter uma mensagem por ID
    @GetMapping("/{id}")
    public ResponseEntity<MensagemEntity> getMensagemById(@PathVariable Long id) {
        Optional<MensagemEntity> mensagem = mensagemRepository.findById(id);
        if (mensagem.isPresent()) {
            return ResponseEntity.ok(mensagem.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: criar uma nova mensagem
    @PostMapping
    public MensagemEntity createMensagem(@RequestBody MensagemEntity mensagem) {
        return mensagemRepository.save(mensagem);
    }

    // PUT: atualizar uma mensagem existente
    @PutMapping("/{id}")
    public ResponseEntity<MensagemEntity> updateMensagem(@PathVariable Long id,
            @RequestBody MensagemEntity mensagemDetails) {
        Optional<MensagemEntity> mensagem = mensagemRepository.findById(id);
        if (mensagem.isPresent()) {
            MensagemEntity existingMensagem = mensagem.get();
            existingMensagem.setMensagem(mensagemDetails.getMensagem());
            existingMensagem.setData(mensagemDetails.getData());
            return ResponseEntity.ok(mensagemRepository.save(existingMensagem));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: deletar uma mensagem por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMensagem(@PathVariable Long id) {
        if (mensagemRepository.existsById(id)) {
            mensagemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
