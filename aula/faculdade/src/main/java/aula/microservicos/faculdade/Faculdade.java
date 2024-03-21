package aula.microservicos.faculdade;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/faculdade")
public class Faculdade {

    @DeleteMapping("/{idDisciplina}")
    public ResponseEntity<String> removerDisciplina(@PathVariable int idDisciplina) {

        RestClient restClient = RestClient.create();

        ResponseEntity<String> result = restClient.get()
                .uri("http://localhost:8080/api/matricula/search/buscarTodasMatriculasPorDisciplina?idDisciplina={idDisciplina}",
                        idDisciplina)
                .retrieve()
                .toEntity(String.class);
        System.out.println("Status: " + result.getStatusCode());
        System.out.println("Headers: " + result.getHeaders());
        System.out.println("Conteudo: " + result.getBody());

        return new ResponseEntity<String>(result.getBody(), HttpStatus.OK);

    }

}
