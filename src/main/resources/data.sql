INSERT INTO CATEGORY (id, name, create_at, modified_at)
VALUES (1, '상의', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (2, '아우터', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (3, '바지', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (4, '스니커즈', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (5, '가방', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (6, '모자', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (7, '양말', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (8, '액세서리', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP));

INSERT INTO BRAND (id, name, status, create_at, modified_at)
VALUES (1, 'A', 'STANDBY', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (2, 'B', 'STANDBY', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (3, 'C', 'STANDBY', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (4, 'D', 'STANDBY', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (5, 'E', 'STANDBY', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (6, 'F', 'STANDBY', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (7, 'G', 'STANDBY', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (8, 'H', 'STANDBY', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (9, 'I', 'STANDBY', EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP));

INSERT INTO PRODUCT (id, price, status, brand_id, category_id, create_at, modified_at)
VALUES (1, 11200, 'STANDBY', 1, 1, EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (2, 5500, 'STANDBY', 1, 2, EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (3, 4200, 'STANDBY', 1, 3, EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (4, 9000, 'STANDBY', 1, 4, EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (5, 2000, 'STANDBY', 1, 5, EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (6, 1700, 'STANDBY', 1, 6, EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (7, 1800, 'STANDBY', 1, 7, EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP)),
       (8, 2300, 'STANDBY', 1, 8, EXTRACT(SECOND FROM CURRENT_TIMESTAMP), EXTRACT(SECOND FROM CURRENT_TIMESTAMP));
