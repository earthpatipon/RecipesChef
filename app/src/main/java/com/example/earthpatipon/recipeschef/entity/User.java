/* Group: Aoong Aoong
 * Members: Tanaporn 5888124, Kanjanaporn 5888178, Patipon 5888218
 */
package com.example.earthpatipon.recipeschef.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "UserID")
    private int userID;
    @ColumnInfo(name = "Username")
    private String userName;
    @ColumnInfo(name = "Password")
    private String passWord;

    /**
     * Constructor
     */
    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    /**
     * Getters and Setters
     */
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userId) {
        this.userID = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String password) {
        this.passWord = password;
    }
}
