package aula.microservicos.faculdade;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoDAO extends CrudRepository<AlunoBean, Integer> {

}
