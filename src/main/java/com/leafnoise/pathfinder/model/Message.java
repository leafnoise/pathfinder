package com.leafnoise.pathfinder.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;


@Entity
@Table(name="Message")
public class Message {

	@Id
	@GeneratedValue (strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotNull(message="Debe ingresar una fecha.")
	@Past(message="La fecha no puede ser posterior a la actual")
	private Date receivedDate;
	
	@NotNull(message="Debe tener un emisor.")
	private String origin;
	
		
	@NotNull(message="Debe tener un contenido.")
	private String payload;
	
	
	
		
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	
	
	
	

}
