package com.example.herve.Study.bean;

/**
 * Created           :Herve on 2016/10/23.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/23
 * @ projectName     :BaseApp
 * @ version
 */

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class User implements BaseBean {
    @Id
    private String userId;//账号ID
    private String passWord;//密码
    private String userName;//名字
    private String age;//年龄
    private boolean sex;//
    private int Identity;//身份（学生，家长，老师）
    private String duties;//职务（班主任，班长）

    private String characterization;//描述


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.passWord);
        dest.writeString(this.userName);
        dest.writeString(this.age);
        dest.writeByte(this.sex ? (byte) 1 : (byte) 0);
        dest.writeInt(this.Identity);
        dest.writeString(this.duties);
        dest.writeString(this.characterization);
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassWord() {
        return this.passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean getSex() {
        return this.sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getIdentity() {
        return this.Identity;
    }

    public void setIdentity(int Identity) {
        this.Identity = Identity;
    }

    public String getDuties() {
        return this.duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getCharacterization() {
        return this.characterization;
    }

    public void setCharacterization(String characterization) {
        this.characterization = characterization;
    }

    public User() {
    }

    protected User(Parcel in) {
        this.userId = in.readString();
        this.passWord = in.readString();
        this.userName = in.readString();
        this.age = in.readString();
        this.sex = in.readByte() != 0;
        this.Identity = in.readInt();
        this.duties = in.readString();
        this.characterization = in.readString();
    }

    public User(String userId, String passWord, String userName, String age, boolean sex) {
        this.userId = userId;
        this.passWord = passWord;
        this.userName = userName;
        this.age = age;
        this.sex = sex;
    }

    @Generated(hash = 264433644)
    public User(String userId, String passWord, String userName, String age,
            boolean sex, int Identity, String duties, String characterization) {
        this.userId = userId;
        this.passWord = passWord;
        this.userName = userName;
        this.age = age;
        this.sex = sex;
        this.Identity = Identity;
        this.duties = duties;
        this.characterization = characterization;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}


