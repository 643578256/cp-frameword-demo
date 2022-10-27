package com.winshare.demo.es;



import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ESBaseVO implements Serializable {


    private String id;

    @JsonIgnore
    private List<String> ids = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIds() {
        return ids;
    }
    public void addId(String id) {
        this.ids.add(id);
    }
    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
