package br.com.gerenciamento.controller;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.service.ServiceAluno;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceAluno serviceAluno;

    @Test
    public void abrirFormularioDeAlunoDeveRetornarPaginaCorreta()
            throws Exception {

        mockMvc.perform(get("/inserirAlunos"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/formAluno"))
                .andExpect(model().attributeExists("aluno"))
                .andExpect(model().attribute(
                        "aluno",
                        instanceOf(Aluno.class)
                ));
    }

    @Test
    public void listarAlunosDeveRetornarTodosOsAlunos()
            throws Exception {

        List<Aluno> alunos = Arrays.asList(
                criarAluno("Antonio Victor", "MAT001"),
                criarAluno("Maria Souza", "MAT002")
        );

        when(serviceAluno.findAll()).thenReturn(alunos);

        mockMvc.perform(get("/alunos-adicionados"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/listAlunos"))
                .andExpect(model().attributeExists("alunosList"))
                .andExpect(model().attribute(
                        "alunosList",
                        hasSize(2)
                ));

        verify(serviceAluno, times(1)).findAll();
    }

    @Test
    public void pesquisarAlunoComNomeVazioDeveRetornarTodosOsAlunos()
            throws Exception {

        List<Aluno> alunos = Arrays.asList(
                criarAluno("Antonio Victor", "MAT003"),
                criarAluno("Carlos Silva", "MAT004")
        );

        when(serviceAluno.findAll()).thenReturn(alunos);

        mockMvc.perform(post("/pesquisar-aluno")
                        .param("nome", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/pesquisa-resultado"))
                .andExpect(model().attributeExists("ListaDeAlunos"))
                .andExpect(model().attribute(
                        "ListaDeAlunos",
                        hasSize(2)
                ));

        verify(serviceAluno, times(1)).findAll();
        verify(serviceAluno, never())
                .findByNomeContainingIgnoreCase(anyString());
    }

    @Test
    public void pesquisarAlunoComNomeDeveRetornarResultadoDaPesquisa()
            throws Exception {

        List<Aluno> alunos = Arrays.asList(
                criarAluno("Antonio Victor", "MAT005")
        );

        when(serviceAluno.findByNomeContainingIgnoreCase("Antonio"))
                .thenReturn(alunos);

        mockMvc.perform(post("/pesquisar-aluno")
                        .param("nome", "Antonio"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/pesquisa-resultado"))
                .andExpect(model().attributeExists("ListaDeAlunos"))
                .andExpect(model().attribute(
                        "ListaDeAlunos",
                        hasSize(1)
                ));

        verify(serviceAluno, times(1))
                .findByNomeContainingIgnoreCase("Antonio");

        verify(serviceAluno, never()).findAll();
    }

    private Aluno criarAluno(String nome, String matricula) {
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setMatricula(matricula);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setTurno(Turno.NOTURNO);
        aluno.setStatus(Status.ATIVO);
        return aluno;
    }
}