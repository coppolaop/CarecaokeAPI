package br.com.darksun.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class Song implements Serializable {
	@Serial
	private static final long serialVersionUID = -2505468630149843656L;
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long          id;
	private String        name;
	private String        artist;
	private String        url;
	private Boolean       hasBeenSung;
	private LocalDateTime createdAt;
	@ManyToOne
	private Guest         singer;
}
