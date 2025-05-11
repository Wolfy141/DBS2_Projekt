-- Clear tables in proper order
DELETE FROM equipment;
DELETE FROM equipment_condition;
DELETE FROM equipment_category;

INSERT INTO employee (first_name, last_name, email, phone, password)
VALUES ('Admin', 'User', 'admin@admin.cz', '+420123456789', '$2a$10$AEFy3qjSiyiBkvKLjivBsu91hpY9R0p2KnGanX3P.zykRAbck67/a');
-- Insert categories with image URLs
INSERT INTO equipment_category (category_id, category_name, description, image_url) VALUES
(1, 'Helmy', 'Ochranné helmy pro lyžování a snowboarding', 'helmet.jpg'),
(2, 'Lyže', 'Klasické sjezdové lyže', 'man-skies.jpg'),
(3, 'Boty', 'Lyžařské a snowboardové boty', 'ski-boot-257375_1280.jpg'),
(4, 'Běžky', 'Běžecké lyže', 'cross-country-skiing-3020748_1280.jpg'),
(5, 'Snowboard', 'Snowboardy různých typů', 'snowboard-227541_1280.jpg');

-- Insert conditions
INSERT INTO equipment_condition (condition_id, name, description) VALUES
(1, 'Nové', 'Nové nepoškozené'),
(2, 'lehce poškozené', 'Menší škrábance a viditelné zanedbatelné opotřebení');

-- Insert equipment
INSERT INTO equipment (name, condition_id, cost_per_day, available, category_id) VALUES
-- Helmy
('Snowboardová helma Smith', 1, 150, true, 1),
('Lyžařská helma Atomic', 2, 120, true, 1),

-- Lyže
('Lyže Rossignol Experience', 1, 400, true, 2),
('Lyže Atomic Redster', 2, 350, true, 2),

-- Boty
('Lyžařské boty Salomon', 1, 250, true, 3),
('Snowboardové boty Burton', 2, 200, true, 3),

-- Běžky
('Běžky Fischer Speedmax', 1, 180, true, 4),

-- Snowboardy
('Snowboard Jones Mountain Twin', 1, 500, true, 5),
('Snowboard Burton Custom', 2, 450, true, 5);