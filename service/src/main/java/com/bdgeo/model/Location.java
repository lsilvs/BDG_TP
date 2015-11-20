package com.bdgeo.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.bdgeo.jdbc.ConnectionFactory;

public class Location {
	private Integer id;
	private String ip;
	private String geom;
	private Calendar insertedAt;

	public Location() {
		this.id = null;
		this.ip = null;
		this.geom = null;
		this.insertedAt = null;
	}

	public Location(String geom) {
		this.id = null;
		this.ip = null;
		this.geom = geom;
		this.insertedAt = null;
	}
	
	public String save() throws SQLException {

		Connection conn = new ConnectionFactory().getConnection();

		PreparedStatement stmt = null;

		stmt = conn
				.prepareStatement("INSERT INTO location (ip, geom, at) VALUES (?, '" + getGeom() + "', NOW())");
		
		InetAddress thisIp;
		String ip = null;

		try {
			thisIp = InetAddress.getLocalHost();
			ip = thisIp.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		stmt.setString(1, ip);
		
		new java.sql.Date(Calendar.getInstance().getTimeInMillis());

		stmt.execute();
		stmt.close();
		System.out.println("Gravado!");

		return "true";
	}
	
	public static String getClose(String latitude, String longitude) {
		
		Connection conn = new ConnectionFactory().getConnection();
		PreparedStatement stmt = null;
		List<Location> locations = new ArrayList<Location>();
		List<String> list = new ArrayList<String>();

		try {
			stmt = conn.prepareStatement("select ip, ST_AsEWKT(geom) geom, at from location where ST_DWithin(ST_GeomFromText('POINT(-19.90255 -43.93315)', 0), geom, 0.015 )");
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
			// criando o objeto
			Location loc = new Location();
			
			loc.setIp(rs.getString("ip"));
			loc.setGeom(rs.getString("geom"));
			
			// adicionando o objeto ˆ lista
			locations.add(loc);
			
			String aux = loc.getGeom().replace("POINT(", "");
			aux = aux.replace(")", "");
			String[] latlng = aux.split(" ");
			
			list.add(" ["+ latlng[1].substring(0, 8) + ", " + latlng[0].substring(0, 8) +"] ");

			}
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String resp = list.toString();
		
		return resp;
	}

	public static String getAll() {
		
		Connection conn = new ConnectionFactory().getConnection();
		PreparedStatement stmt = null;
		List<Location > locations = new ArrayList<Location>();
		List<String> list = new ArrayList<String>();

		try {
			stmt = conn.prepareStatement("SELECT ip, ST_AsEWKT(geom) geom, at FROM location LIMIT 500");
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
			// criando o objeto
			Location loc = new Location();
			
			loc.setIp(rs.getString("ip"));
			loc.setGeom(rs.getString("geom"));
			
			// adicionando o objeto ˆ lista
			locations.add(loc);
			
			String aux = loc.getGeom().replace("POINT(", "");
			aux = aux.replace(")", "");
			String[] latlng = aux.split(" ");
			
			list.add(" ["+ latlng[1].substring(0, 8) + ", " + latlng[0].substring(0, 8) +"] ");

			}
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String resp = list.toString();
		
		return resp;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGeom() {
		return geom;
	}

	public void setGeom(String geom) {
		this.geom = geom;
	}

	public Calendar getInsertedAt() {
		return insertedAt;
	}

	public void setInsertedAt(Calendar data) {
		this.insertedAt = data;
	}
}