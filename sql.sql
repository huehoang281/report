SELECT * FROM selfstudy.study;
use selfstudy;
-- update 
UPDATE study
SET name = 'lan', email = 'lan@gmail.com'
WHERE id = 1;

-- select 
SELECT age , email
FROM study;

-- delete
DELETE FROM study
WHERE id = 2;

-- Order By
SELECT * FROM study
ORDER BY age;

-- GROUP BY
SELECT age, COUNT(name)
FROM study
GROUP BY age;

-- AND OR
SELECT id, name
FROM study
WHERE id = 4 OR id = 7 AND age = 22;

-- select distinct
SELECT DISTINCT email,age FROM study;

-- insert
INSERT INTO study
VALUES (null, 'haha', 25, 'hue2@gmail.com');

-- where
SELECT age FROM study
WHERE age = 32;

-- Join
SELECT product.product_id, study.age, product.product_name
FROM product
LEFT JOIN study ON study.id=product.product_id;

-- view 
CREATE VIEW selfstudy.studies AS
SELECT age
FROM study
WHERE age = 32;
select *from studies;
-- CREATE VIEW search AS
-- SELECT age
-- FROM study;

-- CASE
SELECT age, name,
CASE WHEN age = 21 THEN 'Hic'
WHEN age = 32 THEN 'bin'
ELSE 'nữ'
END AS name
FROM study;

-- subquery 
SELECT id,name, email
FROM study
WHERE id IN (SELECT study_id
FROM product
)
