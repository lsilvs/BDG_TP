package com.bdgeo.rest;

import java.sql.SQLException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.bdgeo.model.Location;

@Path("/")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Services {
	@Context
	UriInfo uriInfo;
	
	@POST
	@Path("add/location")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public String add_location(
			@FormParam("latitude") String latitude, 
			@FormParam("longitude") String longitude
			) {
		String geom = "POINT("+latitude+" "+longitude+")";
		String response = null;

		Location local = new Location(geom);

		try {
			response = local.save();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return response;
	}

	@POST
	@Path("getClose")
	@Consumes("application/x-www-form-urlencoded")
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML})
	public String getClose(
			@FormParam("latitude") String latitude, 
			@FormParam("longitude") String longitude
			){

		String all = new String();
		all += "var val = { 'type': 'Feature', 'properties': { 'name': 'Coors Field', 'amenity': 'Baseball Stadium', 'popupContent': 'This is where the Rockies play!' }, ";
		all += "'geometry': { 'type': 'MultiPoint', 'coordinates':  ";
		all += Location.getClose(latitude, longitude);
		all += "  } }";
		
		return all;
		
	}
	
	@GET
	@Path("getAll")
	@Consumes("application/x-www-form-urlencoded")
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML})
	public String getAll(){

		String all = new String();
		all += "var val = { 'type': 'Feature', 'properties': { 'name': 'Coors Field', 'amenity': 'Baseball Stadium', 'popupContent': 'This is where the Rockies play!' }, ";
		all += "'geometry': { 'type': 'MultiPoint', 'coordinates':  ";
		all += Location.getAll();
		all += "  } }";
		
		return all;
		
	}
	
	@GET
	@Path("hello")
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML})
	public String hello(){
		String hello = new String("HelloWorld!");
		return hello;
	}
	
	@GET
	@Path("teste")
	@Produces("application/json")
	public String teste(){
		return "Teste!";
	}
	
	@GET
	@Path("xml")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML})
	public Services getXML(){
		return this;
	}
}