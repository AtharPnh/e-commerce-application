-- Insert sample data into category table
INSERT INTO category (id, name, description)
VALUES (nextval('category_seq'), 'Electronics', 'Devices and gadgets'),
       (nextval('category_seq'), 'Books', 'Printed and digital books'),
       (nextval('category_seq'), 'Clothing', 'Men and women clothing'),
       (nextval('category_seq'), 'Home Appliances', 'Appliances and kitchen equipment'),
       (nextval('category_seq'), 'Sports', 'Sports and fitness equipment');

-- View inserted categories with generated IDs
SELECT *
FROM category;

-- Insert sample data into product table
INSERT INTO product (id, name, description, price, available_quantity, category_id)
VALUES (nextval('product_seq'), 'Smartphone', 'Latest Android smartphone', 799.99, 50,
        (SELECT id FROM category WHERE name = 'Electronics')),
       (nextval('product_seq'), 'Laptop', '15-inch laptop with 16GB RAM', 1299.99, 30,
        (SELECT id FROM category WHERE name = 'Electronics')),
       (nextval('product_seq'), 'Bluetooth Headphones', 'Noise cancelling headphones', 199.99, 80,
        (SELECT id FROM category WHERE name = 'Electronics')),

       (nextval('product_seq'), 'Fiction Novel', 'Bestselling fiction novel', 19.99, 200,
        (SELECT id FROM category WHERE name = 'Books')),
       (nextval('product_seq'), 'Cookbook', 'Healthy recipes for everyday cooking', 29.99, 100,
        (SELECT id FROM category WHERE name = 'Books')),

       (nextval('product_seq'), 'T-Shirt', 'Cotton round-neck t-shirt', 14.99, 150,
        (SELECT id FROM category WHERE name = 'Clothing')),
       (nextval('product_seq'), 'Jeans', 'Slim fit denim jeans', 49.99, 100,
        (SELECT id FROM category WHERE name = 'Clothing')),

       (nextval('product_seq'), 'Microwave Oven', '700W microwave with grill', 199.99, 40,
        (SELECT id FROM category WHERE name = 'Home Appliances')),
       (nextval('product_seq'), 'Vacuum Cleaner', 'Bagless vacuum cleaner', 149.99, 60,
        (SELECT id FROM category WHERE name = 'Home Appliances')),

       (nextval('product_seq'), 'Tennis Racket', 'Lightweight carbon fiber racket', 89.99, 70,
        (SELECT id FROM category WHERE name = 'Sports')),
       (nextval('product_seq'), 'Yoga Mat', 'Non-slip yoga mat', 24.99, 120,
        (SELECT id FROM category WHERE name = 'Sports'));

-- View all inserted products
SELECT *
FROM product;
