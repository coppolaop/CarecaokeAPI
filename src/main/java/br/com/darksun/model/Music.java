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
	private static final long serialVersionUID = 6463603621276500765L;

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long    id;
	private String  name;
	private String  artist;
	private String  url;
	private Boolean HasBeenSung;
	@ManyToOne
	private Guest   singer;
}
