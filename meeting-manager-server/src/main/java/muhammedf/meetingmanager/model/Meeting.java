package muhammedf.meetingmanager.model;

import javax.persistence.*;
import java.io.Serializable;

import java.util.Set;
import java.util.HashSet;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.Column;

@Entity
@XmlRootElement
public class Meeting implements Serializable, Identity<Long> {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "MEET_DEP", joinColumns = @JoinColumn(name = "MEET_ID"), inverseJoinColumns = @JoinColumn(name = "DEP_ID"))
	private Set<Department> departments = new HashSet<Department>();

	@Column
	private String name;

	@Column
	private String description;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Meeting)) {
			return false;
		}
		Meeting other = (Meeting) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Set<Department> getDepartments() {
		return this.departments;
	}

	public void setDepartments(final Set<Department> departments) {
		this.departments = departments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (name != null && !name.trim().isEmpty())
			result += "name: " + name;
		if (description != null && !description.trim().isEmpty())
			result += ", description: " + description;
		return result;
	}
}