/* Creates a parent table, WritingGroup, which contains information regarding a
   group of writers. It is uniquely identified by group names as the primary key and 
   has a one-to-many relationship with Books. */
CREATE TABLE WritingGroup (
    groupName VARCHAR(50) NOT NULL,
    headWriter VARCHAR(50),
    yearFormed INT,
    subject VARCHAR(50),

    -- Primary key constraint uniquely identifies WritingGroup by the name of a writing group
    CONSTRAINT WritingGroup_PK PRIMARY KEY (groupName)
);

/* Creates a parent table, Publisher, which contains information regarding a particular book publisher.
   It is uniquely identified by the name of the publisher as the primary key and has a
   one-to-many relationship with Books. */
CREATE TABLE Publisher (
    publisherName VARCHAR(50) NOT NULL,
    publisherAddress VARCHAR(50),
    publisherPhone VARCHAR(50),
    publisherEmail VARCHAR(50),

    -- Primary key constraint uniquely identifies Publisher by the name of a publisher
    CONSTRAINT Publisher_PK PRIMARY KEY (publisherName)
);

/* Creates a child table, Books, associated with the parent tables, WritingGroup and Publisher,
   which contains information regarding a particular book. It is uniquely identified by the name of the book and
   its writing group's name. It has a one-to-many relationship with WritingGroup and Publisher. */
CREATE TABLE Books (
    bookTitle VARCHAR(100) NOT NULL,
    yearPublished INT,
    numberOfPages INT,
    groupName VARCHAR(50) NOT NULL,
    publisherName VARCHAR(50),

    -- Primary key constraint uniquely indentifies Books by the name of a book and its writing group's name
    CONSTRAINT Books_PK PRIMARY KEY (bookTitle, groupName),
 
    -- Copies in the primary key of WritingGroup as a foreign key in Books
    CONSTRAINT Books_WritingGroup_FK FOREIGN KEY (groupName)
        REFERENCES WritingGroup (groupName),

    -- Copies in the primary key of Publisher as a foreign key in Books
    CONSTRAINT Books_Publisher_FK FOREIGN KEY (publisherName)
        REFERENCES Publisher (publisherName)
);

/* Books can also be uniquely identified by the name of a book and its publisher's name, making it a candidate key.
   This alter statement adds this candidate key in the form a unique constraint. */
ALTER TABLE Books
ADD CONSTRAINT Books_UK UNIQUE (bookTitle, publisherName);

-- Inserts data into WritingGroup
INSERT INTO WritingGroup
    VALUES ('Cultured Writers', 'Justin Quares', 2019, 'Current Events'),
           ('We Love Writing!', 'Buddy Nights', 2012, 'Fiction'),
           ('Seriously Committed', 'Steven Tanner', 1997, 'Non-Fiction'),
           ('Gamerz', 'Tymee Xong', 2016, 'Gaming'),
           ('Fashionistas', 'Margo Quarter', 2018, 'Fashion');

-- Inserts data into Publisher
INSERT INTO Publisher
    VALUES ('Greenway Press', '123 Christmas Way', '(718) 880-4321', 'info@greenwaypress.com'),
           ('Biggg Publishers', '1020 Blitz Lane', '(123) 111-2020', 'bigemail@bigggpublishers.com'),
           ('Ogres', 'Swamp Avenue', '(212) 808-5050', 'iman@ogre.com'),
           ('The Publishers Pub', '010 Pattys Circle', '(111) 222-3333', NULL),
           ('Lastly, Us', '000 The Final Destination', NULL, NULL);

-- Inserts data into Books
INSERT INTO Books
    VALUES ('Elf', 2003, 100, 'We Love Writing!', 'Greenway Press'),
           ('Life as an Ogre', 2005, 1000, 'Cultured Writers', 'Ogres'),
           ('Book of Steven', 2019, 50, 'Gamerz', 'The Publishers Pub'),
           ('You and Me', 2016, 250, 'Seriously Committed', 'Lastly, Us'),
           ('Fashion Mag', 2000, 200, 'Fashionistas', 'Biggg Publishers'),
           ('Writing Manual', 2001, 99, 'We Love Writing!', 'The Publishers Pub'),
           ('Accessories for Ogres', 2019, 20, 'Fashionistas', 'Ogres'),
           ('How to live life: the life', 2016, 250, 'Cultured Writers', 'Biggg Publishers'),
           ('Elf: Part 2', 2019, 100, 'We Love Writing!', 'Greenway Press'),
           ('Games Over the Years', 2019, 150, 'Gamerz', 'The Publishers Pub');
