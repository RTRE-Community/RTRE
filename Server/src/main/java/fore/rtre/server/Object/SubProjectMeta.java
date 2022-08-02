package fore.rtre.server.Object;

import javax.naming.Name;
import java.util.Date;

public class SubProjectMeta {
    private Long id;
    public Date date;

    public String name;

    public String description;

    public SubProjectMeta(Long id, Date date, String name, String description) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}
