package br.com.devmedia.jerseyrest.resource.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

import br.com.devmedia.jerseyrest.model.domain.TipoUsuario;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AcessoRestrito {
	TipoUsuario[] value() default {TipoUsuario.CLIENTE, TipoUsuario.FUNCIONARIO, TipoUsuario.ADMINISTRADOR};
}
