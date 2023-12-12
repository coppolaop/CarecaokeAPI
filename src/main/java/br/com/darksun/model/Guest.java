package br.com.darksun.model;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@UserDefinition
@Entity
public class Guest implements Serializable {
	@Serial
	private static final long serialVersionUID = -3019283873636077609L;

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long   id;
	@Username
	private String name;
	@Password
	private String password;
	@Roles
	private String role;

	@JsonbTransient
	public String getPassword( ) {
		return password;
	}
}
