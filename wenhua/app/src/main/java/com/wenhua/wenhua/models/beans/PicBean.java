package com.wenhua.wenhua.models.beans;

import com.wenhua.wenhua.models.BaseModel;

/**
 * Created by castiel on 2016/9/17.
 */

public class PicBean extends BaseModel {
    private static final long serialVersionUID = -3535118770213697523L;

    private int nRandomId;
    private int nRecordNumber;

    public int getnRandomId() {
        return nRandomId;
    }

    public void setnRandomId(int nRandomId) {
        this.nRandomId = nRandomId;
    }

    public int getnRecordNumber() {
        return nRecordNumber;
    }

    public void setnRecordNumber(int nRecordNumber) {
        this.nRecordNumber = nRecordNumber;
    }

    @Override
    public String toString() {
        return "PicBean{" +
                "nRandomId=" + nRandomId +
                ", nRecordNumber=" + nRecordNumber +
                '}';
    }
}
