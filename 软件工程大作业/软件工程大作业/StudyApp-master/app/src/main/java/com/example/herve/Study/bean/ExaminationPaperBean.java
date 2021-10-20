package com.example.herve.Study.bean;

import android.os.Parcel;

import com.example.herve.Study.greendao.dao.DaoSession;
import com.example.herve.Study.greendao.dao.ExaminationPaperBeanDao;
import com.example.herve.Study.greendao.dao.QuestionBeanDao;

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
 * 学生考卷
 */
@Entity
public class ExaminationPaperBean implements BaseBean {
    @Id
    private Long examinationPaperId;//年级
    private String grade;//年级
    private String author;//命题人
    private int paperDifficulty;//试卷难度
    private int paperType;//试卷类型

    @ToMany(referencedJoinProperty = "questionId")
    private List<QuestionBean> questionBeans=new ArrayList<>();;//选的答案集


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.examinationPaperId);
        dest.writeString(this.grade);
        dest.writeString(this.author);
        dest.writeInt(this.paperDifficulty);
        dest.writeInt(this.paperType);
        dest.writeTypedList(this.questionBeans);
    }

    public Long getExaminationPaperId() {
        return this.examinationPaperId;
    }

    public void setExaminationPaperId(Long examinationPaperId) {
        this.examinationPaperId = examinationPaperId;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPaperDifficulty() {
        return this.paperDifficulty;
    }

    public void setPaperDifficulty(int paperDifficulty) {
        this.paperDifficulty = paperDifficulty;
    }

    public int getPaperType() {
        return this.paperType;
    }

    public void setPaperType(int paperType) {
        this.paperType = paperType;
    }

    public void setQuestionBeans(List<QuestionBean> questionBeans) {
        this.questionBeans = questionBeans;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 710981554)
    public List<QuestionBean> getQuestionBeans() {
        if (questionBeans == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            QuestionBeanDao targetDao = daoSession.getQuestionBeanDao();
            List<QuestionBean> questionBeansNew = targetDao
                    ._queryExaminationPaperBean_QuestionBeans(examinationPaperId);
            synchronized (this) {
                if (questionBeans == null) {
                    questionBeans = questionBeansNew;
                }
            }
        }
        return questionBeans;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1600591353)
    public synchronized void resetQuestionBeans() {
        questionBeans = null;
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
    @Generated(hash = 756318017)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getExaminationPaperBeanDao() : null;
    }

    public ExaminationPaperBean() {
    }

    protected ExaminationPaperBean(Parcel in) {
        this.examinationPaperId = (Long) in.readValue(Long.class.getClassLoader());
        this.grade = in.readString();
        this.author = in.readString();
        this.paperDifficulty = in.readInt();
        this.paperType = in.readInt();
        this.questionBeans = in.createTypedArrayList(QuestionBean.CREATOR);
    }

    @Generated(hash = 2060623054)
    public ExaminationPaperBean(Long examinationPaperId, String grade, String author,
            int paperDifficulty, int paperType) {
        this.examinationPaperId = examinationPaperId;
        this.grade = grade;
        this.author = author;
        this.paperDifficulty = paperDifficulty;
        this.paperType = paperType;
    }

    public static final Creator<ExaminationPaperBean> CREATOR = new Creator<ExaminationPaperBean>() {
        @Override
        public ExaminationPaperBean createFromParcel(Parcel source) {
            return new ExaminationPaperBean(source);
        }

        @Override
        public ExaminationPaperBean[] newArray(int size) {
            return new ExaminationPaperBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1747032759)
    private transient ExaminationPaperBeanDao myDao;
}
