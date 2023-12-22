package br.com.darksun.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
public class Vote {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long  id;
	private Short score;

	@ManyToOne
	private Song  itsFor;
	@ManyToOne
	private Guest whoVotes;
}
