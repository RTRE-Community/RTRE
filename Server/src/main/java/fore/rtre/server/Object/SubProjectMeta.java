package fore.rtre.server.Object;

import java.util.Date;

public class SubProjectMeta {

    public SubProjectMeta(Long id, Date date) {
        this.id = id;
        this.date = date;
    }

    private Long id;
    public Date date;

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


}
