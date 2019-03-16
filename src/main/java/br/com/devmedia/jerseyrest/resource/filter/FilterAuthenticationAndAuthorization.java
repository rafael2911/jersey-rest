package br.com.devmedia.jerseyrest.resource.filter;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import br.com.devmedia.jerseyrest.exceptions.ErrorCode;
import br.com.devmedia.jerseyrest.model.domain.ErrorMessage;
import br.com.devmedia.jerseyrest.model.domain.TipoUsuario;
import br.com.devmedia.jerseyrest.model.domain.Usuario;
import br.com.devmedia.jerseyrest.service.UsuarioService;

@Provider
@AcessoRestrito
public class FilterAuthenticationAndAuthorization implements ContainerRequestFilter {
	
	private UsuarioService service = new UsuarioService();
	
	@Context
	private ResourceInfo resourceInfo;
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BASIC_AUTHORIZATION_PREFIX = "Basic ";

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		List<String> headersAutorizacao = requestContext.getHeaders().get(AUTHORIZATION_HEADER);
		
		if((headersAutorizacao != null) && (headersAutorizacao.size() > 0)) {
			
			String dadosAutorizacao = headersAutorizacao.get(0);
			Usuario usuario = obterUsuarioDoHeader(dadosAutorizacao);
			
			Usuario usuarioAutenticado = service.validarUsuario(usuario);
			
			if(usuarioAutenticado != null) {
				autorizarUsuario(requestContext, usuarioAutenticado);
				return;
			}
			
		}
		
		Response naoAutorizado = Response.status(Response.Status.UNAUTHORIZED)
				.entity(new ErrorMessage("Usuário não autorizado!", Response.Status.UNAUTHORIZED.getStatusCode()))
				.build();
		
		requestContext.abortWith(naoAutorizado);
		
	}

	private void autorizarUsuario(ContainerRequestContext requestContext, Usuario usuarioAutenticado) {
		// lê as permissões na anotação da classe
		Class<?> classeDoRecurso = resourceInfo.getResourceClass();
		List<TipoUsuario> permissoesDaClasse = recuperarPermissoes(classeDoRecurso);
		
		// lê as permissões na anotação do método
		Method metodoDoRecurso = resourceInfo.getResourceMethod();
		List<TipoUsuario> permissoesDoMetodo = recuperarPermissoes(metodoDoRecurso);
		
		try {
			if(permissoesDoMetodo.isEmpty()) {
				verificaPermissoes(permissoesDaClasse, requestContext, usuarioAutenticado);
			}else {
				verificaPermissoes(permissoesDoMetodo, requestContext, usuarioAutenticado);
			}
		}catch (Exception ex) {
			requestContext.abortWith(
					Response.status(Response.Status.FORBIDDEN)
					.entity(new ErrorMessage("Usuário não tem permissão para executar essa função.", ErrorCode.FORBIDDEN.getCode()))
					.build());
		}
		
	}

	private void verificaPermissoes(List<TipoUsuario> permissoes, ContainerRequestContext requestContext,
			Usuario usuarioAutenticado) throws Exception {
		
		if(permissoes.contains(usuarioAutenticado.getTipoUsuario())) {
			/* Utilizado para permitir que o usuario acesse os dados apenas por ele vinculadas*/
//			Long idUsuarioAcessado = recuperarIdDaUrl(requestContext);
//			
//			if((TipoUsuario.CLIENTE.equals(usuarioAutenticado.getTipoUsuario())) && (usuarioAutenticado.getId() == idUsuarioAcessado)) {
//				return;
//			}else if(!TipoUsuario.CLIENTE.equals(usuarioAutenticado.getTipoUsuario())) {
//				return;
//			}
			return;
			
		}
		
		throw new Exception();
		
	}

	private Long recuperarIdDaUrl(ContainerRequestContext requestContext) {
		String idObtidoDaURL = requestContext.getUriInfo().getPathParameters().getFirst("usuarioId");
		
		if(idObtidoDaURL == null) {
			return 0L;
		}else {
			return Long.parseLong(idObtidoDaURL);
		}
		
	}

	private List<TipoUsuario> recuperarPermissoes(AnnotatedElement elementoAnotado) {
		
		AcessoRestrito acessoRestrito = elementoAnotado.getAnnotation(AcessoRestrito.class);
		if(acessoRestrito == null) {
			return new ArrayList<TipoUsuario>();
		}else {
			TipoUsuario[] permissoes = acessoRestrito.value();
			return Arrays.asList(permissoes);
		}
		
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
