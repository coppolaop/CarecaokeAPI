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
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@UserDefinition
@Entity
public class Guest implements Serializable {
	@Serial
	private static final long serialVersionUID = -9103987095886310518L;

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
