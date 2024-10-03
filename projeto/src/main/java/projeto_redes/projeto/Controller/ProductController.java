package projeto_redes.projeto.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projeto_redes.projeto.MensagemRepository;
import projeto_redes.projeto.entities.MensagemEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mensagem")
public class ProductController {

    @Autowired
    private MensagemRepository mensagemRepository;

    // GET: listar todos os produtos
    @GetMapping
    public List<MensagemEntity> getAllProducts() {
        return mensagemRepository.findAll();
    }

    // GET: obter um produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<MensagemEntity> getProductById(@PathVariable Long id) {
        Optional<MensagemEntity> product = mensagemRepository.findById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: criar um novo produto
    @PostMapping
    public MensagemEntity createProduct(@RequestBody MensagemEntity mensagem) {
        return mensagemRepository.save(mensagem);
    }

    // PUT: atualizar um produto existente
    @PutMapping("/{id}")
    public ResponseEntity<MensagemEntity> updateProduct(@PathVariable Long id, @RequestBody MensagemEntity mensagemDetails) {
        Optional<MensagemEntity> mensagem = mensagemRepository.findById(id);
        if (mensagem.isPresent()) {
            MensagemEntity existingMensagem = mensagem.get();
            existingMensagem.setMensagem(mensagemDetails.getMensagem());  // Chamada no objeto de instância
            existingMensagem.setData(mensagemDetails.getData());  // Chamada no objeto de instância
            return ResponseEntity.ok(mensagemRepository.save(existingMensagem));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    // DELETE: deletar um produto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (mensagemRepository.existsById(id)) {
            mensagemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
