package com.db.pratice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "Username should not be null")
    @NotBlank(message = "Username is mandatory")
    @Size(min = 5,max = 25,message = " Username Should be b/w 5 and 25 chars")
    private String username;
    @NotNull(message = "Password should not be null")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 5,max = 20,message = "Password Should be b/w 5 and 20 chars")
    private String password;

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
