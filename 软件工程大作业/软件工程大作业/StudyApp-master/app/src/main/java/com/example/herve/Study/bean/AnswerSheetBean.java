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
import com.example.herve.Study.greendao.dao.AnswerBeanDao;
import com.example.herve.Study.greendao.dao.AnswerSheetBeanDao;

/**
 * Created           :Herve on 2016/11/7.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/11/7
 * @ projectName     :StudyApp
 * @ version
 */
@Entity
public class AnswerSheetBean implements BaseBean {
    @Id
    private Long examinationPaperId;//年级
    private String grade;//年级
    private String author;//命题人
    private String student;//命题人
    private int paperDifficulty;//试卷难度
    private int paperType;//试卷类型
    private int totalScore;//总分
    private int totalPoints;//总得分

    @ToMany(referencedJoinProperty = "answerId")
    private List<AnswerBean> answerBeans = new ArrayList<>();//选的答案集


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.examinationPaperId);
        dest.writeString(this.grade);
        dest.writeString(this.author);
        dest.writeString(this.student);
        dest.writeInt(this.paperDifficulty);
        dest.writeInt(this.paperType);
        dest.writeTypedList(this.answerBeans);
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

    public String getStudent() {
        return this.student;
    }

    public void setStudent(String student) {
        this.student = student;
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


    public void setAnswerBeans(List<AnswerBean> answerBeans) {
        this.answerBeans = answerBeans;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1107319066)
    public List<AnswerBean> getAnswerBeans() {
        if (answerBeans == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AnswerBeanDao targetDao = daoSession.getAnswerBeanDao();
            List<AnswerBean> answerBeansNew = targetDao
                    ._queryAnswerSheetBean_AnswerBeans(examinationPaperId);
            synchronized (this) {
                if (answerBeans == null) {
                    answerBeans = answerBeansNew;
                }
            }
        }
        return answerBeans;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1140255602)
    public synchronized void resetAnswerBeans() {
        answerBeans = null;
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
    @Generated(hash = 1672360273)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAnswerSheetBeanDao() : null;
    }

    public int getTotalScore() {
        return this.totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalPoints() {
        return this.totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public AnswerSheetBean() {
    }

    protected AnswerSheetBean(Parcel in) {
        this.examinationPaperId = (Long) in.readValue(Long.class.getClassLoader());
        this.grade = in.readString();
        this.author = in.readString();
        this.student = in.readString();
        this.paperDifficulty = in.readInt();
        this.paperType = in.readInt();
        this.answerBeans = in.createTypedArrayList(AnswerBean.CREATOR);
    }

    @Generated(hash = 708364313)
    public AnswerSheetBean(Long examinationPaperId, String grade, String author,
            String student, int paperDifficulty, int paperType, int totalScore,
            int totalPoints) {
        this.examinationPaperId = examinationPaperId;
        this.grade = grade;
        this.author = author;
        this.student = student;
        this.paperDifficulty = paperDifficulty;
        this.paperType = paperType;
        this.totalScore = totalScore;
        this.totalPoints = totalPoints;
    }

    public static final Creator<AnswerSheetBean> CREATOR = new Creator<AnswerSheetBean>() {
        @Override
        public AnswerSheetBean createFromParcel(Parcel source) {
            return new AnswerSheetBean(source);
        }

        @Override
        public AnswerSheetBean[] newArray(int size) {
            return new AnswerSheetBean[size];
        }
    };
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 741696538)
    private transient AnswerSheetBeanDao myDao;
}
