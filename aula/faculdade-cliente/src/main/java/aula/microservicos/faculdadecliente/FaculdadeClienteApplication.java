package aula.microservicos.faculdadecliente;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class FaculdadeClienteApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FaculdadeClienteApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		RestClient client = RestClient.create();
		ResponseEntity<AlunoBean> ret = client.get().uri("http://localhost:8080/aluno/1").retrieve()
				.toEntity(AlunoBean.class);

		System.out.println(ret.getStatusCode());
		AlunoBean aluno = new AlunoBean();
		aluno = ret.getBody();

		System.out.println(aluno.getNome());

		// Criar um novo aluno (POST)

		AlunoBean novoAluno = new AlunoBean();
		novoAluno.setNome("Ana Paula da Silva");
		novoAluno.setCurso("Direito");
		novoAluno.setTurma("D10");

		client.post().uri("http://localhost:8080/aluno").body(novoAluno).retrieve();
	}

}
