package com.catapultlearning.walkthrough.dto.observation.input;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.catapultlearning.walkthrough.web.json.CustomDateDeserializer;
import com.catapultlearning.walkthrough.web.json.CustomDateSerializer;

public class UserDTO {

	private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private String nickName;
    private String nameSuffix;
    private String eMailAddress;
    private String password;
    private Date birthday;
    
    public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getNameSuffix() {
        return nameSuffix;
    }
    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }
    public String getEMailAddress() {
        return eMailAddress;
    }
    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getBirthday() {
        return birthday;
    }
    @JsonDeserialize(using = CustomDateDeserializer.class)
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    
}
