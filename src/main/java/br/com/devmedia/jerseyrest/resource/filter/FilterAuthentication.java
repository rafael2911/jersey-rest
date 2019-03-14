package br.com.devmedia.jerseyrest.resource.filter;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import br.com.devmedia.jerseyrest.model.domain.ErrorMessage;
import br.com.devmedia.jerseyrest.model.domain.Usuario;
import br.com.devmedia.jerseyrest.service.UsuarioService;

@Provider
@AcessoRestrito
public class FilterAuthentication implements ContainerRequestFilter {
	
	private UsuarioService service = new UsuarioService();
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BASIC_AUTHORIZATION_PREFIX = "Basic ";

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		List<String> headersAutorizacao = requestContext.getHeaders().get(AUTHORIZATION_HEADER);
		
		if((headersAutorizacao != null) && (headersAutorizacao.size() > 0)) {
			
			String dadosAutorizacao = headersAutorizacao.get(0);
			Usuario usuario = obterUsuarioDoHeader(dadosAutorizacao);
			
			if(service.validarUsuario(usuario)) {
				return;
			}
			
		}
		
		Response naoAutorizado = Response.status(Response.Status.UNAUTHORIZED)
				.entity(new ErrorMessage("Usuário não autorizado!", Response.Status.UNAUTHORIZED.getStatusCode()))
				.build();
		
		requestContext.abortWith(naoAutorizado);
		
	}

	private Usuario obterUsuarioDoHeader(String dadosAutorizacao) {
		dadosAutorizacao = dadosAutorizacao.replaceFirst(BASIC_AUTHORIZATION_PREFIX, "");
		
		String dadosDecodificados = Base64.decodeAsString(dadosAutorizacao);
		StringTokenizer dadosTokenizer = new StringTokenizer(dadosDecodificados, ":");
		
		Usuario usuario = new Usuario();
		usuario.setUsername(dadosTokenizer.nextToken());
		usuario.setPassword(dadosTokenizer.nextToken());
		return usuario;
	}

}
