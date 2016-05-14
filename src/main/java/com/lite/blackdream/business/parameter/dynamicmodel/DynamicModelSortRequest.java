package com.lite.blackdream.business.parameter.dynamicmodel;

import com.lite.blackdream.framework.model.Request;

/**
 * @author LaineyC
 */
public class DynamicModelSortRequest extends Request {

    private Long id;

    private Integer fromIndex;

    private Integer toIndex;

    public DynamicModelSortRequest(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(Integer fromIndex) {
        this.fromIndex = fromIndex;
    }

    public Integer getToIndex() {
        return toIndex;
    }

    public void setToIndex(Integer toIndex) {
        this.toIndex = toIndex;
    }
}
