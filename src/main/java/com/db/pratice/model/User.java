package com.db.pratice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * USER ENTITY (OR) TABLE WITH ATTRIBUTES
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "UserDetails")
public class User {

    /**
     * unique
     * NOT NULL
     * PRIMARY KEY
     */
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)//Automatically gives unique values for Id
    private Integer id;
    /**
     * should be string
     */
    private String username;
    private String password;

    /**
     * CHAR either M OR F
     */
    //private char gender;

    /**
     * Boolean either TRUE(1) OR FALSE(0)
     */
    //private  Boolean employee;

/*
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_role", referencedColumnName = "roleId")
    private Role role;
*/

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "User_Role",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id")  //FK (which is Pk in OWN Entity)
            ,inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "roleId") //FK (Which is PK in Another Entity)
    )
    private List<Role> role= new ArrayList<>();


}
