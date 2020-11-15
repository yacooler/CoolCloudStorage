module common {
    exports fileobjects;
    exports frames;
    exports utils;
    exports cloudusers;
    exports database;
    exports database.entity;

    opens database.entity;

    requires org.hibernate.orm.core;
    requires org.hibernate.commons.annotations;
    requires javassist;
    requires io.netty.all;
    requires lombok;
    requires java.persistence;
    requires java.naming;
    requires java.sql;
    requires net.bytebuddy;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;

}