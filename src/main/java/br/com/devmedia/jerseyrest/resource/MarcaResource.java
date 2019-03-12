package br.com.devmedia.jerseyrest.resource;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.devmedia.jerseyrest.model.domain.Marca;
import br.com.devmedia.jerseyrest.service.MarcaService;

@Path("/marcas")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class MarcaResource {

	private MarcaService service = new MarcaService();
	
	@GET
	public List<Marca> getMarcas(@BeanParam FilterBean filterBean){
		
		if(filterBean.getOffset() >= 0 && filterBean.getLimit() > 0) {
			return service.findByPagination(filterBean.getOffset(), filterBean.getLimit());
		}
		
		if(filterBean.getName() != null) {
			return service.findByName(filterBean.getName());
		}
		
		return service.findAll();
	}
	
	@GET
	@Path("/{marcaId}")
	public Response getMarca(@PathParam("marcaId") Long id) {
		Marca marca = service.findById(id);
		return Response.ok(marca).build();
	}
	
	@POST
	public Response save(Marca marca) {
		marca = service.save(marca);
		return Response.status(Status.CREATED)
				.entity(marca)
				.build();
	}
	
	@PUT
	@Path("/{marcaId}")
	public Response update(@PathParam("marcaId") Long id, Marca marca) {
		marca.setId(id);
		service.update(marca);
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{marcaId}")
	public Response delete(@PathParam("marcaId") Long id) {
		service.delete(id);
		return Response.noContent().build();
	}
	
	@Path("/{marcaId}/produtos")
	public ProdutoResource getProdutoResource() {
		return new ProdutoResource();
	}
	
}
