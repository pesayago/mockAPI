package com.psayago.pruebaApp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

/**
 * Entidad que representa un producto. Sus campos se corresponden con los
 * objetos del archivo {@code object.json}: id, name, price, image, description y badge.
 */
@Entity
@Table(name = "products")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String name;

	// El precio se modela como texto ("$24.00") para coincidir con object.json
	private String price;

	private String image;

	@Column(length = 1000)
	private String description;

	private String badge;

	public Product() {
	}

	public Product(Long id, String name, String price, String image, String description, String badge) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.description = description;
		this.badge = badge;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}
}
