package br.com.gerenciamento.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import jakarta.validation.ConstraintViolationException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoServiceTest {

    @Autowired
    private ServiceAluno serviceAluno;

    @Test
    public void getById() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Vinicius");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        this.serviceAluno.save(aluno);

        Aluno alunoRetorno = this.serviceAluno.getById(1L);
        Assert.assertTrue(alunoRetorno.getNome().equals("Vinicius"));
    }

    @Test
    public void salvarSemNome() {
        Aluno aluno = new Aluno();
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");

        Assert.assertThrows(
                ConstraintViolationException.class,
                () -> this.serviceAluno.save(aluno)
        );
    }

    @Test
    public void findByNomeContainingIgnoreCase() {
        Aluno aluno1 = new Aluno();
        aluno1.setNome("Antonio Victor");
        aluno1.setTurno(Turno.NOTURNO);
        aluno1.setCurso(Curso.ADMINISTRACAO);
        aluno1.setStatus(Status.ATIVO);
        aluno1.setMatricula("123456");
        serviceAluno.save(aluno1);

        Aluno aluno2 = new Aluno();
        aluno2.setNome("Maria Souza");
        aluno2.setTurno(Turno.NOTURNO);
        aluno2.setCurso(Curso.ADMINISTRACAO);
        aluno2.setStatus(Status.ATIVO);
        aluno2.setMatricula("654321");
        serviceAluno.save(aluno2);

        List<Aluno> resultado =
                serviceAluno.findByNomeContainingIgnoreCase("ANTONIO");

        Assert.assertFalse(resultado.isEmpty());

        Assert.assertTrue(
                resultado.stream()
                        .anyMatch(aluno ->
                                aluno.getNome().equals("Antonio Victor"))
        );
    }

    @Test
    public void findByStatusInativo() {
        Aluno aluno = new Aluno();
        aluno.setNome("Carlos Inativo");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.INATIVO);
        aluno.setMatricula("789123");

        serviceAluno.save(aluno);

        List<Aluno> resultado =
                serviceAluno.findByStatusInativo();

        Assert.assertFalse(resultado.isEmpty());

        Assert.assertTrue(
                resultado.stream()
                        .allMatch(a ->
                                a.getStatus() == Status.INATIVO)
        );
    }
}