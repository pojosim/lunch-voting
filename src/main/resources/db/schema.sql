drop table IF EXISTS votes;
drop table IF EXISTS dishes;
drop table IF EXISTS menus;
drop table IF EXISTS restaurants;
drop table IF EXISTS user_roles;
drop table IF EXISTS users;

create TABLE users (
  id               INTEGER GENERATED BY DEFAULT AS IDENTITY(START with 1000, INCREMENT BY 1) PRIMARY KEY,
  name             VARCHAR(100)              NOT NULL,
  email            VARCHAR(100)              NOT NULL,
  password         VARCHAR(100)              NOT NULL,
  CONSTRAINT email_idx UNIQUE (email)
);

create TABLE user_roles (
    user_id INTEGER NOT NULL,
    role    VARCHAR(255),
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON delete CASCADE
);

create TABLE restaurants (
  id   INTEGER GENERATED BY DEFAULT AS IDENTITY(START with 1000, INCREMENT BY 1) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  CONSTRAINT name_idx UNIQUE (name)
);

create TABLE menus (
  id            INTEGER GENERATED BY DEFAULT AS IDENTITY(START with 1000, INCREMENT BY 1) PRIMARY KEY,
  menu_date     DATE    NOT NULL,
  restaurant_id INTEGER NOT NULL,
  CONSTRAINT date_restaurant_id_idx UNIQUE (menu_date, restaurant_id),
  FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON delete CASCADE
);

create TABLE dishes (
  id      INTEGER GENERATED BY DEFAULT AS IDENTITY(START with 1000, INCREMENT BY 1) PRIMARY KEY,
  name    VARCHAR(100) NOT NULL,
  price   DECIMAL(8,2) NOT NULL,
  menu_id INTEGER,
  CONSTRAINT name_price_menu_idx UNIQUE (name, price, menu_id),
  FOREIGN KEY (menu_id) REFERENCES menus (id) ON delete CASCADE
);

create TABLE votes (
  id            INTEGER GENERATED BY DEFAULT AS IDENTITY(START with 1000, INCREMENT BY 1) PRIMARY KEY,
  vote_date     DATE    NOT NULL,
  user_id       INTEGER NOT NULL,
  restaurant_id INTEGER NOT NULL,
  CONSTRAINT user_date_idx UNIQUE (user_id, vote_date),
  FOREIGN KEY (user_id) REFERENCES users (id) ON delete CASCADE,
  FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON delete CASCADE
);