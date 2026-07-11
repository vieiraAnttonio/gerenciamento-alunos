package br.com.gerenciamento.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ServiceUsuario serviceUsuario;

    @Test
    public void salvarUsuarioComSucesso() throws Exception {
        Usuario usuario = criarUsuario();

        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(null);

        serviceUsuario.salvarUsuario(usuario);

        Assert.assertNotNull(usuario.getSenha());
        Assert.assertNotEquals("123456", usuario.getSenha());

        verify(usuarioRepository, times(1))
                .save(usuario);
    }

    @Test
    public void salvarUsuarioComEmailDuplicadoDeveLancarExcecao() {
        Usuario usuarioNovo = criarUsuario();
        Usuario usuarioExistente = criarUsuario();

        when(usuarioRepository.findByEmail(usuarioNovo.getEmail()))
                .thenReturn(usuarioExistente);

        Assert.assertThrows(
                EmailExistsException.class,
                () -> serviceUsuario.salvarUsuario(usuarioNovo)
        );

        verify(usuarioRepository, never())
                .save(any(Usuario.class));
    }

    @Test
    public void loginComCredenciaisValidasDeveRetornarUsuario() {
        Usuario usuario = criarUsuario();

        when(usuarioRepository.buscarLogin("antonio", "123456"))
                .thenReturn(usuario);

        Usuario resultado = serviceUsuario.loginUser(
                "antonio",
                "123456"
        );

        Assert.assertNotNull(resultado);
        Assert.assertEquals("antonio", resultado.getUser());

        verify(usuarioRepository, times(1))
                .buscarLogin("antonio", "123456");
    }

    @Test
    public void loginComCredenciaisInvalidasDeveRetornarNull() {
        when(usuarioRepository.buscarLogin("antonio", "senha-incorreta"))
                .thenReturn(null);

        Usuario resultado = serviceUsuario.loginUser(
                "antonio",
                "senha-incorreta"
        );

        Assert.assertNull(resultado);

        verify(usuarioRepository, times(1))
                .buscarLogin("antonio", "senha-incorreta");
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("antonio@email.com");
        usuario.setUser("antonio");
        usuario.setSenha("123456");
        return usuario;
    }
}