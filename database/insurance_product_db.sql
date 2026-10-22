CREATE DATABASE IF NOT EXISTS insurance_product_db;
USE insurance_product_db;

CREATE TABLE IF NOT EXISTS product_category (
  category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  category_name VARCHAR(100) NOT NULL UNIQUE,
  description VARCHAR(255),
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS insurance_product (
  product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  category_id BIGINT NOT NULL,
  product_code VARCHAR(30) NOT NULL UNIQUE,
  product_name VARCHAR(150) NOT NULL UNIQUE,
  product_type VARCHAR(100) NOT NULL,
  description TEXT,
  minimum_age INT,
  maximum_age INT,
  minimum_sum_insured DECIMAL(15,2),
  maximum_sum_insured DECIMAL(15,2),
  status VARCHAR(20) NOT NULL DEFAULT 'INACTIVE',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES product_category(category_id),
  CONSTRAINT chk_product_age CHECK (minimum_age IS NULL OR maximum_age IS NULL OR minimum_age <= maximum_age),
  CONSTRAINT chk_product_sum CHECK (minimum_sum_insured IS NULL OR maximum_sum_insured IS NULL OR minimum_sum_insured <= maximum_sum_insured)
);

CREATE TABLE IF NOT EXISTS coverage_master (
  coverage_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  coverage_name VARCHAR(150) NOT NULL,
  minimum_coverage DECIMAL(15,2) NOT NULL,
  maximum_coverage DECIMAL(15,2) NOT NULL,
  waiting_period_days INT NOT NULL DEFAULT 0,
  deductible_amount DECIMAL(15,2) NOT NULL DEFAULT 0,
  CONSTRAINT fk_coverage_product FOREIGN KEY (product_id) REFERENCES insurance_product(product_id),
  CONSTRAINT chk_coverage_range CHECK (minimum_coverage <= maximum_coverage)
);

CREATE TABLE IF NOT EXISTS rider_master (
  rider_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  rider_name VARCHAR(150) NOT NULL,
  rider_description TEXT,
  rider_premium DECIMAL(15,2) NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  CONSTRAINT fk_rider_product FOREIGN KEY (product_id) REFERENCES insurance_product(product_id)
);

CREATE TABLE IF NOT EXISTS benefit_master (
  benefit_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  benefit_name VARCHAR(150) NOT NULL,
  benefit_description TEXT,
  benefit_limit DECIMAL(15,2),
  CONSTRAINT fk_benefit_product FOREIGN KEY (product_id) REFERENCES insurance_product(product_id)
);

CREATE TABLE IF NOT EXISTS premium_frequency (
  frequency_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  frequency_name VARCHAR(30) NOT NULL UNIQUE,
  installment_count INT NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  CONSTRAINT chk_installment_count CHECK (installment_count > 0)
);

CREATE TABLE IF NOT EXISTS product_commission (
  commission_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL UNIQUE,
  first_year_commission DECIMAL(5,2) NOT NULL DEFAULT 0,
  renewal_commission DECIMAL(5,2) NOT NULL DEFAULT 0,
  branch_commission DECIMAL(5,2) NOT NULL DEFAULT 0,
  CONSTRAINT fk_commission_product FOREIGN KEY (product_id) REFERENCES insurance_product(product_id),
  CONSTRAINT chk_first_commission CHECK (first_year_commission BETWEEN 0 AND 100),
  CONSTRAINT chk_renewal_commission CHECK (renewal_commission BETWEEN 0 AND 100),
  CONSTRAINT chk_branch_commission CHECK (branch_commission BETWEEN 0 AND 100)
);

INSERT IGNORE INTO premium_frequency(frequency_name, installment_count, status) VALUES
('Monthly',12,'ACTIVE'),('Quarterly',4,'ACTIVE'),('Half-Yearly',2,'ACTIVE'),
('Yearly',1,'ACTIVE'),('Single Premium',1,'ACTIVE');


ALTER TABLE insurance_product
    ADD COLUMN minimum_policy_term_years INT NULL,
ADD COLUMN maximum_policy_term_years INT NULL;

UPDATE insurance_product
SET minimum_policy_term_years = 1,
    maximum_policy_term_years = 30
WHERE product_id = 1;
