CREATE TABLE IF NOT EXISTS account (
	account_id BIGINT PRIMARY KEY,
	account_name VARCHAR(255) NOT NULL,
    account_state VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS product (
	id BIGSERIAL PRIMARY KEY,
	url VARCHAR(255) NOT NULL,
	current_price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS subscription (
	id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    target_price DECIMAL(10, 2) NOT NULL,
    
    FOREIGN KEY (account_id) REFERENCES account(account_id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);
