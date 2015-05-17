package org.keviny.gallery.rdb.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by kevin on 5/15/15.
 */
@Entity(name = "category")
public class Gategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name", unique = true, nullable = false, length = 30)
    private String name;
    @Column(name = "description", length = 255)
    private String description;
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;
    @Column(name = "last_modified_time", nullable = false)
    private Timestamp lastModifiedTime;
    @Column(name = "creator", nullable = false)
    private Integer creator;
    @Column(name = "deleted", nullable = false, length = 1)
    private Boolean deleted;
}
