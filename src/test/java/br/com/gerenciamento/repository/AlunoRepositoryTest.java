package br.com.gerenciamento.repository;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    @Before
    public void limparBanco() {
        alunoRepository.deleteAll();
    }

    @Test
    public void salvarAlunoEConsultarPorId() {
        Aluno aluno = criarAluno(
                "Antonio Victor",
                "MAT001",
                Status.ATIVO
        );

        Aluno alunoSalvo = alunoRepository.save(aluno);

        Optional<Aluno> resultado =
                alunoRepository.findById(alunoSalvo.getId());

        Assert.assertTrue(resultado.isPresent());
        Assert.assertEquals(
                "Antonio Victor",
                resultado.get().getNome()
        );
        Assert.assertEquals(
                "MAT001",
                resultado.get().getMatricula()
        );
    }

    @Test
    public void findByStatusAtivoDeveRetornarSomenteAlunosAtivos() {
        Aluno alunoAtivo = criarAluno(
                "Aluno Ativo",
                "MAT002",
                Status.ATIVO
        );

        Aluno alunoInativo = criarAluno(
                "Aluno Inativo",
                "MAT003",
                Status.INATIVO
        );

        alunoRepository.save(alunoAtivo);
        alunoRepository.save(alunoInativo);

        List<Aluno> resultado =
                alunoRepository.findByStatusAtivo();

        Assert.assertEquals(1, resultado.size());
        Assert.assertEquals(
                Status.ATIVO,
                resultado.get(0).getStatus()
        );
        Assert.assertEquals(
                "Aluno Ativo",
                resultado.get(0).getNome()
        );
    }

    @Test
    public void findByStatusInativoDeveRetornarSomenteAlunoInativo() {
        Aluno alunoAtivo = criarAluno(
                "Maria Ativa",
                "MAT004",
                Status.ATIVO
        );

        Aluno alunoInativo = criarAluno(
                "Carlos Inativo",
                "MAT005",
                Status.INATIVO
        );

        alunoRepository.save(alunoAtivo);
        alunoRepository.save(alunoInativo);

        List<Aluno> resultado =
                alunoRepository.findByStatusInativo();

        Assert.assertEquals(1, resultado.size());
        Assert.assertEquals(
                Status.INATIVO,
                resultado.get(0).getStatus()
        );
        Assert.assertEquals(
                "Carlos Inativo",
                resultado.get(0).getNome()
        );
    }

    @Test
    public void findByNomeContainingIgnoreCaseDeveIgnorarMaiusculasEMinusculas() {
        Aluno primeiroAluno = criarAluno(
                "Joao Silva",
                "MAT006",
                Status.ATIVO
        );

        Aluno segundoAluno = criarAluno(
                "Mariana Souza",
                "MAT007",
                Status.ATIVO
        );

        alunoRepository.save(primeiroAluno);
        alunoRepository.save(segundoAluno);

        List<Aluno> resultado =
                alunoRepository.findByNomeContainingIgnoreCase("JOAO");

        Assert.assertEquals(1, resultado.size());
        Assert.assertEquals(
                "Joao Silva",
                resultado.get(0).getNome()
        );
    }

    private Aluno criarAluno(
            String nome,
            String matricula,
            Status status
    ) {
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setMatricula(matricula);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setTurno(Turno.NOTURNO);
        aluno.setStatus(status);
        return aluno;
    }
}