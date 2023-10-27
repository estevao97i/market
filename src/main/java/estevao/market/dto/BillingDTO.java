package estevao.market.dto;

import java.io.Serializable;

public class BillingDTO implements Serializable {
    private boolean free;
    private boolean database;

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isDatabase() {
        return database;
    }

    public void setDatabase(boolean database) {
        this.database = database;
    }
}
