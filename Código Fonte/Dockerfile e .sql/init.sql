CREATE TABLE item
(
    id integer NOT NULL,
    name character varying NOT NULL,
    description character varying,
    CONSTRAINT "PK_Item" PRIMARY KEY (id)
);


CREATE TABLE deposit
(
    id integer NOT NULL,
    quantity integer,
    CONSTRAINT "PK_id" PRIMARY KEY (id),
    CONSTRAINT "FK_itemId" FOREIGN KEY (id)
        REFERENCES item (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

CREATE TABLE local
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT "PK_Local" PRIMARY KEY (id)
);

CREATE TABLE delivery
(
    id integer NOT NULL,
    quantity integer NOT NULL,
    local_id integer NOT NULL,
    item_id integer NOT NULL,
    CONSTRAINT "PK_Delivery" PRIMARY KEY (id),
    CONSTRAINT "FK_Delivery_Item" FOREIGN KEY (item_id)
        REFERENCES item (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT "FK_Delivery_Local" FOREIGN KEY (local_id)
        REFERENCES local (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

INSERT INTO local (id, name) VALUES
(   
    12, 'Lisboa'),
(     
    24, 'Porto'),
(   
    36, 'Setubal'),
(
    48, 'Faro'),
(
    60, 'Madeira'),
(
    72, 'Acores');

