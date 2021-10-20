package com.example.herve.Study.bean;

import android.os.Parcel;

import com.example.herve.Study.greendao.dao.DaoSession;
import com.example.herve.Study.greendao.dao.QuestionBeanDao;
import com.example.herve.Study.greendao.dao.SelectBeanDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Created           :Herve on 2016/10/25.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/25
 * @ projectName     :StudyApp
 * @ version
 */


/**
 * 教师出的的试题
 */
@Entity
public class QuestionBean implements BaseBean {
    @Id
    private Long questionId;//数据库ID
    private String question;//问题
    @ToMany(referencedJoinProperty = "selectId")
    private List<SelectBean> selectBeans=new ArrayList<>();;//选项
    private String solution;//解析
    private String answerKey;//正确答案
    private int score;//分数
    private int difficulty;//难度等级
    private int type;//类型


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.questionId);
        dest.writeString(this.question);
        dest.writeTypedList(this.selectBeans);
        dest.writeString(this.solution);
        dest.writeString(this.answerKey);
        dest.writeInt(this.score);
        dest.writeInt(this.difficulty);
        dest.writeInt(this.type);
    }

    public Long getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSolution() {
        return this.solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getAnswerKey() {
        return this.answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSelectBeans(List<SelectBean> selectBeans) {
        this.selectBeans = selectBeans;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1540854847)
    public List<SelectBean> getSelectBeans() {
        if (selectBeans == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SelectBeanDao targetDao = daoSession.getSelectBeanDao();
            List<SelectBean> selectBeansNew = targetDao
                    ._queryQuestionBean_SelectBeans(questionId);
            synchronized (this) {
                if (selectBeans == null) {
                    selectBeans = selectBeansNew;
                }
            }
        }
        return selectBeans;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 45468637)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getQuestionBeanDao() : null;
    }

    public QuestionBean() {
    }

    protected QuestionBean(Parcel in) {
        this.questionId = (Long) in.readValue(Long.class.getClassLoader());
        this.question = in.readString();
        this.selectBeans = in.createTypedArrayList(SelectBean.CREATOR);
        this.solution = in.readString();
        this.answerKey = in.readString();
        this.score = in.readInt();
        this.difficulty = in.readInt();
        this.type = in.readInt();
    }

    @Generated(hash = 345654034)
    public QuestionBean(Long questionId, String question, String solution, String answerKey,
            int score, int difficulty, int type) {
        this.questionId = questionId;
        this.question = question;
        this.solution = solution;
        this.answerKey = answerKey;
        this.score = score;
        this.difficulty = difficulty;
        this.type = type;
    }

    public static final Creator<QuestionBean> CREATOR = new Creator<QuestionBean>() {
        @Override
        public QuestionBean createFromParcel(Parcel source) {
            return new QuestionBean(source);
        }

        @Override
        public QuestionBean[] newArray(int size) {
            return new QuestionBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 170334476)
    private transient QuestionBeanDao myDao;
}
