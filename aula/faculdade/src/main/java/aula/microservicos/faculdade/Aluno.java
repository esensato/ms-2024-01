package aula.microservicos.faculdade;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/aluno")
public class Aluno {

    @Value("${nome.aluno}")
    String nomeAlunoProperty;

    // instancia log para a classe Aluno
    private static final Logger logger = LoggerFactory.getLogger(Aluno.class);

    // consultar um aluno
    // localhost:8080/aluno?id=100
    @GetMapping
    public ResponseEntity<AlunoBean> getAluno(
            @RequestParam(name = "id", required = false, defaultValue = "0") Integer id) {
        logger.debug("Obtendo o aluno: " + id);
        if (id > 100) {
            return new ResponseEntity<AlunoBean>(new AlunoBean(), HttpStatus.OK);
        } else {
            return new ResponseEntity<AlunoBean>(new AlunoBean(), HttpStatus.NOT_FOUND);
        }
    }

    // localhost:8080/aluno/100
    @GetMapping("/{id}")
    public ResponseEntity<AlunoBean> getAlunoByPath(@PathVariable Integer id) {
        logger.debug("Obtendo o aluno: " + id);
        AlunoBean a = new AlunoBean();
        a.setId(id);
        a.setNome("Joao Pereira");
        a.setTurma("32C");
        if (id > 100) {
            return new ResponseEntity<AlunoBean>(a, HttpStatus.OK);
        } else {
            return new ResponseEntity<AlunoBean>(new AlunoBean(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Integer> cadastrar(@RequestBody AlunoBean aluno) {
        logger.debug("Cadastrando o aluno: " + aluno.getNome());
        return new ResponseEntity<Integer>(aluno.getId(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> atualizar(@PathVariable Integer id, @RequestBody AlunoBean aluno) {
        logger.debug("Atualizando o aluno: " + aluno.getNome());
        return new ResponseEntity<Integer>(id, HttpStatus.OK);
    }

}
