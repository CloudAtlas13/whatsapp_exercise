/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;


/**
 *
 * @author upcnet
 */
public class UserInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  private Integer id;
  private String name;
  private String surname;
  private Collection<Message> messageCollection;
  private Collection<Message> messageCollection1;
  private Collection<User> userCollection;

  public UserInfo() {
  }

  public UserInfo(Integer id) {
    this.id = id;
  }

  public UserInfo(Integer id, String name, String surname) {
    this.id = id;
    this.name = name;
    this.surname = surname;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public Collection<Message> getMessageCollection() {
    return messageCollection;
  }

  public void setMessageCollection(Collection<Message> messageCollection) {
    this.messageCollection = messageCollection;
  }

  public Collection<Message> getMessageCollection1() {
    return messageCollection1;
  }

  public void setMessageCollection1(Collection<Message> messageCollection1) {
    this.messageCollection1 = messageCollection1;
  }

  public Collection<User> getUserCollection() {
    return userCollection;
  }

  public void setUserCollection(Collection<User> userCollection) {
    this.userCollection = userCollection;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof UserInfo)) {
      return false;
    }
    UserInfo other = (UserInfo) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entity.UserInfo[ id=" + id + " ]";
  }
  
}
