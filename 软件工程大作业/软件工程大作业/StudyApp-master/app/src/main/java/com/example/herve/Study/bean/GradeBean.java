package com.example.herve.Study.bean;

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.example.herve.Study.greendao.dao.DaoSession;
import com.example.herve.Study.greendao.dao.CurriculumBeanDao;
import com.example.herve.Study.greendao.dao.GradeBeanDao;

/**
 * Created           :Herve on 2016/10/23.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/23
 * @ projectName     :BJXAPP
 * @ version
 */

/*
* 年级类
* */
@Entity
public class GradeBean implements BaseBean {
    @Id
    private Long gradeId;//年级ID
    private String gradeName;//年级名字
    private String headTeacher;//年级班主任

    @ToMany(referencedJoinProperty = "curriculumId")
    private List<CurriculumBean> curriculumBeans=new ArrayList<>();;


    @Override
    public int describeContents() {
        return 0;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getHeadTeacher() {
        return headTeacher;
    }

    public void setHeadTeacher(String headTeacher) {
        this.headTeacher = headTeacher;
    }

    public void setCurriculumBeans(List<CurriculumBean> curriculumBeans) {
        this.curriculumBeans = curriculumBeans;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.gradeId);
        dest.writeString(this.gradeName);
        dest.writeString(this.headTeacher);
        dest.writeTypedList(this.curriculumBeans);
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1304168258)
    public List<CurriculumBean> getCurriculumBeans() {
        if (curriculumBeans == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CurriculumBeanDao targetDao = daoSession.getCurriculumBeanDao();
            List<CurriculumBean> curriculumBeansNew = targetDao
                    ._queryGradeBean_CurriculumBeans(gradeId);
            synchronized (this) {
                if (curriculumBeans == null) {
                    curriculumBeans = curriculumBeansNew;
                }
            }
        }
        return curriculumBeans;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 974620221)
    public synchronized void resetCurriculumBeans() {
        curriculumBeans = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1627665509)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGradeBeanDao() : null;
    }

    public GradeBean() {
    }

    protected GradeBean(Parcel in) {
        this.gradeId = (Long) in.readValue(Long.class.getClassLoader());
        this.gradeName = in.readString();
        this.headTeacher = in.readString();
        this.curriculumBeans = in.createTypedArrayList(CurriculumBean.CREATOR);
    }

    @Generated(hash = 1932574550)
    public GradeBean(Long gradeId, String gradeName, String headTeacher) {
        this.gradeId = gradeId;
        this.gradeName = gradeName;
        this.headTeacher = headTeacher;
    }

    public static final Creator<GradeBean> CREATOR = new Creator<GradeBean>() {
        @Override
        public GradeBean createFromParcel(Parcel source) {
            return new GradeBean(source);
        }

        @Override
        public GradeBean[] newArray(int size) {
            return new GradeBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 990633144)
    private transient GradeBeanDao myDao;
}
