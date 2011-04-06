package eu.wisebed.wiseui.shared.dto;

/**
 * @author Soenke Nommensen
 */
public class UrnPrefix implements Dto {

	private static final long serialVersionUID = -7693239881876639487L;

	private Integer id;

    public UrnPrefix() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UrnPrefix{" +
                "id=" + id +
                '}';
    }
}
