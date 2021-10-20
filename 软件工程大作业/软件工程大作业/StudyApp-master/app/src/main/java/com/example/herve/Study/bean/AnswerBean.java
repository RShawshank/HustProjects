package com.example.herve.Study.bean;

import android.os.Parcel;

import com.example.herve.Study.greendao.dao.AnswerBeanDao;
import com.example.herve.Study.greendao.dao.DaoSession;
import com.example.herve.Study.greendao.dao.SelectBeanDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Created           :Herve on 2016/11/3.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/11/3
 * @ projectName     :StudyApp
 * @ version
 */

/**
 * 学生所选单题目对应的选项
 */
@Entity
public class AnswerBean implements BaseBean {
    @Id
    private Long answerId;//数据库ID

    private int result;//-1 答错 0//未做 1//正确
    @ToMany(referencedJoinProperty = "selectId")
    private List<SelectBean> selectBeans = new ArrayList<>();
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 877692854)
    private transient AnswerBeanDao myDao;

    @Generated(hash = 1569383734)
    public AnswerBean(Long answerId, int result) {
        this.answerId = answerId;
        this.result = result;
    }

    @Generated(hash = 1597358991)
    public AnswerBean() {
    }

    public Long getAnswerId() {
        return this.answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public void setSelectBeans(List<SelectBean> selectBeans) {
        this.selectBeans = selectBeans;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 478087438)
    public List<SelectBean> getSelectBeans() {
        if (selectBeans == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SelectBeanDao targetDao = daoSession.getSelectBeanDao();
            List<SelectBean> selectBeansNew = targetDao
                    ._queryAnswerBean_SelectBeans(answerId);
            synchronized (this) {
                if (selectBeans == null) {
                    selectBeans = selectBeansNew;
                }
            }
        }
        return selectBeans;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 487366468)
    public synchronized void resetSelectBeans() {
        selectBeans = null;
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1374602174)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAnswerBeanDao() : null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.answerId);
        dest.writeTypedList(this.selectBeans);
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    protected AnswerBean(Parcel in) {
        this.answerId = (Long) in.readValue(Long.class.getClassLoader());
        this.selectBeans = in.createTypedArrayList(SelectBean.CREATOR);
    }

    public static final Creator<AnswerBean> CREATOR = new Creator<AnswerBean>() {
        @Override
        public AnswerBean createFromParcel(Parcel source) {
            return new AnswerBean(source);
        }

        @Override
        public AnswerBean[] newArray(int size) {
            return new AnswerBean[size];
        }
    };
}
