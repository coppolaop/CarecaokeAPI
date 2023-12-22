package br.com.darksun.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class Music implements Serializable {
	@Serial
	private static final long serialVersionUID = -5004976300148121153L;
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long    id;
	private String  name;
	private String  artist;
	private String  url;
	private Boolean hasBeenSung;
	@ManyToOne
	private Guest   singer;
}
