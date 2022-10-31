package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.stubs;

import lombok.Getter;
import lombok.Setter;
import org.apiaddicts.apitools.apigen.archetypecore.core.persistence.ApigenAbstractPersistable;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "entity_nodes")
public class FakeEntityNode extends ApigenAbstractPersistable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private FakeEntityNode parent;
	@OneToMany(mappedBy = "parent")
	private Set<FakeEntityNode> children;
	@ManyToMany
	@JoinTable(
			name = "entity_nodes_neighbors",
			joinColumns = {
					@JoinColumn(name = "source_id", referencedColumnName = "id")
			},
			inverseJoinColumns = {
					@JoinColumn(name = "target_id", referencedColumnName = "id")
			},
			uniqueConstraints = {
					@UniqueConstraint(columnNames = {"source_id", "target_id"})
			}
	)
	private Set<FakeEntityNode> neighbors;
	@OneToOne
	@JoinColumn(name="nearest_id")
	private FakeEntityNode nearest;

	@Override
	public boolean isReference() {
		return getId() != null && name == null && parent == null && children == null && neighbors == null && nearest == null;
	}
}
