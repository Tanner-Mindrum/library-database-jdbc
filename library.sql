CREATE TABLE WritingGroup (
    groupName VARCHAR(50) NOT NULL,
    headWriter VARCHAR(50),
    yearFormed INT,
    subject VARCHAR(50),

    CONSTRAINT WritingGroup_PK PRIMARY KEY (groupName)
);

CREATE TABLE Publisher (
    publisherName VARCHAR(50) NOT NULL,
    publisherAddress VARCHAR(50),
    publisherPhone VARCHAR(50),
    publisherEmail VARCHAR(50),

    CONSTRAINT Publisher_PK PRIMARY KEY (publisherName)
);

CREATE TABLE Books (
    bookTitle VARCHAR(100) NOT NULL,
    yearPublished INT,
    numberOfPages INT,
    groupName VARCHAR(50) NOT NULL,
    publisherName VARCHAR(50),

    CONSTRAINT Books_PK PRIMARY KEY (bookTitle, groupName),
 
    CONSTRAINT Books_WritingGroup_FK FOREIGN KEY (groupName)
        REFERENCES WritingGroup (groupName),

    CONSTRAINT Books_Publisher_FK FOREIGN KEY (publisherName)
        REFERENCES Publisher (publisherName)
);

ALTER TABLE Books
ADD CONSTRAINT Books_UK UNIQUE (bookTitle, publisherName);

-- SELECT * FROM WritingGroup;
-- SELECT * FROM Publisher;
-- SELECT * FROM Books;
