package com.example.herve.Study.bean;

/**
 * Created           :Herve on 2016/10/23.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/23
 * @ projectName     :BJXAPP
 * @ version
 */

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 课程类
 */
@Entity
public class CurriculumBean implements BaseBean {
    @Id
    private Long curriculumId;//课程ID
    private int resId;//课程图标
    private String curriculumName;//课程名字
    private String teacher;//课程任教老师


    public Long getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(Long curriculumId) {
        this.curriculumId = curriculumId;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getCurriculumName() {
        return curriculumName;
    }

    public void setCurriculumName(String curriculumName) {
        this.curriculumName = curriculumName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.curriculumId);
        dest.writeInt(this.resId);
        dest.writeString(this.curriculumName);
        dest.writeString(this.teacher);
    }

    public CurriculumBean() {
    }

    protected CurriculumBean(Parcel in) {
        this.curriculumId = (Long) in.readValue(Long.class.getClassLoader());
        this.resId = in.readInt();
        this.curriculumName = in.readString();
        this.teacher = in.readString();
    }

    @Generated(hash = 1348755391)
    public CurriculumBean(Long curriculumId, int resId, String curriculumName,
            String teacher) {
        this.curriculumId = curriculumId;
        this.resId = resId;
        this.curriculumName = curriculumName;
        this.teacher = teacher;
    }

    public static final Creator<CurriculumBean> CREATOR = new Creator<CurriculumBean>() {
        @Override
        public CurriculumBean createFromParcel(Parcel source) {
            return new CurriculumBean(source);
        }

        @Override
        public CurriculumBean[] newArray(int size) {
            return new CurriculumBean[size];
        }
    };
}
