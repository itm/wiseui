package eu.wisebed.wiseui.shared;

/**
 * @author Soenke Nommensen
 */
public class UrnPrefix implements Dto {

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
