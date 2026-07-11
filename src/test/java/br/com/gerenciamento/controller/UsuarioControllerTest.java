package br.com.gerenciamento.controller;

import static org.hamcrest.Matchers.instanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.service.ServiceUsuario;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceUsuario serviceUsuario;

    @Test
    public void abrirPaginaDeLoginDeveRetornarViewCorreta()
            throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/login"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attribute(
                        "usuario",
                        instanceOf(Usuario.class)
                ));
    }

    @Test
    public void abrirPaginaDeCadastroDeveRetornarViewCorreta()
            throws Exception {

        mockMvc.perform(get("/cadastro"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/cadastro"))
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    public void loginComCredenciaisInvalidasNaoDeveRedirecionarParaIndex()
            throws Exception {

        when(serviceUsuario.loginUser(
                anyString(),
                anyString()
        )).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("user", "usuarioInvalido")
                        .param("senha", "senhaInvalida")
                        .param("email", "usuario@email.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/cadastro"))
                .andExpect(model().attributeExists("usuario"));

        verify(serviceUsuario, times(1))
                .loginUser(anyString(), anyString());
    }

    @Test
    public void loginComCredenciaisValidasDeveAbrirIndexESalvarUsuarioNaSessao()
            throws Exception {

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setEmail("antonio@email.com");
        usuarioEncontrado.setUser("antonio");
        usuarioEncontrado.setSenha("senhaCriptografada");

        when(serviceUsuario.loginUser(
                anyString(),
                anyString()
        )).thenReturn(usuarioEncontrado);

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/login")
                        .session(session)
                        .param("user", "antonio")
                        .param("senha", "123456")
                        .param("email", "antonio@email.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"))
                .andExpect(model().attributeExists("aluno"))
                .andExpect(request().sessionAttribute(
                        "usuarioLogado",
                        usuarioEncontrado
                ));

        verify(serviceUsuario, times(1))
                .loginUser(anyString(), anyString());
    }
}