package aula.microservicos.faculdade;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/aluno")
public class Aluno {

    @Autowired
    private AlunoDAO dao;

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

    // /aluno
    @GetMapping("/todos")
    public ResponseEntity<Iterable<AlunoBean>> todosAlunos() throws AlunoInexistenteException {

        if (dao.count() > 0) {
            return new ResponseEntity<Iterable<AlunoBean>>(dao.findAll(), HttpStatus.OK);
        } else
            throw new AlunoInexistenteException();
    }

    // localhost:8080/aluno/100
    @GetMapping("/{id}")
    public ResponseEntity<AlunoBean> getAlunoByPath(@PathVariable Integer id) throws AlunoInexistenteException {
        logger.debug("Obtendo o aluno: " + id);

        if (dao.existsById(id)) {

            AlunoBean ret = dao.findById(id).get();
            return new ResponseEntity<AlunoBean>(ret, HttpStatus.OK);

        } else {
            throw new AlunoInexistenteException();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable Integer id) throws AlunoInexistenteException {
        logger.debug("Removendo o aluno: " + id);

        if (dao.existsById(id)) {

            dao.deleteById(id);
            return new ResponseEntity<String>("Aluno removido", HttpStatus.OK);

        } else {
            throw new AlunoInexistenteException();
        }

    }

    // {"id": 100, "turma": "teste"}
    @PostMapping
    public ResponseEntity<Integer> cadastrar(@Valid @RequestBody AlunoBean aluno) throws IdDuplicadoException {
        logger.debug("Cadastrando o aluno: " + aluno.getNome());

        // if (aluno.getId() == 100) throw new IdDuplicadoException();

        dao.save(aluno);

        return new ResponseEntity<Integer>(aluno.getId(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Integer id, @RequestBody AlunoBean aluno)
            throws AlunoInexistenteException {
        logger.debug("Atualizando o aluno: " + aluno.getNome());

        if (dao.existsById(id)) {

            AlunoBean bcoDados = dao.findById(id).get();
            bcoDados.setCurso(aluno.getCurso());
            bcoDados.setTurma(aluno.getTurma());
            bcoDados.setNome(aluno.getNome());
            dao.save(bcoDados);
            return new ResponseEntity<String>("Aluno atualizado", HttpStatus.OK);

        } else {
            throw new AlunoInexistenteException();
        }

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

}
