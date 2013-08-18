-- parameter
CREATE TABLE :parameterTableName (
    id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    input_levels INTEGER NOT NULL,
    cacheable BOOLEAN DEFAULT true NOT NULL,
    nullable BOOLEAN DEFAULT false NOT NULL,
    array_separator CHAR(1) DEFAULT ';' NOT NULL,
    CONSTRAINT pk_:parameterTableName PRIMARY KEY(id),
    CONSTRAINT u_:parameterTableName_name UNIQUE(name)
);
CREATE SEQUENCE seq_:parameterTableName;
CREATE INDEX idx_:parameterTableName_name ON :parameterTableName(name);

-- level
CREATE TABLE :levelTableName (
    id BIGINT NOT NULL,
    param_id BIGINT NOT NULL,
    order_no BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    type VARCHAR(100) NOT NULL,
    matcher VARCHAR(100),
    level_creator VARCHAR(100),
    array_flag BOOLEAN DEFAULT false NOT NULL,
    CONSTRAINT pk_:levelTableName PRIMARY KEY(id),
    CONSTRAINT fk_:parameterTableName_param_id FOREIGN KEY(param_id) REFERENCES :parameterTableName(id)
);
CREATE SEQUENCE seq_:levelTableName;
CREATE INDEX idx_:levelTableName_param_id ON :levelTableName(param_id);

-- level entry
CREATE TABLE :parameterEntryTableName (
    id BIGINT NOT NULL ,
    param_id BIGINT NOT NULL,
    level1 VARCHAR(256),
    level2 VARCHAR(256),
    level3 VARCHAR(256),
    level4 VARCHAR(256),
    level5 VARCHAR(256),
    level6 VARCHAR(256),
    level7 VARCHAR(256),
    level8 VARCHAR(256),
    CONSTRAINT pk_:parameterEntryTableName PRIMARY KEY (id),
    CONSTRAINT fk_:parameterEntryTableName_param_id FOREIGN KEY (param_id) REFERENCES :parameterTableName(id)
);
CREATE SEQUENCE seq_:parameterEntryTableName;
CREATE INDEX idx_:parameterEntryTableName_param_id ON :parameterEntryTableName(param_id);