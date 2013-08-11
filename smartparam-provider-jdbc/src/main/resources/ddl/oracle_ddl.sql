-- parameter
CREATE TABLE :parameterTableName (
    id NUMBER(10,0) NOT NULL,
    name VARCHAR2(200 CHAR) NOT NULL,
    input_levels NUMBER(10,0) NOT NULL,
    cacheable NUMBER(1,0) DEFAULT 1 NOT NULL,
    nullable NUMBER(1,0) NOT NULL,
    array_separator VARCHAR2(1 CHAR) DEFAULT ';' NOT NULL,
    CONSTRAINT pk_:parameterTableName PRIMARY KEY (id),
    CONSTRAINT u_:parameterTableName_name UNIQUE (name)
)

-- parameter level
CREATE TABLE :levelTableName (
    id NUMBER(10,0) NOT NULL,
    param_id NUMBER(10,0) NOT NULL,
    order_no NUMBER(10,0) NOT NULL,
    name VARCHAR2(200 CHAR) NOT NULL,
    type VARCHAR2(100 CHAR) NOT NULL,
    matcher VARCHAR2(100 CHAR),
    level_creator VARCHAR2(100 CHAR),
    array_flag NUMBER(1,0) DEFAULT 0 NOT NULL,
    CONSTRAINT pl_:levelTableName PRIMARY KEY (id),
    CONSTRAINT fk_:levelTableName_param_id FOREIGN KEY (param_id) REFERENCES :parameterTableName (id)
)

-- parameter entry
CREATE TABLE :parameterEntryTableName (
    id NUMBER(10,0) NOT NULL ,
    param_id NUMBER(10,0) NOT NULL ,
    level1 VARCHAR2(256 CHAR),
    level2 VARCHAR2(256 CHAR),
    level3 VARCHAR2(256 CHAR),
    level4 VARCHAR2(256 CHAR),
    level5 VARCHAR2(256 CHAR),
    level6 VARCHAR2(256 CHAR),
    level7 VARCHAR2(256 CHAR),
    level8 VARCHAR2(256 CHAR),
    CONSTRAINT pk_:parameterEntryTableName PRIMARY KEY (id),
    CONSTRAINT fk_:parameterEntryTableName_param_id FOREIGN KEY (param_id) REFERENCES :parameterTableName (id)
)
