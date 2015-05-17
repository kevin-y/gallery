package org.keviny.gallery.rdb.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by kevin on 5/15/15.
 */
@Entity(name = "album")
public class Album implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "description", length = 255)
    private String description;
    @Column(name = "owner", nullable =  false)
    private Integer owner;
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;
    @Column(name = "last_modified_time", nullable = false)
    private Timestamp lastModifiedTime;
    @Column(name = "cover", nullable = false, length = 100)
    private String cover;
    @Column(name = "is_public", nullable = false, length = 1)
    private Boolean makePublic;
    @Column(name = "deleted", nullable = false, length = 1)
    private Boolean deleted;



}
